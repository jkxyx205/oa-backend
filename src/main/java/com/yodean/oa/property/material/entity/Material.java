package com.yodean.oa.property.material.entity;

import com.yodean.oa.common.entity.DataEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
     * 计量单位(仓库基本单位)
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unit_id")
    private ConversionUnit unit = new ConversionUnit();

    /**
     *  物料转换关系
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private List<ConversionUnit> selfUnit = new ArrayList<>();

    @Transient
    private Integer unitId;

    /**
     * 物料规格
     */
    private String specification;

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

    public ConversionUnit getUnit() {
        return unit;
    }

    public void setUnit(ConversionUnit unit) {
        this.unit = unit;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public List<ConversionUnit> getSelfUnit() {
        return selfUnit;
    }

    public void setSelfUnit(List<ConversionUnit> selfUnit) {
        this.selfUnit = selfUnit;
    }
}
