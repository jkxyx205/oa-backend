package com.yodean.oa.sys.org.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.sys.user.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by rick on 2018/3/23.
 */
@Entity
@Table(name = "sys_org")
public class Organization extends DataEntity {

    @Column(nullable = false)
    @NotNull(message = "parentId不能为空")
    private Integer parentId;

    @Column(nullable = false, length = 32)
    @NotBlank
    private String name;

    @Column(length = 32)
    private String manager;

    @ManyToMany(mappedBy = "organizations")
    @JsonIgnore
    private Set<User> users;

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

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
