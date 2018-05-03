package com.yodean.oa.photo.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.photo.entity.Photo;
import com.yodean.oa.photo.service.PhotoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by rick on 4/27/18.
 */
@RestController
@RequestMapping("/photos")
public class PhotoController {

    @Resource
    private PhotoService photoService;


    @PostMapping
    public Result send(Photo photo, MultipartHttpServletRequest multipartRequest) {
        List<MultipartFile> files = multipartRequest.getFiles("upload");
        photoService.save(photo, files);

        return ResultUtil.success();
    }

    @GetMapping
    public Result<List<Photo>> list() {
        return ResultUtil.success(photoService.list());
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        photoService.delete(id);
        return ResultUtil.success();
    }
}
