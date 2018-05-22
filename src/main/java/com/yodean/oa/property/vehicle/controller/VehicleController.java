package com.yodean.oa.property.vehicle.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.property.vehicle.entity.UsageRecord;
import com.yodean.oa.property.vehicle.entity.Vehicle;
import com.yodean.oa.property.vehicle.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by rick on 5/17/18.
 */
@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    @Resource
    private VehicleService vehicleService;

    @PostMapping
    public Result<Integer> save(@Valid @RequestBody Vehicle vehicle, BindingResult result) {
        if (result.hasErrors())
            return ResultUtil.error(ResultCode.VALIDATE_ERROR, result.getAllErrors());

        return ResultUtil.success(vehicleService.save(vehicle).getId());

    }

    @PutMapping("/{id}")
    public Result update(@RequestBody Vehicle vehicle, @PathVariable Integer id) {
        vehicleService.update(vehicle, id);
        return ResultUtil.success();
    }

    /**
     * 启用 禁用 报废
     * @param status
     * @param id
     * @return
     */
    @PutMapping("/{id}/{status}")
    public Result status(@PathVariable Vehicle.VehicleStatus status, @PathVariable Integer id) {
        vehicleService.changeStatus(id, status);
        return ResultUtil.success();
    }

    @GetMapping("/{id}")
    public Result<Vehicle> findById(@PathVariable Integer id) {
        return ResultUtil.success(vehicleService.findById(id));
    }

    @GetMapping("/list")
    public Result<Page<Vehicle>> findByKeywords(String kw, int pageNo, int rows) {
        return ResultUtil.success(vehicleService.list(kw, pageNo, rows));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id) {
        vehicleService.delete(id);
        return ResultUtil.success();
    }

    @PostMapping("/records")
    public Result addRecord(@RequestBody UsageRecord usageRecord) {
        vehicleService.addVehicleUsageRecord(usageRecord);
        return ResultUtil.success();
    }

}
