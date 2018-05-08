package com.yodean.oa.notice.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.notice.entity.Notice;
import com.yodean.oa.notice.service.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by rick on 5/4/18.
 */
@RestController
@RequestMapping("/notices")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @PostMapping
    public Result<Integer> save(@RequestBody Notice notice) {
        noticeService.save(notice);
        return ResultUtil.success(notice.getId());
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable("id") Integer id, @RequestBody Notice notice) {
        notice.setId(id);
        noticeService.update(notice);
        return ResultUtil.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        noticeService.delete(id);
        return ResultUtil.success();
    }

    @GetMapping("/{id}")
    public Result<Notice> findById(@PathVariable Integer id) {
        return ResultUtil.success(noticeService.findById(id));
    }

    @GetMapping
    public Result<Page<Notice>> findAll(String title) {
        return ResultUtil.success(noticeService.findAll2(title));
    }

}
