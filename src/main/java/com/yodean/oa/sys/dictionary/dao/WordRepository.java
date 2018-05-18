package com.yodean.oa.sys.dictionary.dao;

import com.yodean.oa.common.dao.ExtendedRepository;
import com.yodean.oa.sys.dictionary.entity.Word;

import java.util.List;

/**
 * Created by rick on 5/17/18.
 */
public interface WordRepository extends ExtendedRepository<Word, Integer> {

    List<Word> findByCategoryOrderBySeqAsc(String category);
}
