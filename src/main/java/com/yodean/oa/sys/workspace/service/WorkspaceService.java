package com.yodean.oa.sys.workspace.service;

import com.yodean.oa.common.enums.Category;
import com.yodean.oa.sys.util.UserUtils;
import com.yodean.oa.sys.workspace.dao.WorkspaceRepository;
import com.yodean.oa.sys.workspace.entity.Workspace;
import com.yodean.oa.sys.workspace.enums.CategoryStatus;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by rick on 3/27/18.
 */
@Service
public class WorkspaceService {

    @Resource
    private WorkspaceRepository workspaceRepository;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private final static String TIP_SQL = "UPDATE sys_workspace w SET w.category_status = 'INBOX', w.readed = 0 WHERE w.category = ? AND w.category_id = ?";


    /***
     * 获取当前登陆用户workspace
     * 根据AuthorityType.USER查找
     * @param category
     * @param categoryId
     * @return
     */
    private Workspace findMyWorkspaceByUserType(Category category, Integer categoryId) {
        Workspace workspace = new Workspace();
        workspace.setCategory(category);
        workspace.setCategoryId(categoryId);
        workspace.setAuthorityType(Workspace.AuthorityType.USER);
        workspace.setAuthorityId(UserUtils.getUser().getId());

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
        Workspace workspace = findMyWorkspaceByUserType(category, categoryId);
        workspace.setCategoryStatus(categoryStatus);
        workspaceRepository.save(workspace);
    }


    /***
     * 标记已读/未读
     * @param category
     * @param categoryId
     * @param isRead
     */
    public void markRead(Category category, Integer categoryId, boolean isRead) {
        Workspace workspace = findMyWorkspaceByUserType(category, categoryId);
        workspace.setReaded(isRead);
        workspaceRepository.save(workspace);
    }


    /***
     * 跟进
     * @param isFollow
     */
    public void markFollow(Category category, Integer categoryId, boolean isFollow) {
        Workspace workspace = findMyWorkspaceByUserType(category, categoryId);
        workspace.setFollow(isFollow);
        workspaceRepository.save(workspace);
    }

    /**
     * 通知所有用户
     * @param category
     * @param id
     */
    public void tip(Category category, Integer id) {
        jdbcTemplate.update(TIP_SQL, category.name(), id);
    }
}
