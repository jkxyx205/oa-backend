package com.yodean.oa.property.vehicle.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.util.ResultUtils;
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
            return ResultUtils.error(ResultCode.VALIDATE_ERROR, result.getAllErrors());

        return ResultUtils.success(vehicleService.save(vehicle).getId());
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody Vehicle vehicle, @PathVariable Integer id) {
        vehicleService.update(vehicle, id);
        return ResultUtils.success();
    }

    /**
     * 启用 禁用 报废
     *
     * @param status
     * @param id
     * @return
     */
    @PutMapping("/{id}/{status}")
    public Result status(@PathVariable Vehicle.VehicleStatus status, @PathVariable Integer id) {
        vehicleService.changeStatus(id, status);
        return ResultUtils.success();
    }

    @GetMapping("/{id}")
    public Result<Vehicle> findById(@PathVariable Integer id) {
        return ResultUtils.success(vehicleService.findById(id));
    }

    @GetMapping
    public Result<Page<Vehicle>> findByKeywords(@RequestParam(defaultValue = "") String kw, @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "15") Integer rows) {
        return ResultUtils.success(vehicleService.list(kw, pageNo, rows));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id) {
        vehicleService.delete(id);
        return ResultUtils.success();
    }

    @PostMapping("/records")
    public Result addRecord(@RequestBody UsageRecord usageRecord) {
        vehicleService.addVehicleUsageRecord(usageRecord);
        return ResultUtils.success();
    }

}