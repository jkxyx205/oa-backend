package com.yodean.oa.notice.entity;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.sys.label.entity.Label;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by rick on 5/4/18.
 */
@Entity
@Table(name="t_notice")
public class Notice extends DataEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    /**
     * 是否置顶
     */
    private Boolean top;


    /**
     * 附件
     */
    @Transient
    private List<Document> documents;

    @Transient
    private Set<Integer> docIds;

    @Transient
    private List<Label> labels;

    /**
     * 查看范围
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name="notice_id")
    private List<NoticeAuthority> noticeAuthorityList;

    /**
     * 封面路径
     */
    private String cover;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean isTop() {
        return top;
    }

    public void setTop(Boolean top) {
        this.top = top;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Set<Integer> getDocIds() {
        return docIds;
    }

    public void setDocIds(Set<Integer> docIds) {
        this.docIds = docIds;
    }

    public List<NoticeAuthority> getNoticeAuthorityList() {
        return noticeAuthorityList;
    }

    public void setNoticeAuthorityList(List<NoticeAuthority> noticeAuthorityList) {
        this.noticeAuthorityList = noticeAuthorityList;
    }
}
