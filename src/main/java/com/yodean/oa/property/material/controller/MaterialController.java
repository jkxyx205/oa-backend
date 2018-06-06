package com.yodean.oa.property.material.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.property.material.entity.Incoming;
import com.yodean.oa.property.material.entity.Material;
import com.yodean.oa.property.material.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by rick on 5/22/18.
 */
@RestController
@RequestMapping("/materials")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    /**
     * 添加物料
     * @param material
     * @return
     */
    @PostMapping
    public Result<Material> save(@RequestBody Material material) {
        return ResultUtil.success(materialService.save(material));
    }

    /**
     * 根据Id查看物料详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Material> findById(@PathVariable Integer id) {
        return ResultUtil.success(materialService.findById(id));
    }

    /**
     * 更新物料
     * @param material
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result update(@RequestBody Material material, @PathVariable Integer id) {
        materialService.update(material, id);
        return ResultUtil.success();
    }

    /**
     * 入库
     * @param incoming
     * @return
     */
    @PostMapping("/incoming")
    public Result incoming(@RequestBody Incoming incoming) {
        materialService.addIncoming(incoming);
        return ResultUtil.success();
    }

    @GetMapping("/incoming")
    public Result<List<Incoming>> list(@RequestParam(defaultValue = "") String kw,  @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "15") Integer rows) {
        return ResultUtil.success(materialService.listIncoming(kw, pageNo, rows));
    }

    /**
     * 领用／借用
     * @param id
     * @param num
     * @param unitId
     * @return
     */
    @PostMapping("/incoming/{id}/borrow")
    public Result<Incoming> borrow(@PathVariable Integer id, Integer num, Integer unitId) {
        return ResultUtil.success(materialService.borrow(id, num, unitId));
    }

    @PostMapping("/incoming/{id}/back/{storageId}")
    public Result<Incoming> borrow(@PathVariable Integer id, @PathVariable Integer storageId) {
        return ResultUtil.success(materialService.back(id, storageId));
    }

}