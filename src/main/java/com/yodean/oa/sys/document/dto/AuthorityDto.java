package com.yodean.oa.sys.document.dto;

import com.yodean.oa.sys.document.entity.Authority;

import java.util.List;

/**
 * Created by rick on 4/17/18.
 * UI传递的权限信息
 */
public class AuthorityDto {

    /**
     * 文件夹id
     */
    private Integer documentId;

    /**
     * 是否使用继承
     */
    private Boolean inherit;


    /**
     * 权限体系
     */
    private List<Authority> authorityList;


    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public Boolean getInherit() {
        return inherit;
    }

    public void setInherit(Boolean inherit) {
        this.inherit = inherit;
    }

    public List<Authority> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<Authority> authorityList) {
        this.authorityList = authorityList;
    }
}
