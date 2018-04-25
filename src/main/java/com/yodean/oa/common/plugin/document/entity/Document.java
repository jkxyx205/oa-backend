package com.yodean.oa.common.plugin.document.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yodean.oa.common.config.Global;
import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.DocumentCategory;
import com.yodean.oa.common.plugin.document.enums.FileType;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.File;

/**
 * Created by rick on 2018/3/22.
 */
@Entity
@Table(name = "sys_document",
        uniqueConstraints = {@UniqueConstraint(columnNames={"category", "category_id", "type", "del_flag", "name", "ext"})})
@DynamicUpdate
public class Document extends DataEntity {

    private String name;

    private String path;

    private String ext;

    private Long size;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "parent_id")
    private Integer parentId;

    /**
     *文件类型
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private FileType fileType;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private DocumentCategory category;

    @Column(name = "category_id")
    private Integer categoryId;


    /**
     * 是否启用权限继承
     */
    private Boolean inherit;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Boolean getInherit() {
        return inherit;
    }

    public void setInherit(Boolean inherit) {
        this.inherit = inherit;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public DocumentCategory getCategory() {
        return category;
    }

    public void setCategory(DocumentCategory category) {
        this.category = category;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setFullName(String fullName) {
        String fileName = StringUtils.stripFilenameExtension(fullName);
        String fileExt = StringUtils.getFilenameExtension(fullName);
        setName(fileName);
        setExt(fileExt);
    }

    public String getFullName() {
        if (StringUtils.isEmpty(this.getExt()))
            return this.name;

        return this.name + "." + this.getExt();
    }

    /***
     * 获取原文件绝对路径
     * @return
     */
    @JsonIgnore
    public String getFileAbsolutePath() {
        if (FileType.FOLDER == this.fileType) return null;
        return Global.DOCUMENT + File.separator + path;
    }


    /***
     * 获取原文件网络地址
     * @return
     */
    public String getUrlPath() {
        if (FileType.FOLDER == this.fileType) return null;
        return Global.CDN + "/" + path + "." + ext;
    }
}
