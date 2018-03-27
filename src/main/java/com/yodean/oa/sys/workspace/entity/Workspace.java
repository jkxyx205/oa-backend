package com.yodean.oa.sys.workspace.entity;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.CategoryEnum;
import com.yodean.oa.sys.workspace.enums.CategoryStatus;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by rick on 3/27/18.
 */
@Entity
@Table(name = "sys_workspace")
@DynamicUpdate
public class Workspace extends DataEntity {

    @Enumerated(EnumType.STRING)
    private CategoryEnum category;

    @Column(name = "category_id")
    private Integer categoryId;

    @Enumerated(EnumType.STRING)
    private CategoryStatus categoryStatus;

    /***
     * 跟进
     */
    @Column(name = "follow_up",length = 1)
    private Boolean followUp;

    /***
     * 阅读状态
     */
    @Column(length = 1)
    private Boolean unread;

    /***
     * 参与者
     */
    private Integer userId;

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
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

    public Boolean getFollowUp() {
        return followUp;
    }

    public void setFollowUp(Boolean followUp) {
        this.followUp = followUp;
    }

    public Boolean getUnread() {
        return unread;
    }

    public void setUnread(Boolean unread) {
        this.unread = unread;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
