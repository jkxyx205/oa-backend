package com.yodean.oa.property.material.service;

import com.yodean.oa.property.material.Constant;
import com.yodean.oa.property.material.dao.ConversionCategoryRepository;
import com.yodean.oa.property.material.dao.IncomingRepository;
import com.yodean.oa.property.material.dao.MaterialRepository;
import com.yodean.oa.property.material.entity.ConversionCategory;
import com.yodean.oa.property.material.entity.ConversionUnit;
import com.yodean.oa.property.material.entity.Incoming;
import com.yodean.oa.property.material.entity.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

/**
 * Created by rick on 5/22/18.
 */
@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private IncomingRepository incomingRepository;

    @Autowired
    private UnitConversionService unitConversionService;

    @Autowired
    private ConversionCategoryRepository conversionCategoryRepository;

    /**
     * 保存物料主数据
     *
     * @param material
     * @return
     */
    @Transactional
    public Material save(Material material) {
        bind(material);
        material = materialRepository.save(material);
        //保存单位转换 TODO
        return material;
    }

    /**
     * 编辑物料主数据
     *
     * @param material
     * @return
     */
    @Transactional
    public Material update(Material material, Integer id) {
        material.setId(id);
        bind(material);
        material = materialRepository.updateCascade(material);
        return material;
    }

    private void bind(Material material) {
        //换算关系
        ConversionCategory category = new ConversionCategory();

        if (Objects.nonNull(material.getId())) { //修改
            ConversionCategory _category = conversionCategoryRepository.findByMaterial(material);
            if (Objects.nonNull(_category)) {
                category.setId(_category.getId());
                category.setBaseUnit(_category.getBaseUnit());
            }
        } else {//新增
            ConversionUnit baseUnit = unitConversionService.findUnitById(material.getBaseUnitId());
            category.setBaseUnit(baseUnit);
        }

        category.setMaterial(material);
        category.setTitle(material.getTitle());
        category.setConversionDetailList(material.getConversionUnits());
        category.getConversionDetailList().forEach(_conversionUnit -> {
            _conversionUnit.setConversionCategory(category);
            _conversionUnit.setCategoryId(category.getId());
        });

        material.setCategory(category);
    }

    /**
     * 删除物料主数据
     *
     * @param id
     * @return
     */
    public Integer delete(Integer id) {
        return materialRepository.deleteLogical(id).size();
    }

    /**
     * 查找物料
     *
     * @param id
     * @return
     */
    public Material findById(Integer id) {
        return materialRepository.get(id);
    }

    /**
     * 入库操作
     */
    public void addIncoming(Incoming incoming) {
        incomingRepository.save(incoming);
    }

    /**
     * 更新耗材或设备
     * 保管人 + 可见范围 + 序列号（设备）
     */
    public void update(Incoming incoming, Integer id) {
        incoming.setId(id);
        incomingRepository.update(incoming);
    }

    /**
     * sno不为空，则为设备；否则为耗材
     * @param id
     * @param borrowNum 借用数量
     * @param borrowUnit 借用单位
     */
    public void borrow(Integer id, double borrowNum, Integer borrowUnit) {
        Incoming incoming = incomingRepository.load(id);


        borrowNum =  unitConversionService.convertUnit(borrowNum, borrowUnit, incoming.getBaseUnitId());

        //删减数量
        incoming.setBaseNum(incoming.getBaseNum() - borrowNum); //TODO check

        //修改状态
        if (0 == incoming.getBaseNum()) {
            incoming.setStatus(Constant.STATUS_NONE);
        } else {
            incoming.setStatus(Constant.STATUS_RICH);
        }


        if (Objects.nonNull(incoming.getSno())) { //设备
            incomingRepository.save(incoming);
        } else {//耗材
            if (Constant.STATUS_NONE == incoming.getStatus()) {//库存0 删除记录
                incomingRepository.delete(incoming);
            } else {

            }
            incomingRepository.save(incoming);
        }
    }

    public void back() {

    }
}
