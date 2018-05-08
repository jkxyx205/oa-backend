package com.yodean.oa.sys.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.sys.org.entity.Organization;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by rick on 2018/3/15.
 */
@Entity
@Table(name = "sys_user")
public class User extends DataEntity {

    @Length(max=20, message = "昵称不能超过20个字符串")
    @NotNull(message = "昵称不能为空")
    @Column(nullable = false, length = 20)
    private String nickName;

    @Length(max=20, message = "中文姓名不能超过20个字符串")
    @NotNull(message = "中文姓名不能为空")
    @Column(nullable = false, length = 20)
    private String chineseName;

    @Length(max=20, message = "英文姓名不能超过20个字符串")
    @Column(length = 20)
    private String englishName;

    @Column(length = 1)
    private Character sex;

    private String workNumber;

    @Column(length = 20)
    @Length(max=20, message = "手机号码不能超过20个字符串")
    private String tel;

    @Column(length = 20)
    @Length(max=20, message = "办公室电话不能超过20个字符串")
    private String officeCall;

    @Column(length = 20)
    @Length(max=20, message = "邮箱不能超过20个字符串")
    private String email;

    @Column(length = 20)
    @Length(max=20, message = "职位不能超过20个字符串")
    private String position;

    @Column(length = 20, nullable = false)
    @Length(min=6, message = "密码不能少于6个字符")
    private String password;



    @Transient
    private Integer[] orgIds;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "sys_user_org",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "org_id", referencedColumnName ="id")}
    )
    private Set<Organization> organizations;

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOfficeCall() {
        return officeCall;
    }

    public void setOfficeCall(String officeCall) {
        this.officeCall = officeCall;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Set<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = organizations;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer[] getOrgIds() {
        return orgIds;
    }

    public String getOrgIdsAsString() {
        return StringUtils.join(orgIds, ";");
    }

    public void setOrgIds(Integer[] orgIds) {
        this.orgIds = orgIds;
    }
}
