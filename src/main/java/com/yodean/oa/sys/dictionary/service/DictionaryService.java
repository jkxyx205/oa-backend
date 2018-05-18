package com.yodean.oa.sys.dictionary.service;

import com.yodean.oa.common.service.SharpService;
import com.yodean.oa.sys.dictionary.dao.WordRepository;
import com.yodean.oa.sys.dictionary.entity.Word;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by rick on 5/17/18.
 */
@Service
public class DictionaryService {

//    private static final String KEY = "dictionary";

    private static final String QUERY_SQL = "select category, name, description, remark from sys_dictionary where category = :category and name = :name";

    @Resource
    private SharpService sharpService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WordRepository wordRepository;

    private ValueOperations<String, String> vo;

    @PostConstruct
    public void init() {
        vo = stringRedisTemplate.opsForValue();
    }

    @CacheEvict(value = "dictionary", key="#word.category")
    public Word save(Word word) {
        return wordRepository.save(word);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "dictionary", key="#word.category"),
            @CacheEvict(cacheNames = "dictionary", key="#word.category + '::' +  #word.name")
    })
    public Word update(Word word, Integer id) {
        word.setId(id);
       return wordRepository.update(word);
    }

    @CacheEvict(value = "dictionary", allEntries = true)
    public void delete(Integer id) {
        wordRepository.deleteLogical(id);
    }

    @Cacheable(value = "dictionary")
    public List<Word> findByCategory(String category) {
        return wordRepository.findByCategoryOrderBySeqAsc(category);
    }

    @Cacheable(value = "dictionary", key="#category + '::' +  #name")
    public Word findByCategoryAndName(String category, String name) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("category", category);
        params.put("name", name);
        List<Word> list = sharpService.query(QUERY_SQL, params, Word.class);
        if (CollectionUtils.isEmpty(list)) return null;

        return list.get(0);
    }

}
