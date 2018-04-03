package com.yodean.oa.common.dao;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.ResultType;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.common.exception.OANoSuchElementException;
import com.yodean.oa.common.util.NullAwareBeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
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

    private static final String ENTITY_ID = "id";

    private EntityManager entityManager;

    public ExtendedRepositoryImpl(JpaEntityInformation<T, ?>
                                          entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    public void updateNonNull(T t) {
        try {
            ID id = (ID)PropertyUtils.getProperty(t, ENTITY_ID);
            Optional<T> optional = findById(id);
            if (!optional.isPresent()) throw new OANoSuchElementException();

            T persist = optional.get();
            NullAwareBeanUtilsBean.getInstance().copyProperties(persist, t);
            save(persist);
        } catch (Exception e) {
            throw new OAException(ResultType.UNKNOW_ERROR);
        }
    }

    @Transactional
    public void deleteLogical(ID ...ids) {
        if (Objects.isNull(ids)) throw new OAException(ResultType.NULL_ERROR);

        List<T> list = findAllById(Arrays.asList(ids));
        for(T t : list) {
            ((DataEntity)t).setDelFlag(DataEntity.DEL_FLAG_REMOVE);
        }
        saveAll(list);
    }
}

