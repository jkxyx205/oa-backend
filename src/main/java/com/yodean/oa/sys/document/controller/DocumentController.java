package com.yodean.oa.sys.document.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.DocumentCategory;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.sys.document.dto.AuthorityDto;
import com.yodean.oa.sys.document.service.AuthorityService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by rick on 4/17/18.
 */
@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Resource
    private AuthorityService authorityService;

    private static final String UPLOAD_NAME = "upload";

    private static final String UPLOAD_PATH = "document";

    /***
     * 上传附件
     * @param multipartRequest
     * @param categoryId
     * @return
     * @throws IOException
     */
    @PostMapping("/{category}/{categoryId}/upload")
    public Result<List<Document>> upload(MultipartHttpServletRequest multipartRequest, @PathVariable DocumentCategory category, @PathVariable Integer categoryId, Integer parentId) throws IOException {
        List<MultipartFile> files = multipartRequest.getFiles(UPLOAD_NAME);
        return ResultUtil.success(authorityService.upload(files, UPLOAD_PATH, parentId, category, categoryId));
    }

    /***
     * 上传附件
     * @param multipartRequest
     * @return
     * @throws IOException
     */
    @PostMapping("/{category}/upload")
    public Result<List<Document> > upload2(MultipartHttpServletRequest multipartRequest, @PathVariable DocumentCategory category, Integer parentId) throws IOException {
        return upload(multipartRequest, category, null, parentId);
    }

    /**
     * 创建文件夹
     * @param document
     * @return
     */
    @PostMapping("/{category}/mkdir")
    public Result<Integer> addFolder(@RequestBody Document document, @PathVariable DocumentCategory category) {
        Integer docId = authorityService.mkdir(document.getParentId(), document.getName(), category);
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
     * 还原
     * @return
     */
    @PostMapping("/{id}/putBack")
    public Result putBack(@PathVariable Integer id) {
        authorityService.putBack(id);
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
    @GetMapping(value = "/{id}/download")
    public void download(HttpServletResponse response, HttpServletRequest request, @PathVariable Integer id) throws IOException {
        authorityService.download(request, response, id);
    }

    /**
     * 批量下载文件
     * @param ids
     * @return
     */
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, HttpServletRequest request, @RequestParam(name = "id") Integer[] ids) throws IOException {
        authorityService.download(request, response, ids);
    }

    /**
     * 预览文件
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/{id}/view")
    public void view(HttpServletResponse response, HttpServletRequest request, @PathVariable Integer id) throws IOException {
        authorityService.view(request, response, id);
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
