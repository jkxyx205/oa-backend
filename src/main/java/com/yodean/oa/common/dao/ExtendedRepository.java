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
     * 父实体，只更新不为null的字段，t为非瞬时状态
     * @param t
     */
    <S extends T> S update(S t);

    /**
     * 级联更新所有,只更新不为null的字段，t为非瞬时状态
     * @param t
     */
    <S extends T> S updateCascade(S t);

    /**
     * 逻辑删除
     * @param ids
     */
    List<T> deleteLogical(ID ...ids);

    /***
     * 查找所有未被删除的纪录
     * @return
     */
    List<T> findAllNormal();

    /**
     * 如果未能发现符合条件的记录，load方法会抛出一个ObjectNotFoundException
     * @param id
     * @return
     */
    T load(ID id);

    /**
     * 如果未能发现符合条件的记录，get方法返回null
     * @param id
     * @return
     */
    T get(ID id);
}
