package com.yodean.oa.sys.user.entity;

import com.yodean.oa.common.entity.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by rick on 2018/3/15.
 */
@Entity
@Table(name = "sys_user")
public class User extends DataEntity {

    @Length(max=5, message = "字符串不能超过5")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
