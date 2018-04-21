package com.yodean.oa.sys.document.service;

import com.yodean.oa.common.enums.DocumentCategory;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.plugin.document.enums.FileType;
import com.yodean.oa.common.plugin.document.service.DocumentService;
import com.yodean.oa.common.service.SharpService;
import com.yodean.oa.sys.document.dao.AuthorityRepository;
import com.yodean.oa.sys.document.dto.AuthorityDto;
import com.yodean.oa.sys.document.entity.Authority;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

/**
 * Created by rick on 4/20/18.
 */
@Service
public class AuthorityService extends SharpService {

    @Resource
    private AuthorityRepository authorityRepository;

    @Resource
    private DocumentService documentService;

    @Resource
    private JdbcTemplate jdbcTemplate;

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
        Set<Authority> allAddAuthority = new HashSet<>();
        Set<Authority> allRemoveAuthority = new HashSet<>();
        addAuthority(authorityDto, true, allAddAuthority, allRemoveAuthority);
        authorityRepository.saveAll(allAddAuthority);  //添加权限
        authorityRepository.deleteAll(allRemoveAuthority); //删除权限

    }

    /**
     *
     * @param authorityDto
     */
    private void addAuthority(AuthorityDto authorityDto, Boolean isCurrent, Set<Authority> allAddAuthority,  Set<Authority> allRemoveAuthority ) {
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

        //根据1和2获取非继承权限 TODO set contains在某些清空下居然有问题...改用list
        List<Authority> unInheritAuthority = new ArrayList<>();


        if (isCurrent) {
            paramsAuthority.forEach(authority -> {
                Boolean paramInherit = authority.getInherit();
                authority.setInherit(true);
                if (!parentAuthority.contains(authority)) { //不是继承:在继承之中没有找到

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
                if (!currentAuthority.contains(authority)) { //添加非继承权限
                    authority.setDocumentId(docId);
                    allAddAuthority.add(authority);
                }
            });
        }

        parentAuthority.forEach(authority -> {
            if (!currentAuthority.contains(authority)) { //添加继承权限
                authority.setDocumentId(docId);
                allAddAuthority.add(authority);
            }
        });

        //--
        if (isCurrent) {
            currentAuthority.forEach(authority -> {
                if (!authority.getInherit() && !unInheritAuthority.contains(authority)) { //删除非继承权限
                    allRemoveAuthority.add(authority);
                }
            });
        }

        currentAuthority.forEach(authority -> {
            if (authority.getInherit() && !parentAuthority.contains(authority)) { //删除继承权限
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

        return query("SELECT  id, authority_type authorityType, authority_id authorityId, document_id documentId, authority_area authorityArea, inherit, create_by createBy, update_by updateBy, create_date createDate, update_date updateDate,del_flag delFlag FROM sys_document_auth WHERE document_id = :id",
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
     * 重命名
     * @param id
     * @param name
     */
    public void rename(Integer id, String name) {
        Document document = documentService.findById(id);
        document.setName(name);
        documentService.save(document);
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
     * 下载
     * @param id
     */
    public void download(HttpServletRequest request, HttpServletResponse response, Integer id) throws IOException {
        documentService.download(response, request, id);
    }


    /**
     * 获取某个文件夹的，用户所有权限集合
     * @param docId
     * @param userId
     * @return
     */
    public Set<Authority.AuthorityType> findAuthorities(Integer docId, Integer userId) {
        return null;
    }

}
