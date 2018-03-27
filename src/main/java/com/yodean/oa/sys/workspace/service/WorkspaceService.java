package com.yodean.oa.sys.workspace.service;

import com.yodean.oa.common.enums.CategoryEnum;
import com.yodean.oa.common.service.BaseService;
import com.yodean.oa.sys.user.entity.User;
import com.yodean.oa.sys.util.UserUtils;
import com.yodean.oa.sys.workspace.dao.WorkspaceRepository;
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

    @Transactional
    public void save(CategoryEnum category, Integer categoryId, Set<Integer> userIds) {
        //delete
        delete(category, categoryId);
        //insert
        List<Workspace> workspaceList = new ArrayList<>(userIds.size());

        userIds.forEach(userId -> {
            Workspace workspace = new Workspace();
            workspace.setCategory(category);
            workspace.setCategoryId(categoryId);
            workspace.setCategoryStatus(CategoryStatus.INBOX);
            workspace.setFollowUp(false);
            workspace.setUnread(true);
            workspace.setUserId(userId);
            workspaceList.add(workspace);
        });

        workspaceRepository.saveAll(workspaceList);
    }



    @Transactional
    public void save(CategoryEnum category, Integer categoryId, Integer ...userIds) {
        save(category, categoryId, new HashSet<>(Arrays.asList(userIds)));
    }

    public void delete(CategoryEnum category, Integer categoryId) {
        //delete
        String sql = "DELETE FROM sys_workspace\n" +
                "WHERE\n" +
                "\tcategory = ?\n" +
                "AND category_id = ?\n";

        jdbcTemplate.update(sql, category.name(), categoryId);
    }


    /***
     * 文件移动
     * @param category
     * @param categoryId
     */
    public void move(CategoryEnum category, Integer categoryId, CategoryStatus categoryStatus) {
        Workspace workspace = new Workspace();
        workspace.setCategory(category);
        workspace.setCategoryId(categoryId);
        workspace.setUserId(UserUtils.getUser().getId());

        Example<Workspace> example = Example.of(workspace);
        List<Workspace> list = workspaceRepository.findAll(example);
        if (CollectionUtils.isNotEmpty(list)) {
            workspace = list.get(0);
            workspace.setCategoryStatus(categoryStatus);
            workspaceRepository.save(workspace);
        }

    }

    /***
     * 查找所有相关者
     * @param category
     * @param categoryId
     * @return
     */
    public List<User> findUsers(CategoryEnum category, Integer categoryId) {
        String sql = "select id, chinese_name chineseName from sys_user su\n" +
                "where exists(\n" +
                "select 1 from sys_workspace sp where  sp.category = :category and sp.category_id = :categoryId and \n" +
                "su.id = sp.user_id\n" +
                ")";
        Map<String, Object> params = new HashMap<>(2);
        params.put("category", category.name());
        params.put("categoryId",categoryId);

       return baseService.query(sql, params, User.class);
    }

}
