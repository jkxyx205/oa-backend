package com.yodean.oa.task.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.plugin.document.entity.Document;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by rick on 4/2/18.
 */
@Entity
@Table(name = "sys_task_discussion")
public class Discussion extends DataEntity {

    private String content;

    @Transient
    private List<Document> documents;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonIgnore
    private Task task;

    @Transient
    private Set<Integer> docIds;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Set<Integer> getDocIds() {
        return docIds;
    }

    public void setDocIds(Set<Integer> docIds) {
        this.docIds = docIds;
    }
}
