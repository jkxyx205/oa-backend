package com.yodean.oa.property.material.entity;

import com.yodean.oa.common.entity.DataEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 5/22/18.
 */
@Entity
@Table(name = "sys_conversion_category")
public class ConversionCategory extends DataEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 基本单位
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "unit_id")
    private ConversionUnit baseUnit;

    /**
     * 分类所有单位
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "conversionCategory")
    private List<ConversionUnit> conversionDetailList = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ConversionUnit getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(ConversionUnit baseUnit) {
        this.baseUnit = baseUnit;
    }

    public List<ConversionUnit> getConversionDetailList() {
        return conversionDetailList;
    }

    public void setConversionDetailList(List<ConversionUnit> conversionDetailList) {
        this.conversionDetailList = conversionDetailList;
    }
}
