package com.yodean.oa.sys.org.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.sys.user.entity.User;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * Created by rick on 2018/3/23.
 */
@Entity
@Table(name = "sys_org")
@DynamicUpdate
public class Organization extends DataEntity {

    private Integer parentId;

    @Column(nullable = false, length = 32)
    @NotBlank
    private String name;

    @Column(name = "manager_id")
    private Integer managerId;

    @ManyToMany(mappedBy = "organizations")
    @JsonIgnore
    private Set<User> users;

    @Transient
    private User manager;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public User getManager() {

        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }
}
