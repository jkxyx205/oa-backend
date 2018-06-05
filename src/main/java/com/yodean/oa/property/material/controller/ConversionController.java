package com.yodean.oa.property.material.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.property.material.entity.ConversionCategory;
import com.yodean.oa.property.material.entity.ConversionUnit;
import com.yodean.oa.property.material.service.UnitConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rick on 5/24/18.
 */
@RestController
@RequestMapping("/conversions")
public class ConversionController {
    @Autowired
    private UnitConversionService conversionService;

    /**
     * 添加维度
     */
    @PostMapping("/categories")
    public Result<Integer> addCategory(@RequestBody ConversionCategory category) {
        conversionService.addCategory(category);
        return ResultUtil.success(category.getId());
    }

    /**
     * 查看维度
     *
     * @param id
     * @return
     */
    @GetMapping("/categories/{id}")
    public Result<ConversionCategory> findCategoryById(@PathVariable Integer id) {
        return ResultUtil.success(conversionService.findCategoryById(id));
    }

    /**
     * 添加单位
     */
    @PostMapping("/units")
    public Result<Integer> addUnit(@RequestBody ConversionUnit conversionUnit) {
        conversionService.addUnit(conversionUnit);
        return ResultUtil.success(conversionUnit.getId());
    }

    /**
     * 编辑单位
     */
    @PutMapping("/units/{id}")
    public Result updateUnit(@RequestBody ConversionUnit conversionUnit, @PathVariable Integer id) {
        conversionService.updateUnit(conversionUnit, id);
        return ResultUtil.success();
    }

    /**
     * 删除单位
     *
     * @param id
     * @return
     */
    @DeleteMapping("units/{id}")
    public Result deleteUnits(@PathVariable Integer id) {
        conversionService.deleteUnit(id);
        return ResultUtil.success();
    }

    @GetMapping("/units/{id}")
    public Result<ConversionUnit> findById(@PathVariable Integer id) {
        return ResultUtil.success(conversionService.findUnitById(id));
    }


}
