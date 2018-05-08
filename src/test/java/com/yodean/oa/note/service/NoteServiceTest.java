package com.yodean.oa.note.service;

import com.yodean.oa.note.entity.Note;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.label.entity.Label2;
import com.yodean.oa.sys.label.enums.ColorEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by rick on 5/7/18.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteServiceTest {
    @Autowired
    private NoteService noteService;
    @Test
    public void save() throws Exception {
        Note note = new Note();

        note.setTitle("记好笔记");
        note.setContent("这个文章非常精彩");
//
        Label2 label = new Label2();

        Label2.LabelId  labelId = new Label2.LabelId();
        labelId.setCategoryId(note.getId());
        labelId.setCategory(Label2.LabelCategory.NOTE);

        label.setTitle("重要");

        label.setColor(ColorEnum.COLOR_1);
        label.setLabelId(labelId);

        note.getLabels().add(label);
//

        Label2 label2 = new Label2();

        Label2.LabelId  labelId2 = new Label2.LabelId();
        labelId2.setCategoryId(note.getId());
        labelId2.setCategory(Label2.LabelCategory.NOTE);

        label2.setTitle("必要");

        label2.setColor(ColorEnum.COLOR_1);
        label2.setLabelId(labelId2);



        note.getLabels().add(label2);

        noteService.save(note);

    }

    @Test public void query() {
        Note note = noteService.findById(1);
        System.out.println(note);

    }

    @Test public void buildTableTest() {

    }

}