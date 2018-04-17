package com.yodean.oa.sys.document.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.Category;
import com.yodean.oa.common.enums.DocumentCategory;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.plugin.document.service.DocumentService;
import com.yodean.oa.common.util.ResultUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 3/30/18.
 */
@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Resource
    private DocumentService documentService;

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
    public Result<List<Document> > upload(MultipartHttpServletRequest multipartRequest, @PathVariable DocumentCategory category, @PathVariable Integer categoryId) throws IOException {
        List<MultipartFile> files = multipartRequest.getFiles(UPLOAD_NAME);

        List<Document> documents = new ArrayList<>(files.size());
        for (MultipartFile file : files) {
            documents.add(documentService.upload(UPLOAD_PATH, file, category, categoryId));
        }

        return ResultUtil.success(documents);
    }

    /***
     * 上传附件
     * @param multipartRequest
     * @return
     * @throws IOException
     */
    @PostMapping("/{category}/upload")
    public Result<List<Document> > upload2(MultipartHttpServletRequest multipartRequest, @PathVariable DocumentCategory category) throws IOException {
        return upload(multipartRequest, category, null);
    }

    /***
     * 下载附件
     */
    @GetMapping("/{id}/download")
    public void download(@PathVariable Integer id, HttpServletResponse response, HttpServletRequest request) throws IOException {
        documentService.download(response, request, id);
    }

    /***
     * 删除附件
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        documentService.delete(id);
        return ResultUtil.success();
    }
}
