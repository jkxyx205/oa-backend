package com.yodean.oa.property.material.service;

import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.property.material.Constant;
import com.yodean.oa.property.material.dao.ConversionCategoryRepository;
import com.yodean.oa.property.material.dao.IncomingRepository;
import com.yodean.oa.property.material.dao.MaterialRepository;
import com.yodean.oa.property.material.entity.ConversionCategory;
import com.yodean.oa.property.material.entity.ConversionUnit;
import com.yodean.oa.property.material.entity.Incoming;
import com.yodean.oa.property.material.entity.Material;
import org.apache.commons.lang3.RandomStringUtils;
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
        //保存单位转换 TODO 检查单位换算是否与预设产生冲突，如果产生冲突自动纠错
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

    /**
     * 根据前台传递的参数，组装实体类
     * @param material
     */
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
        //单位转换
        Material material = findById(incoming.getMaterialId());
        incoming.setBaseUnitId(material.getCategory().getBaseUnit().getId());

        incoming.setBaseNum(convertUnit(incoming.getNum(), incoming.getUnitId(), material));
        incoming.setBatchNum(RandomStringUtils.randomAlphanumeric(16));
        //如果是设备
        if (Material.EQUIPMENT.equals(material.getType())) { //TODO
            //序列号
        } else { //耗材

        }

        incomingRepository.save(incoming);
    }

    /**
     * 单位转换
     * @param num
     * @param unitId
     * @param material
     * @return
     */
    private double convertUnit(double num, int unitId, Material material) {
        ConversionUnit baseUnit = material.getCategory().getBaseUnit(); //获取基准id
        ConversionUnit inputUnit = unitConversionService.findUnitById(unitId);

        int baseId;
        if (baseUnit.getConversionCategory().getId().equals(inputUnit.getConversionCategory().getId())) { //预设单位维度
            baseId = baseUnit.getId();
        } else {
            ConversionUnit storageUnit = unitConversionService.findByConversionCategoryAndName(inputUnit.getConversionCategory(), baseUnit.getName());
            baseId = storageUnit.getId();
        }

        return unitConversionService.convertUnit(num, unitId, baseId);
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
     * 则为设备；否则为耗材
     * @param id incoming Id
     * @param borrowNum 借用数量
     * @param borrowUnit 借用单位
     */
    public Incoming borrow(Integer id, double borrowNum, Integer borrowUnit) {
        Incoming incoming = incomingRepository.load(id);

        Material material = findById(incoming.getMaterialId());

        borrowNum = convertUnit(borrowNum, borrowUnit, material);

        //删减数量
        incoming.setBaseNum(incoming.getBaseNum() - borrowNum);
        if(incoming.getBaseNum() < 0) {
            throw new OAException(ResultCode.NUM_NOT_ENOUGH);
        }

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

        return incoming;
    }

    /**设备归还
     * @param id incoming Id
     * @param borrowNum 借用数量
     * @param borrowUnit 借用单位
     * @param storageId 库位id
     */
    public void back(int id, double borrowNum, Integer borrowUnit, Integer storageId) {
        Incoming incoming = incomingRepository.load(id);

        Material material = findById(incoming.getMaterialId());

        borrowNum = convertUnit(borrowNum, borrowUnit, material);

        //添加数量
        incoming.setBaseNum(incoming.getBaseNum() + borrowNum);

        incoming.setStatus(Constant.STATUS_RICH);
        incoming.setStorageId(storageId);

        incomingRepository.save(incoming);
    }

    /**
     * 移库操作
     */
    public void move() {

    }

}
