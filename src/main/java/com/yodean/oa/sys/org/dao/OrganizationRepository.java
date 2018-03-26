package com.yodean.oa.sys.org.dao;

import com.yodean.oa.sys.org.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by rick on 2018/3/23.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
}
