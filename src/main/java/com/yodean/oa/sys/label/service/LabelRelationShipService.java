package com.yodean.oa.sys.label.service;

import com.yodean.oa.common.enums.CategoryEnum;
import com.yodean.oa.common.service.BaseService;
import com.yodean.oa.sys.label.dao.LabelRelationShipRepository;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.label.entity.LabelRelationShip;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2018/3/20.
 */
@Service
public class LabelRelationShipService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private BaseService baseService;

    @Resource
    private LabelRelationShipRepository labelRelationShipRepository;

    public LabelRelationShip save(LabelRelationShip labelRelationShip) {
        return labelRelationShipRepository.save(labelRelationShip);
    }

    public void deleteLabel(CategoryEnum categoryEnum, Integer categoryId) {
        String sql = "DELETE FROM sys_label_relationship WHERE category = ? and category_id = ?";
        jdbcTemplate.update(sql, new Object[] {categoryEnum.name(), categoryId});
    }

    public List<Label> getLabels(CategoryEnum categoryEnum, Integer categoryId) {
        String sql = "SELECT\n" +
                "\tid,color, title, remarks, update_by updateBy, update_Date updateDate,create_by createBy, create_date createDate\n" +
                "FROM\n" +
                "\tsys_label lab\n" +
                "WHERE\n" +
                "\tEXISTS (\n" +
                "\t\tSELECT\n" +
                "\t\t\t1\n" +
                "\t\tFROM\n" +
                "\t\t\tsys_label_relationship rel\n" +
                "\t\tWHERE\n" +
                "\t\t\trel.category_id = :category_id\n" +
                "\t\t\tAND rel.category = :category\n" +
                "\t\tAND lab.id = rel.label_id\n" +
                "\t)";

        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("category", categoryEnum.name());
        params.put("category_id", categoryId);
        return baseService.query(sql, params, Label.class);
    }
}
