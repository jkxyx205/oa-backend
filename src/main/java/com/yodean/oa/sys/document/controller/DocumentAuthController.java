package com.yodean.oa.sys.document.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.sys.document.dto.AuthorityDto;
import com.yodean.oa.sys.document.service.AuthorityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by rick on 4/17/18.
 */
@RestController
@RequestMapping("/pan/company/documents")
public class DocumentAuthController {

    @Resource
    private AuthorityService authorityService;

    /**
     * 添加文件夹
     * @param document
     * @return
     */
    @PostMapping
    public Result<Integer> addFolder(@RequestBody Document document) {
        Integer docId = authorityService.addFolder(document.getParentId(), document.getName());
        return ResultUtil.success(docId);
    }

    @PostMapping("/{id}/auth")
    public Result auth(@RequestBody AuthorityDto authorityDto, @PathVariable Integer id) {
        authorityDto.setDocumentId(id);
        authorityService.addAuthority(authorityDto);

        return ResultUtil.success();
    }


}
