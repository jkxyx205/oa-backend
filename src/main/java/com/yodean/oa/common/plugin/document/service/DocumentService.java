package com.yodean.oa.common.plugin.document.service;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.Category;
import com.yodean.oa.common.enums.DocumentCategory;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.print.Doc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
     * @param name
     * @param parentId
     * @param documentCategory
     * @param id
     */
    public Integer mkdir(String name, Integer parentId, DocumentCategory documentCategory, Integer id) {
        Document document = new Document();
        document.setCategory(documentCategory);
        document.setCategoryId(id);
        document.setFileType(FileType.FOLDER);
        document.setName(name);
        document.setParentId(parentId);
        document.setInherit(true);// 初始化启用继承
        save(document);
        return document.getId();
    }
}
