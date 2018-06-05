package com.yodean.oa.sys.dictionary.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.sys.dictionary.core.WordDeserializer;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Objects;

/**
 * Created by rick on 5/17/18.
 */
@Entity
@Table(name = "sys_dictionary",
        uniqueConstraints = {@UniqueConstraint(columnNames={"category", "name"})})
@JsonDeserialize(using = WordDeserializer.class)
public class Word extends DataEntity {

    private String category;

    private String name;

    private String description;

    private String seq;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (super.equals(o)) return true;
        if (!(o instanceof Word)) return false;

        Word word = (Word) o;

        return new EqualsBuilder().append(category, word.category)
                .append(name, word.name)
                .isEquals();
    }
    @Override
    public int hashCode() {
        return Objects.hash(category, name);
    }
}