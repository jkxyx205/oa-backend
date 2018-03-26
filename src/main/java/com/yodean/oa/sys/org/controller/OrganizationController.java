package com.yodean.oa.sys.org.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.sys.org.entity.Organization;
import com.yodean.oa.sys.org.service.OrganizationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by rick on 2018/3/23.
 */
@RestController
@RequestMapping("/orgs")
public class OrganizationController {

    @Resource
    private OrganizationService organizationService;

    @PostMapping
    public Result<Organization> saveOrganization(Organization organization) {
        return ResultUtil.success(organizationService.save(organization));
    }

    @GetMapping
    public Result<List<Organization>> getAllOrgs() {
        List<Organization> list = organizationService.findAll();
        return ResultUtil.success(list);
    }
}
