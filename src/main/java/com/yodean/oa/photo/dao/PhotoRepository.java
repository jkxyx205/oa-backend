package com.yodean.oa.photo.dao;

import com.yodean.oa.common.dao.ExtendedRepository;
import com.yodean.oa.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by rick on 4/27/18.
 */
public interface PhotoRepository extends ExtendedRepository<Photo, Integer> {

}