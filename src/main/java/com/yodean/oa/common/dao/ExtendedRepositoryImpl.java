package com.yodean.oa.common.dao;

import com.yodean.oa.common.config.Global;
import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.common.exception.OANoSuchElementException;
import com.yodean.oa.common.util.EntityBeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by rick on 3/29/18.
 */
public class ExtendedRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements ExtendedRepository<T, ID> {

    private EntityManager entityManager;

    public ExtendedRepositoryImpl(JpaEntityInformation<T, ?>
                                          entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }


    /**
     * 单实体操作，不做级联更新操作，忽略为null的属性
     *
     * @param t
     */
    @Transactional
    public <S extends T> S update(S t) {
        return update(t, false);
    }

    /**
     * 做级联更新操作
     *
     * @param t
     */
    @Transactional
    public <S extends T> S updateCascade(S t) {
        return update(t, true);
    }


    private <S extends T> S update(S t, boolean deep) {
        S persist;
        try {
            ID id = (ID) PropertyUtils.getProperty(t, Global.ENTITY_ID);
            persist = (S) load(id);
            EntityBeanUtils.merge(persist, t, deep);
            save(persist);
        } catch (OANoSuchElementException e) {
            throw new OAException(ResultCode.NOT_FOUND_ERROR, e);
        } catch (Exception e) {
            throw new OAException(ResultCode.UNKNOW_ERROR, e);
        }

        return persist;

    }


    @Transactional
    public List<T> deleteLogical(ID... ids) {
        if (Objects.isNull(ids)) throw new OAException(ResultCode.NULL_ERROR);

        List<T> list = findAllById(Arrays.asList(ids));
        for (T t : list) {
            ((DataEntity) t).setDelFlag(DataEntity.DEL_FLAG_REMOVE);
        }
        saveAll(list);

        return list;
    }


    public List<T> findAllNormal() {
        Class<T> tClass = this.getDomainClass();
        T t;
        try {
            t = tClass.newInstance();
            PropertyUtils.setProperty(t, Global.ENTITY_DEL_FLAG, DataEntity.DEL_FLAG_NORMAL);
        } catch (Exception e) {
            throw new OAException(ResultCode.SERVER_ERROR);
        }

        Example<T> example = Example.of(t);
        return this.findAll(example);
    }

    @Override
    public T load(ID id) {
        Optional<T> optional = findById(id);
        if (optional.isPresent())
            return optional.get();

        throw new OAException(ResultCode.NOT_FOUND_ERROR);
    }

    @Override
    public T get(ID id) {
        Optional<T> optional = findById(id);
        if (optional.isPresent())
            return optional.get();

        return null;
    }
}

