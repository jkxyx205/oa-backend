package com.yodean.oa.property.material.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.property.material.entity.Storage;
import com.yodean.oa.property.material.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rick on 5/24/18.
 */
@RestController
@RequestMapping("/storages")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping
    public Result<Integer> save(@RequestBody Storage storage) {
        return ResultUtil.success(storageService.save(storage));
    }

    @GetMapping("/{id}")
    public Result<Storage> findById(@PathVariable Integer id) {
        return ResultUtil.success(storageService.findById(id));
    }
}
