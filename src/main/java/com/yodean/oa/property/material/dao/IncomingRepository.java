package com.yodean.oa.property.material.dao;

import com.yodean.oa.common.dao.ExtendedRepository;
import com.yodean.oa.property.material.entity.Incoming;

/**
 * Created by rick on 5/29/18.
 */
public interface IncomingRepository extends ExtendedRepository<Incoming, Integer> {
    public Incoming findBySno(String sno);

    public Incoming findByBatchNum(String batchNum);
}
