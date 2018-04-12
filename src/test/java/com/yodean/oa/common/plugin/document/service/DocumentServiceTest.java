package com.yodean.oa.common.plugin.document.service;

import com.yodean.oa.common.enums.Category;
import com.yodean.oa.common.enums.DocumentCategory;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.plugin.document.enums.FileType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Created by rick on 2018/3/22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DocumentServiceTest {

    @Resource
    private DocumentService documentService;

    @Resource
    private ImageHandler imageHandler;

    @Test
    public void save() throws Exception {
        Document doc = new Document();
        doc.setName("中国万岁");
        doc.setCategory(DocumentCategory.TASK);
        doc.setCategoryId(100);
        doc.setRemarks("重要文件");
        doc.setContentType("image/jpeg");
        doc.setExt("jpg");
        doc.setFileType(FileType.FILE);
        doc.setPath("/task");
        doc.setSize(1000l);

        doc = documentService.save(doc);
        Assert.assertNotNull(doc.getId());

    }

    @Test
    public void deleteById() throws Exception {
//        documentService.deleteById(25);
    }

//    public void testLoaclImage() {
//        File file = new File();
//
//        CommonsMultipartFile multipartFile = new CommonsMultipartFile(new DiskFileItem(
//                "", "", false, file.getName(),(int)file.length(), file
//        ) {});
////        StandardMultipartFile standardMultipartFile = new StandardMultipartFile();
//    }

}