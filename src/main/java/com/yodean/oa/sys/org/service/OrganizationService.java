package com.yodean.oa.sys.org.service;

import com.yodean.oa.sys.org.dao.OrganizationRepository;
import com.yodean.oa.sys.org.entity.Organization;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by rick on 2018/3/23.
 */
@Service
public class OrganizationService {

    @Resource
    private OrganizationRepository organizationRepository;

    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }

    public void deleteById(Integer id) {
        organizationRepository.deleteById(id);
    }

    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

}
