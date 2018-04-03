package com.yodean.oa.sys.user.service;

import com.yodean.oa.common.enums.ResultType;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.common.service.BaseService;
import com.yodean.oa.sys.org.entity.Organization;
import com.yodean.oa.sys.org.service.OrganizationService;
import com.yodean.oa.sys.user.dao.UserRepository;
import com.yodean.oa.sys.user.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by rick on 2018/3/15.
 */
@Service
public class UserService {
    @Resource
    private UserRepository userRepository;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private BaseService baseService;

    @Resource
    private JdbcTemplate jdbcTemplate;


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        Optional<User> optional =userRepository.findById(id);

        if(!optional.isPresent()) {
            throw new OAException(ResultType.NOT_FOUND_ERROR);
        }

        User user =  optional.get();
        //获取组织代码
        String sql = "select org_id from sys_user_org where user_id = :userId";
        Map<String, Object> params = new HashMap<>(1);
        params.put("userId", id);

        List<Integer> list = baseService.query(sql, params, Integer.class);
        user.setOrgIds(new HashSet<>(list));
        return user;
    }


    @Transactional
    public Integer save(User user) {
        User persist = user;

        Set<Organization> organizations = new HashSet<>(user.getOrgIds().size());
        user.getOrgIds().forEach(orgId -> {
            Organization organization = organizationService.findById(orgId);
            organizations.add(organization);
        });
        //添加组织
        persist.setOrganizations(organizations);

        return userRepository.save(persist).getId();
    }

    @Transactional
    public void delete(Integer id) {
        userRepository.deleteLogical(id);

        //取消该用户主管的所有组织
        jdbcTemplate.update("update sys_org set manager_id = null WHERE manager_id = ?", id);
    }


    public void update(User user, Integer id) {
        user.setId(id);
       userRepository.updateNonNull(user);
    }

}
