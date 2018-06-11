package com.yodean.oa.property.meetingroom.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtils;
import com.yodean.oa.property.meetingroom.entity.MeetingRoom;
import com.yodean.oa.property.meetingroom.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rick on 5/18/18.
 */
@RestController
@RequestMapping("/meeting-rooms")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;

    /**
     * 添加会议室
     *
     * @param meetingRoom
     * @return
     */
    @PostMapping
    public Result<MeetingRoom> save(@RequestBody MeetingRoom meetingRoom) {
        meetingRoom = meetingRoomService.save(meetingRoom);
        return ResultUtils.success(meetingRoom);
    }

    /**
     * 更新会议室
     *
     * @param meetingRoom
     * @return
     */
    @PutMapping("/{id}")
    public Result<MeetingRoom> update(@RequestBody MeetingRoom meetingRoom, @PathVariable Integer id) {
        meetingRoom = meetingRoomService.update(meetingRoom, id);
        return ResultUtils.success(meetingRoom);
    }

    /**
     * 删除会议室
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Integer> delete(@PathVariable Integer id) {
        return ResultUtils.success(meetingRoomService.delete(id));
    }

    /**
     * 根据Id查找会议室
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<MeetingRoom> findById(@PathVariable Integer id) {
        return ResultUtils.success(meetingRoomService.findById(id));
    }

    /**
     * @param kw
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public Result<Page<MeetingRoom>> list(@RequestParam(defaultValue = "") String kw, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        return ResultUtils.success(meetingRoomService.list(kw, page, size));
    }
}
