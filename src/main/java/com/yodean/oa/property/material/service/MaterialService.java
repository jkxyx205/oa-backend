package com.yodean.oa.property.material.service;

import com.yodean.oa.property.material.dao.MaterialRepository;
import com.yodean.oa.property.material.entity.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by rick on 5/22/18.
 */
@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    /**
     * 保存物料主数据
     *
     * @param material
     * @return
     */
    @Transactional
    public Material save(Material material) {
        material.getUnit().setId(material.getUnitId());
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
    public Material update(Material material, Integer id) {
        material.setId(id);
        material = materialRepository.update(material);
        return material;
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
        return materialRepository.load(id);
    }

}
