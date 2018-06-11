package com.yodean.oa.sys.workspace.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.Category;
import com.yodean.oa.common.util.ResultUtils;
import com.yodean.oa.sys.workspace.enums.CategoryStatus;
import com.yodean.oa.sys.workspace.service.WorkspaceService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by rick on 3/30/18.
 */
@RestController
@RequestMapping("/workspace")
public class WorkspaceController {

    @Resource
    private WorkspaceService workspaceService;

    /***
     * 本地删除到回收站
     * @return
     */
    @PostMapping("/{category}/{id}/trash")
    public Result trash(@PathVariable Category category, @PathVariable Integer id) {
        workspaceService.move(category, id, CategoryStatus.TRASH);
        return ResultUtils.success();
    }

    /***
     * 移动到待办
     * @return
     */
    @PostMapping("{category}/{id}/inbox")
    public Result inbox(@PathVariable Category category, @PathVariable Integer id) {
        workspaceService.move(category, id, CategoryStatus.INBOX);
        return ResultUtils.success();
    }


    /***
     * 归档
     * @return
     */
    @PostMapping("{category}/{id}/archive")
    public Result archive(@PathVariable Category category, @PathVariable Integer id) {
        workspaceService.move(category, id, CategoryStatus.ARCHIVE);
        return ResultUtils.success();
    }


    /***
     * 从回收站中彻底删除
     * @return
     */
    @PostMapping("{category}/{id}/clean")
    public Result clean(@PathVariable Category category, @PathVariable Integer id) {
        workspaceService.move(category, id, CategoryStatus.CLEAN);
        return ResultUtils.success();
    }

    /***
     * 跟进
     * @return
     */
    @PostMapping("/{category}/{id}/follow")
    public Result follow(@PathVariable Category category, @PathVariable Integer id) {
        workspaceService.markFollow(category, id, true);
        return ResultUtils.success();
    }

    /***
     * 取消跟进
     * @return
     */
    @PostMapping("/{category}/{id}/unfollow")
    public Result unfollow(@PathVariable Category category, @PathVariable Integer id) {
        workspaceService.markFollow(category, id, false);
        return ResultUtils.success();
    }

    /***
     * 标记未读
     * @return
     */
    @PostMapping("/{category}/{id}/unread")
    public Result unread(@PathVariable Category category, @PathVariable Integer id) {
        workspaceService.markRead(category, id, false);
        return ResultUtils.success();
    }
}
