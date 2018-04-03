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
        uniqueConstraints = {@UniqueConstraint(columnNames={"category", "category_id", "user_id", "del_flag"})})

@DynamicUpdate
@DynamicInsert
public class Workspace extends DataEntity {

    public enum UserType {
        MUST,OPTIONAL
    }

    /***
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
     * 状态
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

    /***
     * 参与者
     */
    @Column(name = "user_id")
    private Integer userId;

    /***
     * 参与者类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
