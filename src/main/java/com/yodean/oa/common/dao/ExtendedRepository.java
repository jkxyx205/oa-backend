package com.yodean.oa.common.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rick on 3/29/18.
 */
@NoRepositoryBean
public interface ExtendedRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    /**
     * 只更新不为null的字段
     * @param t
     */
    void updateNonNull(T t);

    /**
     * 逻辑删除
     * @param ids
     */
    void deleteLogical(ID ...ids);

    /***
     * 查找所有未被删除的纪录
     * @return
     */
    List<T> findAllNormal();
}
