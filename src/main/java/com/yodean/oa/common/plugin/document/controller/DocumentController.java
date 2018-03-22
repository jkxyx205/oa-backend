package com.yodean.oa.common.plugin.document.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.CategoryEnum;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.plugin.document.service.DocumentService;
import com.yodean.oa.common.util.ResultUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 2018/3/22.
 */
@RestController
@RequestMapping("/documents")
public class DocumentController {
    private static final String UPLOAD_NAME = "upload";

    private static final String UPLOAD_PATH = "doc";

    @Resource
    private DocumentService documentService;

    @GetMapping("/{id}")
    public  Result<Document> getDocumentById(@PathVariable Integer id) {
        return ResultUtil.success(documentService.findById(id));
    }

    @PostMapping
    public Result<Document> upload(MultipartHttpServletRequest multipartRequest) throws IOException {
        List<MultipartFile> files = multipartRequest.getFiles(UPLOAD_NAME);

        List<Document> documents = new ArrayList<>(files.size());
        for (MultipartFile file : files) {
            documents.add(documentService.upload(UPLOAD_PATH, file, CategoryEnum.TASK, 2));
        }

        return ResultUtil.success(documents);
    }

    /***
     * 测试使用！！！
     * @param id
     * @return
     * @throws IOException
     */
    @PostMapping("/{id}/crop")
    public Result<List<Document>> crop(@PathVariable Integer id) throws IOException {

        List<Document> list = new ArrayList<>(2);

        list.add(documentService.cropAuto("crop", id, 1, 1));
        list.add(documentService.cropManual("crop", id, 0, 0,  100, 200,9, 5));
        return ResultUtil.success(list);

    }
}
