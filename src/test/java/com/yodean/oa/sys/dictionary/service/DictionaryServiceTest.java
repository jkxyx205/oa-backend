package com.yodean.oa.sys.dictionary.service;

import com.yodean.oa.sys.dictionary.entity.Word;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by rick on 5/17/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DictionaryServiceTest {

    @Autowired
    private DictionaryService dictionaryService;


    @Test
    public void save() throws Exception {
        Word word = new Word();
        word.setCategory("gender");
        word.setDescription("female2");
        word.setName("02");
        dictionaryService.save(word);
    }

    @Test
    public void update() throws Exception {
        Word word = new Word();
        word.setId(1);
        word.setCategory("VEHICLE_BRAND");
        word.setName("NS");
        word.setDescription("尼桑水饺");
        dictionaryService.update(word, 1);
    }

    @Test
    public void delete() throws Exception {
        dictionaryService.delete(1);
    }

    @Test
    public void findByCategory() throws Exception {
        List<Word> list = dictionaryService.findByCategory("VEHICLE_BRAND");
        System.out.println(list);
    }


    @Test
    public void findByCategoryAndName() throws Exception {
        Word word = dictionaryService.findByCategoryAndName("VEHICLE_BRAND", "NS");
        System.out.println(word);
    }


}