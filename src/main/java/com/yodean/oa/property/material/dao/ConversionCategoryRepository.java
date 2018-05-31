package com.yodean.oa.property.material.dao;

import com.yodean.oa.common.dao.ExtendedRepository;
import com.yodean.oa.property.material.entity.ConversionCategory;
import com.yodean.oa.property.material.entity.Material;

/**
 * Created by rick on 5/22/18.
 */
public interface ConversionCategoryRepository extends ExtendedRepository<ConversionCategory, Integer> {
    ConversionCategory findByMaterial(Material material);
}
