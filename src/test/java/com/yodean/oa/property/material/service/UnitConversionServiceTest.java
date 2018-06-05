package com.yodean.oa.property.material.service;

import com.yodean.oa.property.material.entity.ConversionUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by rick on 6/5/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitConversionServiceTest {
    @Autowired
    private  UnitConversionService unitConversionService;

    @Test
    public void deleteUnit() throws Exception {
        unitConversionService.deleteUnit(8);
    }

}