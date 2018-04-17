package com.yodean.oa.sys.document.service;

import com.yodean.oa.common.enums.DocumentCategory;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.plugin.document.enums.FileType;
import com.yodean.oa.common.plugin.document.service.DocumentService;
import com.yodean.oa.sys.document.dao.AuthorityRepository;
import com.yodean.oa.sys.document.dto.AuthorityDto;
import com.yodean.oa.sys.document.entity.Authority;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by rick on 4/17/18.
 */
@Service
public class AuthorityService {

    @Resource
    private AuthorityRepository authorityRepository;

    @Resource
    private DocumentService documentService;

    /**
     * 添加文件夹
     * @param documentName 文件夹名称
     */
    @Transactional
    public Integer addFolder(Integer parentId, String documentName) {
        //创建文件夹
        Integer docId = documentService.mkdir(parentId, documentName, DocumentCategory.DISK_COMPANY);

        //添加文件夹权限
        addAuthority(docId);

        return docId;
    }

    /**
     *
     * 设置文件夹权限
     * @param docId
     */
    @Transactional
    private void addAuthority(Integer docId) {
        //获取父权限
        List<Authority> authorityList = getInherit(docId);
        authorityList.forEach(authority-> {
            authority.setInherit(true); //设置为继承
        });

        AuthorityDto authorityDto = new AuthorityDto();
        authorityDto.setInherit(true);
        authorityDto.setAuthorityList(authorityList);
        authorityDto.setDocumentId(docId);

        addAuthority(authorityDto);

    }

    @Transactional
    public void addAuthority(AuthorityDto authorityDto) {
        List<Authority> addAuthority = new ArrayList<>();
        List<Authority> delAuthority = new ArrayList<>();

        addAuthority(authorityDto, true, addAuthority, delAuthority);

        authorityRepository.saveAll(addAuthority);
        authorityRepository.deleteAll(delAuthority);
    }

    /**
     *
     * 修改文件夹权限
     * @param authorityDto 当前目录权限信息
     * @param curFolder true 当前目录 false子目录
     */

    private void addAuthority(AuthorityDto authorityDto, boolean curFolder, List<Authority> addAuthority , List<Authority> delAuthority ) {
        Integer docId = authorityDto.getDocumentId();
        List<Authority> newAuthority= authorityDto.getAuthorityList();

        //获取当前的权限
        List<Authority> authorityList = findAuthorityByDocumentId(docId);

        newAuthority.forEach(_authority -> {

            Authority authority = SerializationUtils.clone(_authority);
            authority.setDocumentId(docId);

            if (curFolder)
                authority.setInherit(false);//新增非继承权限
            else
                authority.setInherit(true);//新增继承权限

            if (!authorityList.contains(authority)) { //判断是否是新增的权限
                addAuthority.add(authority);
            }
        });

        authorityList.forEach(_authority -> { //判断是否是删除的权限
            Authority authority = SerializationUtils.clone(_authority);
            authority.setDocumentId(docId);
            Boolean inherit = authority.isInherit(); //是否继承

            authority.setInherit(null); //忽略继承关系

            if (!newAuthority.contains(authority)) { //删除的权限
                if (curFolder && !inherit) //本目录：删除非继承
                    delAuthority.add(authority);
                else if(!curFolder && inherit) //子目录：删除继承的
                    delAuthority.add(authority);

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
        Authority authority = new Authority();
        authority.setDocumentId(docId);

        Example example = Example.of(authority);
        return authorityRepository.findAll(example);

    }


    /**
     * 获取某个文件夹的，用户所有权限
     * @param docId
     * @param userId
     * @return
     */
    public Set<Authority.AuthorityType> findAuthorities(Integer docId, Integer userId) {

        return null;
    }

}