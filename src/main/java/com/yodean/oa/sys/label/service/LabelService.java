package com.yodean.oa.sys.label.service;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.sys.label.dao.LabelRepository;
import com.yodean.oa.sys.label.entity.Label;
import org.springframework.data.domain.Example;
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

    public void save(Label.LabelCategory category, Integer categoryId, List<Label> labels) {
        labels.forEach(label -> {
            labelSetter(label, category, categoryId);
        });

        labelRepository.saveAll(labels);
    }

    public Integer save(Label.LabelCategory category, Integer categoryId, Label label) {
        labelRepository.save(labelSetter(label, category, categoryId));

        return label.getId();
    }

    public void delete(Integer id) {
        labelRepository.deleteLogical(id);
    }

    public List<Label> findLabels(Label.LabelCategory category, Integer categoryId) {
        Label label = labelSetter(new Label(), category, categoryId);

        label.setDelFlag(DataEntity.DEL_FLAG_NORMAL);

        Example<Label> example = Example.of(label);

        return labelRepository.findAll(example);
    }

    private Label labelSetter(Label label, Label.LabelCategory category, Integer categoryId) {
        label.setLabelId(new Label.LabelId(category, categoryId));
        return label;
    }


}
