package com.yodean.oa.sys.document.entity;

import com.yodean.oa.common.entity.DataEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by rick on 4/17/18.
 * 文件权限表
 */
@Entity
@Table(name = "sys_document_auth",
        uniqueConstraints = {@UniqueConstraint(columnNames={"authority_type", "authority_id", "document_id", "authority_area", "inherit"})})
public class Authority extends DataEntity {
    /**
     * 用户id 或者 组id
     */
    @Column(name = "authority_id")
    private Integer authorityId;

    /**
     * 文件id
     */
    @Column(name = "document_id")
    private Integer documentId;

    /**
     * 是否采用继承
     */
    private Boolean inherit;

    /**
     * 权限范围
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "authority_area")
    private AuthorityArea authorityArea;

    /**
     * 授权对象类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "authority_type")
    private AuthorityType authorityType;

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

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

    public AuthorityArea getAuthorityArea() {
        return authorityArea;
    }

    public void setAuthorityArea(AuthorityArea authorityArea) {
        this.authorityArea = authorityArea;
    }

    public AuthorityType getAuthorityType() {
        return authorityType;
    }

    public void setAuthorityType(AuthorityType authorityType) {
        this.authorityType = authorityType;
    }

    public static enum AuthorityType {
        GROUP("组"), USER("用户");
        private String description;

        AuthorityType(String description) {
            this.description = description;
        }
    }

    public static enum AuthorityArea {
//        READ("查看"), LIST("列表"), UPLOAD("上传"), DELETE("删除"), DOWNLOAD("下载"), EDIT("编辑"), PATH("路径");
        READ("查看"), UPLOAD("上传"), DELETE("删除"), DOWNLOAD("下载"), AUTH("授权"), PATH("路径");
        private String description;

        AuthorityArea(String description) {
            this.description = description;
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;


        if (!(obj instanceof Authority) || obj == null ) return false;

        Authority that = (Authority) obj;

        return new EqualsBuilder()
                .append(this.authorityType, that.authorityType)
                .append(this.authorityId, that.authorityId)
                .append(this.authorityArea, that.authorityArea)
                .append(this.inherit, that.inherit)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityType, authorityId, authorityArea, inherit);
    }

}