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
public class ConversionServiceTest {

    @Autowired
    private UnitConversionService conversionService;

    @Test
    public void addCategory() throws Exception {
        ConversionUnit conversionUnit = new ConversionUnit();
        conversionUnit.setName("K");
        conversionUnit.setTitle("绝对温标");
        conversionUnit.setDenominator(1);
        conversionUnit.setNumerator(1);
        conversionUnit.setConstant(0d);

        ConversionCategory conversionCategory = new ConversionCategory();
        conversionCategory.setTitle("温度单位");

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
        conversionUnit.setName("F");
        conversionUnit.setTitle("华氏温度");
        conversionUnit.setNumerator(5);
        conversionUnit.setDenominator(9);
        conversionUnit.setConstant(255.3722d);

        ConversionCategory conversionCategory = new ConversionCategory();
        conversionCategory.setId(2);

        conversionUnit.setConversionCategory(conversionCategory);
        conversionService.addUnit(conversionUnit);
    }

    @Test
    public void findCategoryById() {
        ConversionCategory conversionCategory = conversionService.findCategoryById(1);
        conversionCategory.getConversionDetailList().forEach(conversionUnit -> System.out.println(conversionUnit.getConversionText()));
    }

    @Test
    public void updateUnit() throws Exception {

    }

    @Test
    public void convertUnitTest() {
        //32F = ?K
        System.out.println("->" +  conversionService.convertUnit(32d, 4, 3));
//        System.out.println("->" +  conversionService.convertUnit(10d, 1, 2));
//        System.out.println("->" +  conversionService.convertUnit(10d, 2, 1));
//        System.out.println("->" +  conversionService.convertUnit(10d, 4, 1));

    }

}