package com.yodean.oa.sys.label.dao;

import com.yodean.oa.sys.label.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by rick on 2018/3/20.
 */
public interface LabelRepository extends JpaRepository<Label, Integer> {
}
