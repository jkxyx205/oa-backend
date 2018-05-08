package com.yodean.oa.meeting.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.Category;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.meeting.entity.Meeting;
import com.yodean.oa.meeting.service.MeetingService;
import com.yodean.oa.sys.workspace.entity.Workspace;
import com.yodean.oa.sys.workspace.service.WorkspaceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by rick on 3/27/18.
 */
@RestController
@RequestMapping("/meetings")
public class MeetingController {

    @Resource
    private MeetingService meetingService;

    @Resource
    private WorkspaceService workspaceService;

    @PostMapping
    public Result<Integer> save(@RequestBody Meeting meeting) {
        return ResultUtil.success(meetingService.save(meeting));
    }

    @GetMapping("/{id}")
    public Result<Meeting> findById(@PathVariable Integer id) {
        return ResultUtil.success(meetingService.findById(id));
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody Meeting meeting) {
        meetingService.update(meeting, id);
        return ResultUtil.success();
    }

    /***
     * 取消会议
     * @param id
     * @return
     */
    @PutMapping("/{id}/cancel")
    public Result cancel(@PathVariable Integer id) {
        meetingService.cancel(id);
        return ResultUtil.success();
    }

    /***
     * 添加参与者
     * @return
     */
    @PostMapping("{id}/users")
    public Result addUser(@PathVariable Integer id, @RequestBody Map<Integer, Workspace.UserType> userMap) {
//        workspaceService.tipUsers(Category.MEETING, id, userMap);
        return ResultUtil.success();
    }

    /***
     * 移除参与者
     * @return
     */
    @DeleteMapping("{id}/users/{userId}")
    public Result removeUser(@PathVariable Integer id, @PathVariable Integer userId) {
        workspaceService.remove(Category.MEETING, id, userId);
        return ResultUtil.success();
    }

}
