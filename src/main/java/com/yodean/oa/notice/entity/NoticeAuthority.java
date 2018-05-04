package com.yodean.oa.notice.entity;

import com.yodean.oa.common.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by rick on 5/4/18.
 */
@Entity
@Table(name = "t_notice_auth",
        uniqueConstraints = {@UniqueConstraint(columnNames={"notice_id", "authority_type", "authority_id"})})
public class NoticeAuthority extends DataEntity {


    @Column(name = "authority_type")
    private String authorityType;

    @Column(name = "authority_id")
    private String authorityId;

    public NoticeAuthority() {}

    public NoticeAuthority(String authorityType, String authorityId) {
        this.authorityType = authorityType;
        this.authorityId = authorityId;
    }

    public static NoticeAuthority of(String authorityType, String authorityId) {
        return new NoticeAuthority(authorityType, authorityId);
    }

    public String getAuthorityType() {
        return authorityType;
    }

    public void setAuthorityType(String authorityType) {
        this.authorityType = authorityType;
    }

    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

}
