package com.yodean.oa.common.plugin.document.service;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.DocumentCategory;
import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.common.exception.OANoSuchElementException;
import com.yodean.oa.common.plugin.document.dao.DocumentRepository;
import com.yodean.oa.common.plugin.document.dto.ImageDocument;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.plugin.document.enums.FileType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by rick on 2018/3/22.
 */
@Service
public class DocumentService {

    @Resource
    private DocumentRepository documentRepository;

    @Resource
    private DocumentHandler documentHandler;

    @Resource
    private ImageHandler imageHandler;

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     *
     * @param file 文件
     * @param folderPath 文件存储路径
     * @param parentId 所属文件夹
     * @param category 分类
     * @param Id 分类id
     * @return
     * @throws IOException
     */
    @Transactional
    public Document upload(MultipartFile file, String folderPath, Integer parentId, DocumentCategory category, Integer Id) throws IOException {
        //上传到服务器
        Document document = documentHandler.store(folderPath, file);
        document.setCategory(category);
        document.setCategoryId(Id);
        document.setParentId(parentId);
        document.setInherit(true);
        document.setFullName(getUniqueName(parentId, FileType.FILE, document.getFullName())); //如果重名自动编号
        return save(document);
    }

    /***
     * 自动裁剪
     * @param folder
     * @param id
     * @param aspectRatioW
     * @param aspectRatioH
     * @return
     * @throws IOException
     */
    public Document cropAuto(String folder, Integer id, Integer aspectRatioW, Integer aspectRatioH) throws IOException {
        Document document = findById(id);
        ImageDocument image = new ImageDocument();
        BeanUtils.copyProperties(document, image);

        return imageHandler.cropPic(folder, image, aspectRatioW, aspectRatioH);
    }


    /***
     * 手动裁剪
      * @param folder
     * @param id
     * @param x
     * @param y
     * @param w
     * @param h
     * @param aspectRatioW
     * @param aspectRatioH
     * @return
     * @throws IOException
     */
    public Document cropManual(String folder, Integer id,
                         Integer x, Integer y, Integer w, Integer h,
                         Integer aspectRatioW, Integer aspectRatioH) throws IOException {
        Document document = findById(id);
        ImageDocument image = new ImageDocument();
        BeanUtils.copyProperties(document, image);

        return imageHandler.cropPic(folder, image, x, y, w, h, aspectRatioW, aspectRatioH);

    }

    public Document save(Document document) {
        return documentRepository.save(document);
    }

    public List<Document> save(Iterable<Document> iterator) {
        return documentRepository.saveAll(iterator);
    }

    public Document findById(Integer id) {
        Optional<Document> optional = documentRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }

        throw new OANoSuchElementException();
    }

    /***
     * 逻辑删除文件
     * @param id
     */
    public void delete(Integer id) {
        documentRepository.deleteLogical(id);
    }

    /***
     * 绑定附件到实例上
     * @param docIds
     * @param categoryId
     */
    public void update(Set<Integer> docIds, DocumentCategory category, Integer categoryId) {
        Validate.notNull(categoryId);
        Validate.notNull(docIds);

        if (docIds.size() == 0) {
            return;
        }

        String sql = "UPDATE sys_document set category = ?, category_id = ? WHERE id = ?";

        List<Object[]> params = new ArrayList<>(docIds.size());
        for (Integer docId : docIds) {
            params.add(new Object[]{category.name(), categoryId, docId});
        }

        jdbcTemplate.batchUpdate(sql, params);
    }

    /**
     * 查找实例的所有文件
     * @param category
     * @param categoryId
     * @return
     */
    public List<Document> findById(DocumentCategory category, Integer categoryId) {
        Document document = new Document();
        document.setCategory(category);
        document.setCategoryId(categoryId);
        document.setDelFlag(DataEntity.DEL_FLAG_NORMAL);

        Example example = Example.of(document);
        return documentRepository.findAll(example);
    }

    /**
     * 查看所有父目录
     * @param id
     * @return
     */
    public List<Document> findParentsDocument(Integer id) {
        return findDocumentPath(id, false);
    }

    /**
     * 查看文件路径
     * @param id
     * @return
     */
    public List<Document> findDocumentPath(Integer id) {
        return findDocumentPath(id, true);
    }

    /**
     * 查看文件路径
     * @param id
     * @return
     */
    private List<Document> findDocumentPath(Integer id, boolean includeSelf) {
        Document document =  findById(id);

        List<Document> parentsDocument = new ArrayList<>();

        Document curDocument = document;

        while (curDocument.getParentId() != null) {
            Document parent = findById(curDocument.getParentId());
            parentsDocument.add(parent);
            curDocument = parent;
        }

        Collections.reverse(parentsDocument);

        if (includeSelf)
            parentsDocument.add(document);

        return parentsDocument;

    }

    /**
     * 新增加文件，检查文件是否重复
     * @param parentId 父目录
     * @param fileFullName 新的名称
     * @return
     */
    public boolean isUniqueFileNameWithNew(Integer parentId, FileType fileType, String fileFullName) {
        List<Document> siblingsList = findSubDocument(parentId);

        for(Document document : siblingsList) {
            if(document.getFileType() == fileType && document.getFullName().equals(fileFullName)) {
                 return false;
            }
        }

        return true;
    }

    /**
     * 已存在文件，检查文件是否重复
     * @param docId
     * @param fileFullName
     * @return
     */
    public boolean isUniqueFileNameWithExists(Integer docId, FileType fileType, String fileFullName) {
        Integer parentId = findById(docId).getParentId();

        List<Document> siblingsList = findSubDocument(parentId);

        for(Document document : siblingsList) {
            if(document.getFileType() == fileType && docId != document.getId() && document.getFullName().equals(fileFullName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据文件名查找目录下的文件
     * @param parentId
     * @param fileType
     * @param fileFullName
     * @return
     */
    public Document findByNameFromParent(Integer parentId, FileType fileType, String fileFullName) {
        Document document = new Document();
        document.setFullName(fileFullName);
        document.setParentId(parentId);
        document.setFileType(fileType);
        document.setDelFlag(DataEntity.DEL_FLAG_NORMAL);

        Example<Document> example = Example.of(document);
        return documentRepository.findOne(example).get();
    }


    /**
     * 获取目录下的唯一名称
     * @return
     */
    public String getUniqueName(Integer parentId, FileType fileType, String fileFullName) {
        List<Document> siblingsList = findSubDocument(parentId);

        List<String> names = new ArrayList<>(siblingsList.size());

        for(Document document : siblingsList) {
            if (fileType == document.getFileType())
                names.add(document.getFullName());
        }

        String name = StringUtils.stripFilenameExtension(fileFullName);
        String ext = StringUtils.getFilenameExtension(fileFullName);

        int sno = 1;
        while(names.contains(fileFullName)) {
            fileFullName = name + "("+sno+")";
            if (!StringUtils.isEmpty(ext)) {
                fileFullName =  fileFullName + "." + ext;
            }
            sno++;
        }

        return fileFullName;
    }

    /**
     * 查找所有子文件（夹）
     * @return
     */
    public List<Document> findSubDocument(Integer parentId) {
        Document document = new Document();
        document.setParentId(parentId);
        document.setDelFlag(DataEntity.DEL_FLAG_NORMAL);

        Example example = Example.of(document);
        return documentRepository.findAll(example);
    }

    public void download(HttpServletResponse response, HttpServletRequest request, Integer id) throws IOException {
        Document doc = findById(id);
        if (doc.getFileType() == FileType.FOLDER)
            throw new RuntimeException("文件夹不能下载！");

        OutputStream os = getResponseOutputStream(response, request, doc.getFullName());
        File file = new File(doc.getFileAbsolutePath());
        IOUtils.write(FileUtils.readFileToByteArray(file), os);
        os.close();
    }

    private static OutputStream getResponseOutputStream(HttpServletResponse response, HttpServletRequest request, String fileName) throws IOException {
        String _fileName = fileName.replaceAll("[\\/:*?\"<>[|]]", "");

        String browserType = request.getHeader("User-Agent").toLowerCase();

        if(browserType.indexOf("firefox") > -1) { //FF
            _fileName = "=?"+ StandardCharsets.UTF_8+"?B?"+(new String(Base64.encodeBase64(_fileName.getBytes(StandardCharsets.UTF_8))))+"?=";
        } else {
            if(fileName.matches(".*[^\\x00-\\xff]+.*")) {
                if(request.getHeader("User-Agent").toLowerCase().indexOf("msie") > -1) { //IE
                    _fileName = java.net.URLEncoder.encode(_fileName,StandardCharsets.UTF_8.name());
                } else  { //其他
                    _fileName = new String(_fileName.getBytes(StandardCharsets.UTF_8), "ISO-8859-1");
                }
            }
        }

        response.reset();// 清空输出流
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());


        response.setHeader("Content-disposition", "attachment; filename="+_fileName+"");// 设定输出文件头
        response.setContentType("application/vnd.ms-excel;charset="+StandardCharsets.UTF_8+"");// 定义输出类型
        OutputStream os = response.getOutputStream(); // 取得输出流
        return os;
    }

    /**
     * 新建文件夹
     * @param fileFullName
     * @param parentId
     * @param documentCategory
     * @param id
     */
    public Integer mkdir(String fileFullName, Integer parentId, DocumentCategory documentCategory, Integer id) {
        //检查文件名是否重复
        if (!isUniqueFileNameWithNew(parentId, FileType.FOLDER, fileFullName)) {
            throw new OAException(ResultCode.FILE_NAME_DUPLICATE_ERROR);
        }

        Document document = new Document();
        document.setCategory(documentCategory);
        document.setCategoryId(id);
        document.setFileType(FileType.FOLDER);
        document.setFullName(fileFullName);
        document.setParentId(parentId);
        document.setInherit(true);// 初始化启用继承
        save(document);
        return document.getId();
    }

    public void rename(Integer id, String name) {
        Document document = findById(id);
        //检查文件名是否重复
        if (!isUniqueFileNameWithExists(id, document.getFileType(), name)) {
            throw new OAException(ResultCode.FILE_NAME_DUPLICATE_ERROR);
        }


        document.setFullName(name);
        save(document);
    }
}
