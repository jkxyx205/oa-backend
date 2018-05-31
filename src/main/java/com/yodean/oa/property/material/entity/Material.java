package com.yodean.oa.property.material.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yodean.oa.common.entity.DataEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * Created by rick on 5/22/18.
 */
@Entity
@Table(name = "t_material")
public class Material extends DataEntity {
    /**
     * 物料id
     */
    private String bid;

    /**
     * 物料名称
     */
    private String title;


    /**
     * 物料规格
     */
    private String specification;

    /**
     * 换算维度
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "material")
    private ConversionCategory category;

    @Transient
    @JsonInclude(NON_EMPTY)
    private List<ConversionUnit> conversionUnits = new ArrayList<>();

    @Transient
    private Integer baseUnitId;

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public ConversionCategory getCategory() {
        return category;
    }

    public void setCategory(ConversionCategory category) {
        this.category = category;
    }


    public List<ConversionUnit> getConversionUnits() {
        return conversionUnits;
    }

    public void setConversionUnits(List<ConversionUnit> conversionUnits) {
        this.conversionUnits = conversionUnits;
    }

    public Integer getBaseUnitId() {
        return baseUnitId;
    }

    public void setBaseUnitId(Integer baseUnitId) {
        this.baseUnitId = baseUnitId;
    }
}