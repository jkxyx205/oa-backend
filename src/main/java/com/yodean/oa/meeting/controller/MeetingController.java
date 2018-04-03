package com.yodean.oa.meeting.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.meeting.entity.Meeting;
import com.yodean.oa.meeting.service.MeetingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Created by rick on 3/27/18.
 */
@RestController
@RequestMapping("/meeting")
public class MeetingController {

    @Resource
    private MeetingService meetingService;

    @PostMapping
    public Result<Meeting> save(@RequestHeader Integer test2, @RequestParam Integer[] test1) {
//        return ResultUtil.success(meetingService.save(meeting));

        return ResultUtil.success();
    }

    @GetMapping("/{id}")
    public Result<Meeting> findById(@PathVariable Integer id) {
        return ResultUtil.success(meetingService.findById(id));
    }

}