package com.yodean.oa.common.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by rick on 3/29/18.
 */
@NoRepositoryBean
public interface ExtendedRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    void updateNonNull(T t);

    void deleteLogical(ID ...id);
}
