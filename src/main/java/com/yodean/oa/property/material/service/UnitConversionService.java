package com.yodean.oa.property.material.service;

import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.property.material.dao.ConversionCategoryRepository;
import com.yodean.oa.property.material.dao.ConversionUnitRepository;
import com.yodean.oa.property.material.entity.ConversionCategory;
import com.yodean.oa.property.material.entity.ConversionUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created by rick on 5/22/18.
 */
@Service
public class UnitConversionService {

    @Autowired
    private ConversionUnitRepository conversionUnitRepository;

    @Autowired
    private ConversionCategoryRepository conversionCategoryRepository;

    /**
     * 添加分类
     *
     * @param conversionCategory
     * @return
     */
    public ConversionCategory addCategory(ConversionCategory conversionCategory) {
        conversionCategory.getBaseUnit().setConversionCategory(conversionCategory);
        return conversionCategoryRepository.save(conversionCategory);
    }

    /**
     * 更新分类
     *
     * @param conversionCategory
     * @param id
     * @return
     */
    public ConversionCategory updateCategory(ConversionCategory conversionCategory, Integer id) {
        conversionCategory.setId(id);
        return conversionCategoryRepository.update(conversionCategory);
    }

    /**
     * 添加单位
     *
     * @param conversionUnit
     */
    public ConversionUnit addUnit(ConversionUnit conversionUnit) {
        return conversionUnitRepository.save(conversionUnit);
    }

    /**
     * 更新单位
     *
     * @param conversionUnit
     */
    public ConversionUnit updateUnit(ConversionUnit conversionUnit, Integer id) {
        conversionUnit.setId(id);
        return conversionUnitRepository.update(conversionUnit);
    }

    /**
     * 删除单位
     *
     * @param id
     */
    public void deleteUnit(Integer id) {
        conversionUnitRepository.deleteById(id);
    }

    /**
     * 查找分类下，所有单位
     *
     * @param id
     * @return
     */
    public ConversionCategory findCategoryById(Integer id) {
        ConversionCategory conversionCategory = conversionCategoryRepository.get(id);
        if (Objects.nonNull(conversionCategory)) {
            conversionCategory.getConversionDetailList().forEach(conversionUnit -> conversionUnit.setConvertedUnit(conversionCategory.getBaseUnit()));
        }

        return conversionCategory;
    }

    /**
     * 根据Id查找单位
     *
     * @param id
     * @return
     */
    public ConversionUnit findUnitById(Integer id) {
        return conversionUnitRepository.load(id);
    }

    /**
     * @param convertNumber 转换数量
     * @param srcUnitId     原单位
     * @param distUnitId    需要转换单位
     * @return
     */
    public String convertUnit(Double convertNumber, Integer srcUnitId, Integer distUnitId) {
        ConversionUnit srcConversionUnit = findUnitById(srcUnitId);
        ConversionUnit distConversionUnit = findUnitById(distUnitId);
        if (!Objects.equals(srcConversionUnit.getConversionCategory().getBaseUnit(), distConversionUnit.getConversionCategory().getBaseUnit())) {
            throw new OAException(ResultCode.UNIT_FORMAT_EXCEPTION);
        }

        srcConversionUnit.setConvertedUnit(distConversionUnit);
        srcConversionUnit.setConvertedNumber(convertNumber);

        return srcConversionUnit.getConversionValue();

    }
}
