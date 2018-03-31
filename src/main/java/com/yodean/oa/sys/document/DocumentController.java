package com.yodean.oa.sys.document;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.CategoryEnum;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.plugin.document.service.DocumentService;
import com.yodean.oa.common.util.ResultUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
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
     * @param id
     * @return
     * @throws IOException
     */
    @PostMapping("/{category}/{id}/upload")
    public Result<List<Document> > upload(MultipartHttpServletRequest multipartRequest, @PathVariable CategoryEnum category, @PathVariable Integer id) throws IOException {
        List<MultipartFile> files = multipartRequest.getFiles(UPLOAD_NAME);

        List<Document> documents = new ArrayList<>(files.size());
        for (MultipartFile file : files) {
            documents.add(documentService.upload(UPLOAD_PATH, file, category, id));
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
    public Result<List<Document> > upload2(MultipartHttpServletRequest multipartRequest, @PathVariable CategoryEnum category, @PathVariable Integer id) throws IOException {
        return upload(multipartRequest, category, id);
    }
}
