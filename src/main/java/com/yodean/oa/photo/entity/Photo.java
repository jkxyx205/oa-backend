package com.yodean.oa.photo.entity;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.plugin.document.entity.Document;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created by rick on 4/27/18.
 */
@Entity
@Table(name = "r_photo")
public class Photo extends DataEntity {

    private String description;

    @Transient
    private List<Document> documentList;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
    }
}
