package com.yodean.oa.property.material.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.property.material.entity.Incoming;
import com.yodean.oa.property.material.entity.Material;
import com.yodean.oa.property.material.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rick on 5/22/18.
 */
@RestController
@RequestMapping("/materials")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @PostMapping
    public Result<Material> save(@RequestBody Material material) {
        return ResultUtil.success(materialService.save(material));
    }

    @GetMapping("/{id}")
    public Result<Material> findById(@PathVariable Integer id) {
        return ResultUtil.success(materialService.findById(id));
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody Material material, @PathVariable Integer id) {
        materialService.update(material, id);
        return ResultUtil.success();
    }

    @PostMapping("/incoming")
    public Result incoming(@RequestBody Incoming incoming) {
        materialService.addIncoming(incoming);
        return ResultUtil.success();
    }

    @PostMapping("/incoming/{id}/borrow")
    public Result<Incoming> borrow(@PathVariable Integer id, Integer num, Integer unitId) {
        return ResultUtil.success(materialService.borrow(id, num, unitId));
    }

}
