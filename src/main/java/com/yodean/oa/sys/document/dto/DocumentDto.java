package com.yodean.oa.sys.document.dto;

import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.sys.document.entity.Authority;

import java.util.List;

/**
 * Created by rick on 4/25/18.
 */
public class DocumentDto {

    private Document document;

    private List<Authority.AuthorityArea> authorityAreaList;


    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public List<Authority.AuthorityArea> getAuthorityAreaList() {
        return authorityAreaList;
    }

    public void setAuthorityAreaList(List<Authority.AuthorityArea> authorityAreaList) {
        this.authorityAreaList = authorityAreaList;
    }
}
