package com.yodean.oa.sys.label.service;

import com.yodean.oa.sys.label.dao.LabelRepository;
import com.yodean.oa.sys.label.entity.Label;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by rick on 2018/3/20.
 */
@Service
public class LabelService {

    @Resource
    private LabelRepository labelRepository;

    public Label save(Label label) {
        return labelRepository.save(label);
    }
}
