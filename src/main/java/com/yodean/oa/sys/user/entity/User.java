package com.yodean.oa.sys.user.entity;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.task.entity.Task;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by rick on 2018/3/15.
 */
@Entity
@Table(name = "sys_user")
public class User extends DataEntity {

    @Length(max=20, message = "字符串不能超过20")
    @NotNull(message = "姓名不能为空")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
