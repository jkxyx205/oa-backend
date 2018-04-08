package com.yodean.oa.sys.org.service;

import com.yodean.oa.common.exception.OANoSuchElementException;
import com.yodean.oa.sys.org.dao.OrganizationRepository;
import com.yodean.oa.sys.org.entity.Organization;
import com.yodean.oa.sys.user.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by rick on 2018/3/23.
 */
@Service
public class OrganizationService {

    @Resource
    private OrganizationRepository organizationRepository;

    @Resource
    private UserService userService;

    @Resource
    private JdbcTemplate jdbcTemplate;

    public Integer save(Organization organization) {
        return organizationRepository.save(organization).getId();
    }

    public void update(Organization organization, Integer id) {
        organization.setId(id);
        organizationRepository.updateNonNull(organization);
    }

    @Transactional
    public void delete(Integer id) {
//        jdbcTemplate.update("UPDATE sys_org SET del_flag = '0' where id = ?", id);

        organizationRepository.deleteLogical(id);
        jdbcTemplate.update("UPDATE sys_user_org SET org_id = null WHERE org_id = ? ", id);
    }

    public List<Organization> findAll() {
        return organizationRepository.findAllNormal();
    }


    public Organization findById(Integer id) {
        Optional<Organization> optional = organizationRepository.findById(id);
        if (optional.isPresent()) {
            Organization organization = optional.get();
            if (!Objects.isNull(organization.getManagerId())) {
                organization.setManager(userService.findById(organization.getManagerId()));
            }

            return organization;
        }

        throw new OANoSuchElementException();
    }
}
