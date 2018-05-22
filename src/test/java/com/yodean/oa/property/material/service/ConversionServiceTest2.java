package com.yodean.oa.property.material.service;

import com.yodean.oa.property.material.entity.ConversionCategory;
import com.yodean.oa.property.material.entity.ConversionUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by rick on 5/22/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConversionServiceTest2 {

    @Autowired
    private UnitConversionService conversionService;

    @Test
    public void addCategory() throws Exception {
        ConversionUnit conversionUnit = new ConversionUnit();
        conversionUnit.setName("m");
        conversionUnit.setTitle("米");
        conversionUnit.setDenominator(1);
        conversionUnit.setNumerator(1);
        conversionUnit.setConstant(0d);

        ConversionCategory conversionCategory = new ConversionCategory();
        conversionCategory.setTitle("长度单位");

        conversionCategory.setBaseUnit(conversionUnit);
        conversionUnit.setConversionCategory(conversionCategory);

        conversionService.addCategory(conversionCategory);
    }

    @Test
    public void updateCategory() throws Exception {

    }

    @Test
    public void addUnit() throws Exception {
        ConversionUnit conversionUnit = new ConversionUnit();
        conversionUnit.setName("dm");
        conversionUnit.setTitle("分米");
        conversionUnit.setNumerator(1);
        conversionUnit.setDenominator(10);
        conversionUnit.setConstant(0d);

        ConversionCategory conversionCategory = new ConversionCategory();
        conversionCategory.setId(1);

        conversionUnit.setConversionCategory(conversionCategory);
        conversionService.addUnit(conversionUnit);
    }

    /**
     * 不添加换算关系
     * @throws Exception
     */
    @Test
    public void addUnit2() throws Exception {
        ConversionUnit conversionUnit = new ConversionUnit();
        conversionUnit.setName("nm");
        conversionUnit.setTitle("纳米");

        ConversionCategory conversionCategory = new ConversionCategory();
        conversionCategory.setId(1);

        conversionUnit.setConversionCategory(conversionCategory);

        conversionService.addUnit(conversionUnit);
    }


    @Test
    public void findCategoryById() {
        ConversionCategory conversionCategory = conversionService.findCategoryById(1);
    }

    @Test
    public void updateUnit() throws Exception {

    }

    @Test
    public void convertUnitTest() {
        System.out.println("->" +  conversionService.convertUnit(10d, 2, 1));

    }

}