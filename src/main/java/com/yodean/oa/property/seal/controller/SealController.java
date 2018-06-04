package com.yodean.oa.property.seal.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.property.seal.entity.Seal;
import com.yodean.oa.property.seal.service.SealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rick on 6/1/18.
 */
@RestController
@RequestMapping("/seals")
public class SealController {
    @Autowired
    private SealService sealService;

    @PostMapping
    public Result<Integer> save(@RequestBody Seal seal) {
        return ResultUtil.success(sealService.save(seal));
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody Seal seal, @PathVariable Integer id) {
        sealService.update(seal, id);
        return ResultUtil.success();
    }
}
