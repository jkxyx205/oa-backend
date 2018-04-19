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
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by rick on 4/17/18.
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
     * @param documentName 文件夹名称
     */
    @Transactional
    public Integer addFolder(Integer parentId, String documentName) {
        //创建文件夹
        Integer docId = documentService.mkdir(parentId, documentName, DocumentCategory.DISK_COMPANY);

        //添加文件夹继承权限
        addAuthority(docId);

        return docId;
    }

    /**
     *
     * 设置文件夹继承权限
     * @param docId
     */
    @Transactional
    private void addAuthority(Integer docId) {
        //获取父权限
        List<Authority> authorityList = getInherit(docId);

        AuthorityDto authorityDto = new AuthorityDto();
        authorityDto.setInherit(true);
        authorityDto.setDocumentId(docId);
        authorityDto.setAuthorityList(authorityList);


        addAuthority(authorityDto);

        //修改权限的继承属性
        String sql = "UPDATE sys_document_auth SET inherit = 1 WHERE document_id = ?";
        jdbcTemplate.update(sql, docId);

    }

    /**
     * 清空权限列表
     */
    private void cleanAuthority(Integer docId) {
        String sql = "DELETE FROM sys_document_auth WHERE document_id = ?";
        jdbcTemplate.update(sql, docId);
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
        cleanAuthority(docId);

        //继承权限
        addAuthority(docId);

        //加载非继承权限
        AuthorityDto authorityDto = new AuthorityDto();
        authorityDto.setDocumentId(docId);
        authorityDto.setInherit(true);

        authorityDto.setAuthorityList(authorityList);
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

        //清空所有权限
        cleanAuthority(docId);

        authorityRepository.saveAll(uniqueAuthorityList);
    }


    @Transactional
    public void addAuthority(AuthorityDto authorityDto) {
        List<Authority> addAuthority = new ArrayList<>(); //新增权限
        List<Authority> delAuthority = new ArrayList<>(); //删除权限

        addAuthority(authorityDto, true, addAuthority, delAuthority);

        authorityRepository.saveAll(addAuthority);
        authorityRepository.deleteAll(delAuthority);
    }

    /**
     *
     * 修改文件夹权限
     * @param authorityDto 当前目录权限信息
     * @param curFolder true 当前目录 false 子目录
     */

    private void addAuthority(AuthorityDto authorityDto, boolean curFolder, List<Authority> addAuthority , List<Authority> delAuthority ) {
        Integer docId = authorityDto.getDocumentId();
        Document folder = documentService.findById(docId);

        if (!curFolder && !folder.getInherit()) //子目录取消继承，那么授权终止
            return;

        List<Authority> newAuthorityList= authorityDto.getAuthorityList();   //新的所有权限
        List<Authority> authorityList = findAuthorityByDocumentId(docId);//获取目录的权限


        //新增的权限
        newAuthorityList.forEach(_authority -> {

            Authority authority = SerializationUtils.clone(_authority);
            authority.setId(null);
            authority.setDocumentId(docId);

            if (curFolder) { //当前目录
                authority.setInherit(true);
                if (!authorityList.contains(authority)) { //如果没有被继承
                    authority.setInherit(false);
                    if (!authorityList.contains(authority)) { //如果没有自定义，则纳入自定义权限
                        addAuthority.add(authority);
                    }
                }
            } else { //子目录继承关系
                authority.setInherit(true);
                if (!authorityList.contains(authority)) { //判断是否是新增的权限
                    addAuthority.add(authority);
                }
            }

        });

        //移除的权限
        authorityList.forEach(_authority -> { //判断是否是删除的权限"
            Authority authority = SerializationUtils.clone(_authority);
            authority.setDocumentId(docId);
            boolean inherit = authority.getInherit();//是否继承
            authority.setInherit(null); //忽略继承查询


            if (curFolder) { //本目录
                if (!newAuthorityList.contains(authority)) { //如果被删除了
                    if (!inherit) //如果是自定义的权限
                        delAuthority.add(authority);
                }

            } else { // 子目录
                if (!newAuthorityList.contains(authority)) { //如果被删除了
                    if (inherit) //如果是继承的
                        delAuthority.add(authority);
                }
            }

        });

        //设置子目录的继承权限
        List<Document> documentList = documentService.findSubDocument(docId);

        documentList.forEach(document -> {
            if (document.getFileType() == FileType.FOLDER) {
                authorityDto.setDocumentId(document.getId());

                addAuthority(authorityDto, false, addAuthority, delAuthority);
            }
        });
    }


    /**
     * 获取文件夹的继承权限（父文件夹权限）
     * @param docId
     */
    private List<Authority> getInherit(Integer docId) {
        Document document = documentService.findById(docId);
        Integer parentId = document.getParentId();
        if (Objects.isNull(parentId)) {
            return Collections.emptyList();
        }

        List<Authority> authorityList = findAuthorityByDocumentId(parentId);
        return authorityList;

    }

    /***
     * 获取文件夹的权限体系
     * @param docId
     * @return
     */
    private List<Authority> findAuthorityByDocumentId(Integer docId) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", docId);

        return query("SELECT  id, authority_type authorityType, authority_id authorityId, document_id documentId, authority_area authorityArea, inherit FROM sys_document_auth WHERE document_id = :id",
                params, Authority.class);

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