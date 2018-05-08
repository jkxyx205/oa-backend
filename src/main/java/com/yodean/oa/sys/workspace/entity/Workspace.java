package com.yodean.oa.sys.workspace.entity;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.Category;
import com.yodean.oa.sys.workspace.enums.CategoryStatus;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by rick on 3/27/18.
 */
@Entity
@Table(name = "sys_workspace",
        uniqueConstraints = {@UniqueConstraint(columnNames={"category", "category_id", "authority_type", "authority_id"})})
@DynamicUpdate
@DynamicInsert
public class Workspace extends DataEntity {
    /***s
     * 类型
     */
    @Enumerated(EnumType.STRING)
    private Category category;

    /***
     * 具体实例
     */
    @Column(name = "category_id")
    private Integer categoryId;

    /***
     *实例的本地化状态
     */
    @Enumerated(EnumType.STRING)
    private CategoryStatus categoryStatus;

    /***
     * 跟进
     */
    @Column(name = "follow",length = 1)
    private Boolean follow;

    /***
     * 阅读状态
     */
    @Column(length = 1)
    private Boolean readed;


    /**
     * 授权对象类型
     */
    @Column(name="authority_type")
    private AuthorityType authorityType;

    public static enum AuthorityType {
        GROUP("组"), USER("用户");
        private String description;

        AuthorityType(String description) {
            this.description = description;
        }
    }

    /***
     * 授权对象ID
     */
    @Column(name = "authority_id")
    private Integer authorityId;

    @Transient
    private String authorityName;

    /***
     * 授权对象 参与姿势
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    public enum UserType {
        MUST,OPTIONAL
    }

    @PrePersist
    private void PrePersist() {
        setCategoryStatus(CategoryStatus.INBOX);
        setFollow(false);
        setReaded(false);
        setUserType(UserType.MUST);
    }

//    @PreUpdate
//    private void PreUpdate() {
//        setCategoryStatus(CategoryStatus.INBOX);
//        setReaded(false);
//    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public CategoryStatus getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(CategoryStatus categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public Boolean getFollow() {
        return follow;
    }

    public void setFollow(Boolean follow) {
        this.follow = follow;
    }

    public Boolean getReaded() {
        return readed;
    }

    public void setReaded(Boolean readed) {
        this.readed = readed;
    }

    public AuthorityType getAuthorityType() {
        return authorityType;
    }

    public void setAuthorityType(AuthorityType authorityType) {
        this.authorityType = authorityType;
    }

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }
}
