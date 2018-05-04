package com.yodean.oa.sys.label.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.Category;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.label.service.LabelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by rick on 4/3/18.
 */
@RestController
@RequestMapping("/labels")
public class LabelController {

    @Resource
    private LabelService labelService;
    /***
     * 添加标签
     */
    @PostMapping("/{category}/{categoryId}")
    public Result<Integer> save(@PathVariable Label.LabelCategory category, @PathVariable Integer categoryId, @RequestBody Label label) {
        return ResultUtil.success(labelService.save(category, categoryId, label));
    }


    /***
     * 删除标签
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        labelService.delete(id);
        return ResultUtil.success();
    }

}
