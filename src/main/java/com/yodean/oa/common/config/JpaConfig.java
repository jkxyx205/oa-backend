package com.yodean.oa.common.config;

import com.yodean.oa.common.dao.ExtendedRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by rick on 3/29/18.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.yodean.oa",
        repositoryBaseClass = ExtendedRepositoryImpl.class)
public class JpaConfig {
}
