package com.yodean.oa.property.material.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yodean.oa.common.entity.DataEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by rick on 5/22/18.
 */
@Entity
@Table(name = "t_material_conversion_category")
public class ConversionCategory extends DataEntity {
    /**
     * 标题
     */
    private String title;

    /**
     * 基本单位
     */
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "unit_id", updatable = false)
    private ConversionUnit baseUnit;

    /**
     * 分类所有单位
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "conversionCategory", orphanRemoval = true)
    private List<ConversionUnit> conversionDetailList = new ArrayList<>();

    /**
     * 0 系统维度
     * 1...n 物料号
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    private Material material;

    @PreUpdate
    @PrePersist
    private void before() {
        if (Objects.isNull(material)) {
            material = new Material();
            material.setId(0);
        }
    }

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

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConversionCategory)) return false;
        if (super.equals(o)) return true;

//        ConversionCategory that = (ConversionCategory) o;
//
//        return new EqualsBuilder().append(this.getMaterial().getId(), that.getMaterial().getId()).isEquals();
        return false;
    }

    @Override
    public int hashCode() {
//        if (this.getMaterial() == null)
            return Objects.hash(this.getId());

//        return Objects.hash(this.getMaterial().getId());
    }
}
