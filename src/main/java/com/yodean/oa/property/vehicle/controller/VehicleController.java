package com.yodean.oa.property.vehicle.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.property.vehicle.entity.Vehicle;
import com.yodean.oa.property.vehicle.service.VehicleService;
import com.yodean.oa.sys.dictionary.entity.Word;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
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


    @GetMapping("/list")
    public Result<Page<Vehicle>> findByKeywords(String kw, int pageNo, int rows) {
        return ResultUtil.success(vehicleService.list(kw, pageNo, rows));
    }


    @PostMapping
    public Result save(@Valid @RequestBody Vehicle vehicle, BindingResult result) {
        if (result.hasErrors())
            return ResultUtil.error(ResultCode.VALIDATE_ERROR, result.getAllErrors());

        return ResultUtil.success(vehicleService.save(vehicle));

    }

}
