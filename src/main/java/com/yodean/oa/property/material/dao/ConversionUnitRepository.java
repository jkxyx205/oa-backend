package com.yodean.oa.property.material.dao;

import com.yodean.oa.common.dao.ExtendedRepository;
import com.yodean.oa.common.enums.Category;
import com.yodean.oa.property.material.entity.ConversionCategory;
import com.yodean.oa.property.material.entity.ConversionUnit;

/**
 * Created by rick on 5/22/18.
 */
public interface ConversionUnitRepository extends ExtendedRepository<ConversionUnit, Integer> {
    ConversionUnit findByConversionCategoryAndName(ConversionCategory category, String name);
}
