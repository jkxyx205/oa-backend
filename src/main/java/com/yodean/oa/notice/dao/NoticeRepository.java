package com.yodean.oa.notice.dao;

import com.yodean.oa.common.dao.ExtendedRepository;
import com.yodean.oa.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by rick on 5/4/18.
 */
public interface NoticeRepository extends ExtendedRepository<Notice, Integer>, JpaSpecificationExecutor<Notice> {
}
