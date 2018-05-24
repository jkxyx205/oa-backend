package com.yodean.oa.property.material.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yodean.oa.common.entity.DataEntity;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Created by rick on 5/22/18.
 */
@Entity
@Table(name = "t_material_conversion_unit")
public class ConversionUnit extends DataEntity {
    /**
     * 单位英文标示，如m，kg
     */
    private String name;

    /**
     * 单位中文名称，如米，千克
     */
    private String title;
    /**
     * 分母
     */
    private Integer denominator;

    /**
     * 分子
     */
    private Integer numerator;

    /**
     * 常量
     */
    private Double constant;

    /**
     * 所属分类
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private ConversionCategory conversionCategory;

    @Transient
    private Integer categoryId;

    @Transient
    private Double convertedNumber = 1d;

    /**
     * 要换算的单位
     */
    @Transient
    private ConversionUnit convertedUnit;

    @PreUpdate
    @PrePersist
    private void injectParams() {
        if (Objects.nonNull(categoryId)) {
            conversionCategory = (conversionCategory == null ? new ConversionCategory() : conversionCategory);
            conversionCategory.setId(categoryId);
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDenominator() {
        return denominator;
    }

    public void setDenominator(Integer denominator) {
        this.denominator = denominator;
    }

    public Integer getNumerator() {
        return numerator;
    }

    public void setNumerator(Integer numerator) {
        this.numerator = numerator;
    }

    public Double getConstant() {
        return constant;
    }

    public void setConstant(Double constant) {
        this.constant = constant;
    }

    public ConversionCategory getConversionCategory() {
        return conversionCategory;
    }

    public void setConversionCategory(ConversionCategory conversionCategory) {
        this.conversionCategory = conversionCategory;
    }

    public void setConvertedUnit(ConversionUnit convertedUnit) {
        this.convertedUnit = convertedUnit;
    }


    public void setConvertedNumber(Double convertedNumber) {
        this.convertedNumber = convertedNumber;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @JsonIgnore
    public String getConversionValue() {
        double result = (this.numerator * convertedUnit.getDenominator() * convertedNumber) / (this.denominator * convertedUnit.getNumerator()) + this.constant - convertedUnit.constant;
        DecimalFormat decimalFormat = new DecimalFormat("#0.000");
        return decimalFormat.format(Math.abs(result));
    }

    /**
     * 获取转换信息
     *
     * @return
     */
    public String getConversionText() {
        if (Objects.isNull(convertedUnit)) return null;

        StringBuilder sb = new StringBuilder(convertedNumber + " ");
        sb.append(this.getName()).append(" = ");
        sb.append(getConversionValue());
        sb.append(" " + convertedUnit.getName());

        return sb.toString();
    }

    /**
     * 获取完整公式
     *
     * @return
     */
    public String getConversionFULLText() {
        if (Objects.isNull(convertedUnit)) return null;
        StringBuilder sb = new StringBuilder("A * " + this.title + " = (A * " + this.numerator + "/" + this.denominator + "  + " + this.constant + ") * " + this.conversionCategory.getBaseUnit().title + "");
        return sb.toString();
    }
}
