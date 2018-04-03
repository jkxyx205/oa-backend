package com.yodean.oa.sys.workspace.service;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.Category;
import com.yodean.oa.common.service.BaseService;
import com.yodean.oa.sys.util.UserUtils;
import com.yodean.oa.sys.workspace.dao.WorkspaceRepository;
import com.yodean.oa.sys.workspace.dto.Participant;
import com.yodean.oa.sys.workspace.entity.Workspace;
import com.yodean.oa.sys.workspace.enums.CategoryStatus;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by rick on 3/27/18.
 */
@Service
public class WorkspaceService {

    @Resource
    private WorkspaceRepository workspaceRepository;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private BaseService baseService;

    /***
     * 【插入】参与者
     * @param category
     * @param categoryId
     * @param userMap 需要到inbox的用户ids
     */

    @Transactional
    public void tipUsers(Category category, Integer categoryId, Map<Integer, Workspace.UserType> userMap) {
//      所有已经参加的人员
        Map<String, Object> params = new HashMap<>(2);
        params.put("category", category.name());
        params.put("categoryId", categoryId);
        Map<Integer, Integer>  wkMap = baseService.query("select user_id, id as userId from sys_workspace sp where category = :category and category_id = :categoryId and del_flag = '1'",
                params, new HashMap<Integer, Integer>());

        List<Workspace> workspaceList = new ArrayList<>(userMap.size());

        userMap.keySet().forEach(userId -> {
            Workspace workspace;

            if (wkMap.keySet().contains(userId)) { //存在
                workspace = workspaceRepository.findById(wkMap.get(userId)).get();
            } else {
                workspace = new Workspace();
                workspace.setCategory(category);
                workspace.setCategoryId(categoryId);
                workspace.setFollow(false);
                workspace.setReaded(false);
                workspace.setUserId(userId);
            }
            workspace.setUserType(userMap.get(userId));
            workspace.setCategoryStatus(CategoryStatus.INBOX);

            workspaceList.add(workspace);
        });

        workspaceRepository.saveAll(workspaceList);
    }


    @Transactional
    public void tipUsers(Category category, Integer categoryId, Integer ...userIds) {
        Map<Integer, Workspace.UserType> userMap = new HashMap<>(userIds.length);
        for (Integer userId: userIds) {
            userMap.put(userId,null);
        }

        tipUsers(category, categoryId, userMap);
    }

    /***
     * 移除参与者
     * @param category
     * @param categoryId
     * @param userId
     */
    public void remove(Category category, Integer categoryId, Integer userId) {
        jdbcTemplate.update("UPDATE sys_workspace set del_flag = '0', update_date = ? WHERE category = ? AND category_id = ? AND user_id = ? AND del_flag = '1'",
               new Date(), category.name(), categoryId, userId);
    }

    /***
     * 参与者不变，全部移动到所有参与者的inbox
     * @param category
     * @param id
     */
    public void tipAll(Category category, Integer id) {
        jdbcTemplate.update("UPDATE sys_workspace set category_status = ? WHERE category = ? AND category_id = ? AND del_flag = '1'",
                CategoryStatus.INBOX.name(), category.name(), id);
    }

    /***
     * 跟进
     * @param isFollow
     */
    public void markFollow(Category category, Integer categoryId, boolean isFollow) {
        Workspace workspace = findCurrentWorkspace(category, categoryId);
        workspace.setFollow(isFollow);
        workspaceRepository.save(workspace);
    }

    /***
     * 获取当前登陆用户分类工作空间
     * @param category
     * @param categoryId
     * @return
     */
    private Workspace findCurrentWorkspace(Category category, Integer categoryId) {
        Workspace workspace = new Workspace();
        workspace.setCategory(category);
        workspace.setCategoryId(categoryId);
        workspace.setUserId(UserUtils.getUser().getId());
        workspace.setDelFlag(DataEntity.DEL_FLAG_NORMAL);

        Example<Workspace> example = Example.of(workspace);
        List<Workspace> list = workspaceRepository.findAll(example);
        if (CollectionUtils.isEmpty(list)) {
            throw new NoSuchElementException();
        }
        workspace = list.get(0);
        return workspace;
    }


    /***
     * 移动
     * @param category
     * @param categoryId
     */
    public void move(Category category, Integer categoryId, CategoryStatus categoryStatus) {
        Workspace workspace = findCurrentWorkspace(category, categoryId);
        workspace.setCategoryStatus(categoryStatus);
        workspaceRepository.save(workspace);
    }



    /***
     * 查找参与者
     * @param category
     * @param categoryId
     * @return
     */
    public List<Participant> findUsers(Category category, Integer categoryId) {
        String sql = "select w.category_status categoryStatus, w.follow, w.readed, w.user_type userType, u.id userId, u.chinese_name chineseName from sys_workspace w left join sys_user u on w.user_id = u.id\n" +
                "where category = :category and category_id = :categoryId and w.del_flag = '1'";
        Map<String, Object> params = new HashMap<>(2);
        params.put("category", category.name());
        params.put("categoryId",categoryId);

       return baseService.query(sql, params, Participant.class);
    }

    /***
     * 标记已读/未读
     * @param category
     * @param categoryId
     * @param isRead
     */
    public void markRead(Category category, Integer categoryId, boolean isRead) {
        Workspace workspace = findCurrentWorkspace(category, categoryId);
        workspace.setReaded(isRead);
        workspaceRepository.save(workspace);
    }

}
