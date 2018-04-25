package com.yodean.oa.sys.document.service;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.DocumentCategory;
import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.plugin.document.enums.FileType;
import com.yodean.oa.common.plugin.document.service.DocumentService;
import com.yodean.oa.common.service.SharpService;
import com.yodean.oa.sys.document.dao.AuthorityRepository;
import com.yodean.oa.sys.document.dto.AuthorityDto;
import com.yodean.oa.sys.document.dto.DocumentDto;
import com.yodean.oa.sys.document.entity.Authority;
import com.yodean.oa.sys.util.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.print.Doc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

/**
 * Created by rick on 4/20/18.
 */
@Service
public class AuthorityService  {

    @Resource
    private AuthorityRepository authorityRepository;

    @Resource
    private DocumentService documentService;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private SharpService sharpService;

    /**
     * 文件上传
     * @return
     */
    public List<Document> upload(List<MultipartFile> files, String folderPath, Integer parentId, DocumentCategory category, Integer categoryId) {

        List<Document> documents = new ArrayList<>(files.size());
        for (MultipartFile file : files) {
            try {
                documents.add(documentService.upload(file, folderPath, parentId, category, categoryId));
            } catch (IOException e) {
                throw new OAException(ResultCode.FILE_UPLOAD_ERROR);
            }
        }

        return documents;
    }

    /**
     * 添加文件夹
     * @param name 文件夹名称
     */
    @Transactional
    public Integer mkdir(Integer parentId, String name, DocumentCategory documentCategory) {
        //创建文件夹
        Integer docId = documentService.mkdir(name, parentId, documentCategory, 0);

        //添加文件夹继承权限
        addAuthority(docId);

        return docId;
    }

    /**
     * 添加继承权限
     * @param docId
     */
    private void addAuthority(Integer docId) {
        AuthorityDto authorityDto = new AuthorityDto();
        authorityDto.setInherit(true);
        authorityDto.setDocumentId(docId);
        authorityDto.setAuthorityList(Collections.emptySet());

        addAuthority(authorityDto);
    }

    /**
     * 添加权限
     * @param authorityDto
     */
    @Transactional
    public void addAuthority(AuthorityDto authorityDto) {
        List<Authority> allAddAuthority = new ArrayList<>();
        List<Authority> allRemoveAuthority = new ArrayList<>();
        addAuthority(authorityDto, true, allAddAuthority, allRemoveAuthority);

        authorityRepository.saveAll(allAddAuthority);  //添加权限
        authorityRepository.deleteAll(allRemoveAuthority); //删除权限

        //设置"路径"权限
        addPathAuthority(authorityDto);
    }

    /**
     * 添加路径的特殊权限
     * @param authorityDto
     */
    private void addPathAuthority(AuthorityDto authorityDto) {
//        List<Document> parents = documentService.findParentsDocument(authorityDto.getDocumentId());

        List<Document> parents = documentService.findDocumentPath(authorityDto.getDocumentId());

        if (CollectionUtils.isEmpty(parents)) return;

        Set<Authority> authoritySet = authorityDto.getAuthorityList();

        List<Authority> pathAuthoritySet = new ArrayList<>(parents.size() * authoritySet.size());

        //删除PATH权限
        String sql = "DELETE FROM sys_document_auth WHERE document_id = ? AND authority_area = 'PATH'";
        List<Object[]> paramsList = new ArrayList<>(parents.size());
        parents.forEach(document -> {
            Object[] params = new Object[] {document.getId()};
            paramsList.add(params);
        });
        jdbcTemplate.batchUpdate(sql, paramsList);


        //添加PATH权限
        if (CollectionUtils.isEmpty(authoritySet)) return;

        authoritySet.forEach(authority -> {
            parents.forEach(document -> {
                Authority _authority = SerializationUtils.clone(authority);
                _authority.setId(null);
                _authority.setAuthorityArea(Authority.AuthorityArea.PATH);
                _authority.setDocumentId(document.getId());
                _authority.setInherit(true);
                pathAuthoritySet.add(_authority);
            });

        });

        authorityRepository.saveAll(pathAuthoritySet);
    }

    /**
     *
     * @param authorityDto
     */
    private void addAuthority(AuthorityDto authorityDto, Boolean isCurrent, List<Authority> allAddAuthority,  List<Authority> allRemoveAuthority ) {
        Integer docId = authorityDto.getDocumentId();
        Document curDocument = documentService.findById(docId);

        if (!isCurrent && Boolean.FALSE == curDocument.getInherit()) //子目录取消继承，那么授权终止
            return;


        //1. 继承权限（继承）
        List<Authority> parentAuthority;
        if (isCurrent) {

            //取消继承
            if (curDocument.getInherit() == Boolean.FALSE) {
                parentAuthority = new ArrayList<>();
            } else { //使用继承
                parentAuthority = getInherit(docId);
                parentAuthority.forEach(authority -> {
                    authority.setId(null);
                    authority.setInherit(true);
                });
            }

        } else {
            parentAuthority = new ArrayList<>(authorityDto.getAuthorityList());
        }

        //2. 参数权限（继承 或 非继承）
        Set<Authority>  paramsAuthority =authorityDto.getAuthorityList();

        //3. DB权限 （继承 或 非继承）
        List<Authority> currentAuthority = findAuthorityByDocumentId(docId);

        //根据1和2获取非继承权限 TODO set contains在某些情况下居然有问题...改用list
        List<Authority> unInheritAuthority = new ArrayList<>();


        if (isCurrent) {
            paramsAuthority.forEach(authority -> {
                Boolean paramInherit = authority.getInherit();
                authority.setInherit(true);
                if (!parentAuthority.contains(authority) && authority.getAuthorityArea() != Authority.AuthorityArea.PATH) { //不是继承:在继承之中没有找到

                    if (paramInherit == Boolean.TRUE) { //可能是移动或复制带过来的继承权限，不能充当非继承权限
                        allRemoveAuthority.add(authority);
                    } else {
                        authority.setInherit(false);
                        unInheritAuthority.add(authority);
                    }
                }

            });
        }

        //--
        if (isCurrent) {
            unInheritAuthority.forEach(authority -> {
                if (!currentAuthority.contains(authority) && authority.getAuthorityArea() != Authority.AuthorityArea.PATH) { //添加非继承权限
                    authority.setDocumentId(docId);
                    allAddAuthority.add(authority);
                }
            });
        }

        parentAuthority.forEach(authority -> {
            if (!currentAuthority.contains(authority) && authority.getAuthorityArea() != Authority.AuthorityArea.PATH) { //添加继承权限
                authority.setDocumentId(docId);
                allAddAuthority.add(authority);
            }
        });

        //--
        if (isCurrent) {
            currentAuthority.forEach(authority -> {
                if (!authority.getInherit() && !unInheritAuthority.contains(authority) && authority.getAuthorityArea() != Authority.AuthorityArea.PATH) { //删除非继承权限
                    allRemoveAuthority.add(authority);
                }
            });
        }

        currentAuthority.forEach(authority -> {
            if (authority.getInherit() && !parentAuthority.contains(authority) && authority.getAuthorityArea() != Authority.AuthorityArea.PATH) { //删除继承权限
                allRemoveAuthority.add(authority);
            }
        });

        //所有继承权限
        if (isCurrent) {
            parentAuthority.addAll(paramsAuthority);
        } else {
            parentAuthority.addAll(currentAuthority);
        }

        //设置子目录权限
        List<Document> documentList = documentService.findSubDocument(docId);

        documentList.forEach(document -> {

            Set<Authority> inheritAuthorityList = new HashSet<>(parentAuthority.size());

            AuthorityDto subAuthorityDto = new AuthorityDto();
            subAuthorityDto.setDocumentId(document.getId());
            subAuthorityDto.setInherit(document.getInherit());

            parentAuthority.forEach(authority -> {
                Authority _authority = SerializationUtils.clone(authority);
                _authority.setId(null);
                _authority.setInherit(true);
                inheritAuthorityList.add(_authority);
            });
            subAuthorityDto.setAuthorityList(inheritAuthorityList);

            addAuthority(subAuthorityDto, false,  allAddAuthority, allRemoveAuthority);
        });
    }

    /**
     * 启用继承权限
     */
    @Transactional
    public void startAuthInherit(Integer docId) {
        Document document = documentService.findById(docId);
        document.setInherit(true);
        documentService.save(document);

        //当前权限集合
        List<Authority> authorityList = findAuthorityByDocumentId(docId);

        //清空所有权限
//        cleanAuthority(docId);


        AuthorityDto authorityDto = new AuthorityDto();
        authorityDto.setDocumentId(docId);
        authorityDto.setInherit(true);
        authorityDto.setAuthorityList(new HashSet<>(authorityList));

        addAuthority(authorityDto);
    }

    /**
     * 停止继承权限
     */
    @Transactional
    public void stopAuthInherit(Integer docId) {
        Document document = documentService.findById(docId);
        document.setInherit(false);
        documentService.save(document);

        //
        List<Authority> authorityList = findAuthorityByDocumentId(docId);

        //剔除重复
        Set<Authority> uniqueAuthorityList = new HashSet<>(authorityList.size());

        authorityList.forEach(authority -> {
            Authority _authority = SerializationUtils.clone(authority);
            _authority.setId(null);
            _authority.setInherit(false);
            uniqueAuthorityList.add(_authority);
        });

        //清空所有权限(剔除重复)
        cleanAuthority(docId);

        authorityRepository.saveAll(uniqueAuthorityList);

    }

    /**
     * 清空权限列表
     */
    private void cleanAuthority(Integer docId) {
        String sql = "DELETE FROM sys_document_auth WHERE document_id = ?";
        jdbcTemplate.update(sql, docId);
    }


    /**
     * 获取文件夹继承权限（父文件权限）
     * @param docId
     */
    private List<Authority> getInherit(Integer docId) {
        Document document = documentService.findById(docId);
        Integer parentId = document.getParentId();
        if (Objects.isNull(parentId)) {
            return new ArrayList<>();
        }

        List<Authority> authorityList = findAuthorityByDocumentId(parentId);
        return authorityList;

    }

    /***
     * 获取文件的权限
     * @param docId
     * @return
     */
    private List<Authority> findAuthorityByDocumentId(Integer docId) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", docId);

        return sharpService.query("SELECT  id, authority_type authorityType, authority_id authorityId, document_id documentId, authority_area authorityArea, inherit, create_by createBy, update_by updateBy, create_date createDate, update_date updateDate,del_flag delFlag FROM sys_document_auth WHERE document_id = :id",
                params, Authority.class);

    }

    /**
     * 逻辑删除文件
     * @param id
     */
    public void delete(Integer id) {
        documentService.delete(id);
    }

    /**
     * 逻辑彻底删除
     * @param id
     */
    public void clean(Integer id) {
        documentService.clean(id);
    }

    /**
     * 重命名
     * @param id
     * @param name
     */
    public void rename(Integer id, String name) {
        documentService.rename(id, name);
    }

    /**
     * 文件移动
     * @param id
     * @param parentId
     */
    public void move(Integer id, Integer parentId) {
        Document document = documentService.findById(id);
        document.setParentId(parentId);
        documentService.save(document);

        //权限重置
        startAuthInherit(id); //重置继承权限
        if (!document.getInherit()) {//停止继承
            stopAuthInherit(id);
        }
    }

    /**
     * 文件拷贝
     * @param id
     * @param parentId
     */
    public void copy(Integer id, Integer parentId) {
        Document document = documentService.findById(id);

        Integer nid = recursionDoc(document, parentId);
        //权限重置

        startAuthInherit(nid); //重置继承权限
        if (!document.getInherit()) {//停止继承
            stopAuthInherit(id);
        }
    }

    private int recursionDoc(Document document, Integer parentId) {
        Document _document = SerializationUtils.clone(document);
        _document.setId(null);
        _document.setParentId(parentId);

        documentService.save(_document);

        if (document.getFileType() == FileType.FILE) return _document.getId();

        documentService.findSubDocument(document.getId()).forEach(subDocument -> {
            recursionDoc(subDocument, _document.getId());
        });

        return _document.getId();
    }

    /**
     * 还原文件
     * @param id
     */
    public void putBack(Integer id) {
        //目录路径
        List<Document> path = documentService.findDocumentPath(id);

        Integer parentFlag = path.get(0).getParentId(); //当前目录的父目录ID

        for (Document curDoc : path) {

            curDoc.setParentId(parentFlag);

            if (DataEntity.DEL_FLAG_REMOVE.equals(curDoc.getDelFlag())) {//文件已经删除
                if (documentService.isUniqueFileNameWithNew(parentFlag, curDoc.getFileType(), curDoc.getFullName())) { //没有同名文件
                    if(id == curDoc.getId()) {//要恢复的目录
                        curDoc.setDelFlag(DataEntity.DEL_FLAG_NORMAL);
                        parentFlag = curDoc.getId();
                    } else {// 上层目录
                        parentFlag = documentService.mkdir(curDoc.getFullName(), parentFlag, curDoc.getCategory(), 0);
                    }

                } else { //有同名文件:获取同名的文件
                    parentFlag = documentService.findByNameFromParent(parentFlag, curDoc.getFileType(), curDoc.getFullName()).getId();

                    if(id == curDoc.getId()) {//要恢复的文件

                        if (curDoc.getFileType() == FileType.FOLDER) { //恢复文件夹

                            curDoc.setDelFlag(DataEntity.DEL_FLAG_CLEAN); //永久删除
                            List<Document> subList = documentService.findSubDocument(id);

                            for (Document document : subList) {
                                document.setParentId(parentFlag);
                            }

                            path.addAll(subList); //合并所有修改
                            break;
                        } else { //恢复文件
                            curDoc.setDelFlag(DataEntity.DEL_FLAG_NORMAL);
                            curDoc.setFullName(documentService.getUniqueName(curDoc.getParentId(), FileType.FILE, curDoc.getFullName()));
                        }
                    }

                }

            } else {
                parentFlag = curDoc.getId();
            }
        }

        documentService.save(path);
    }

    /**
     * 下载
     * @param ids
     */
    public void download(HttpServletRequest request, HttpServletResponse response, Integer ... ids) throws IOException {
        documentService.download(response, request, ids);
    }


    /**
     * 预览
     * @param id
     */
    public void view(HttpServletRequest request, HttpServletResponse response, Integer id) throws IOException {
        documentService.view(response, request, id);
    }

    /**
     * 获取文件的授权表
     * @param docId
     * @return
     */
    public AuthorityDto findAuthorityDtoList(Integer docId) {
        List<Authority> authorityList =  this.findAuthorityByDocumentId(docId);
        Document document = documentService.findById(docId);

        AuthorityDto authorityDto = new AuthorityDto();
        authorityDto.setDocumentId(docId);
        authorityDto.setAuthorityList(new LinkedHashSet<>(authorityList));
        authorityDto.setInherit(document.getInherit());
        return authorityDto;
    }

    /**
     * 获取用户文件夹下的所有文件列表
     * @param parentId
     * @param userId
     * @return
     */
    public List<DocumentDto> findAuthorityDtoList(Integer parentId, Integer userId) {

        String sql = sharpService.getSQL("com.yodean.oa.sys.document.dao.listDocument");
        Map<String, Object> params = new HashMap<>(3);
        params.put("parentId", parentId);
        params.put("userId", userId);
        params.put("orgIds", UserUtils.getUser().getOrgIdsAsString());

        List<Document> documentList = sharpService.query(sql, params, Document.class);

        List<DocumentDto> documentDtoList = new ArrayList<>(documentList.size());

        for (Document document : documentList) {
            DocumentDto documentDto = new DocumentDto();
            documentDto.setDocument(document);
            documentDtoList.add(documentDto);
        }

        return documentDtoList;

    }

}