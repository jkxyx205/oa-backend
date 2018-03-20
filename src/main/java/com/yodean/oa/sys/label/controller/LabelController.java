package com.yodean.oa.sys.label.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.label.service.LabelService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by rick on 2018/3/20.
 */
@RestController
@RequestMapping("/labels")
public class LabelController {

    @Resource
    private LabelService labelService;

    @PostMapping
    public Result<Label> save(Label label) {
        return ResultUtil.success(labelService.save(label));
    }
}
