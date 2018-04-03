package com.yodean.oa.sys.label.service;

import com.yodean.oa.common.enums.Category;
import com.yodean.oa.sys.label.dao.LabelRepository;
import com.yodean.oa.sys.label.entity.Label;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by rick on 2018/3/20.
 */
@Service
public class LabelService {

    @Resource
    private LabelRepository labelRepository;

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void save(Category category, Integer categoryId, List<Label> labels) {
        delete(category, categoryId);
        labels.forEach(label -> {
            label.setCategory(category);
            label.setCategoryId(categoryId);
        });

        labelRepository.saveAll(labels);
    }


    public int delete(Category category, Integer categoryId) {
        String sql = "DELETE FROM sys_label WHERE category = ? and category_id = ?";
        return jdbcTemplate.update(sql, category.name(), categoryId);
    }

    public List<Label> findLabels(Category category, Integer categoryId) {
        Label label = new Label();
        label.setCategory(category);
        label.setCategoryId(categoryId);

        Example<Label> example = Example.of(label);

        return labelRepository.findAll(example);
    }


}
