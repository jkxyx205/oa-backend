package com.yodean.oa.sys.document.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.DocumentCategory;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.sys.document.dto.AuthorityDto;
import com.yodean.oa.sys.document.service.AuthorityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by rick on 4/17/18.
 * 企业盘
 */
@RestController
@RequestMapping("/pan/company/documents")
public class DocumentCompanyController {

    @Resource
    private AuthorityService authorityService;

    /**
     * 添加文件夹
     * @param document
     * @return
     */
    @PostMapping
    public Result<Integer> addFolder(@RequestBody Document document) {
        Integer docId = authorityService.mkdir(document.getParentId(), document.getName(), DocumentCategory.DISK_COMPANY);
        return ResultUtil.success(docId);
    }

    /**
     * 删除文件
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {

        authorityService.delete(id);

        return ResultUtil.success();
    }


    /**
     * 重命名文件
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result rename(@PathVariable Integer id, @RequestBody Document document) {
        authorityService.rename(id, document.getName());
        return ResultUtil.success();
    }


    /**
     * 下载文件
     * @param id
     * @return
     */
    @PostMapping("/{id}/download")
    public Result download(HttpServletResponse response, HttpServletRequest request, @PathVariable Integer id) throws IOException {
        authorityService.download(request, response, id);
        return ResultUtil.success();
    }


    /**
     * 移动文件
     * @param id
     * @return
     */
    @PostMapping("/{id}/{parentId}/move")
    public Result move(@PathVariable Integer id, @PathVariable Integer parentId) throws IOException {
        authorityService.move(id, parentId);
        return ResultUtil.success();
    }

    /**
     * 复制文件
     * @param id
     * @return
     */
    @PostMapping("/{id}/{parentId}/copy")
    public Result copy(@PathVariable Integer id, @PathVariable Integer parentId) throws IOException {
        authorityService.copy(id, parentId);
        return ResultUtil.success();
    }


    /***
     * 修改权限
     * @param authorityDto
     * @param id
     * @return
     */
    @PostMapping("/{id}/auth")
    public Result auth(@RequestBody AuthorityDto authorityDto, @PathVariable Integer id) {
        authorityDto.setDocumentId(id);
        authorityService.addAuthority(authorityDto);

        return ResultUtil.success();
    }

    /**
     * 停止权限继承
     * @param id
     * @return
     */
    @PostMapping("/{id}/stopAuthInherit")
    public Result authStopInherit(@PathVariable Integer id) {
        authorityService.stopAuthInherit(id);
        return ResultUtil.success();
    }

    /**
     * 开启权限继承
     * @param id
     * @return
     */
    @PostMapping("/{id}/startAuthInherit")
    public Result startStopInherit(@PathVariable Integer id) {
        authorityService.startAuthInherit(id);
        return ResultUtil.success();
    }


}
