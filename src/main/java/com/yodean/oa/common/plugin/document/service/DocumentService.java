package com.yodean.oa.common.plugin.document.service;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.CategoryEnum;
import com.yodean.oa.common.exception.OANoSuchElementException;
import com.yodean.oa.common.plugin.document.dao.DocumentRepository;
import com.yodean.oa.common.plugin.document.dto.ImageDocument;
import com.yodean.oa.common.plugin.document.entity.Document;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by rick on 2018/3/22.
 */
@Service
public class DocumentService {

    @Resource
    private DocumentRepository documentRepository;

    @Resource
    private DocumentHandler documentHandler;

    @Resource
    private ImageHandler imageHandler;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public Document upload(String folderPath, MultipartFile file, CategoryEnum categoryEnum, Integer Id) throws IOException {
        //上传到服务器
        Document document = documentHandler.store(folderPath, file);
        document.setCategoryEnum(categoryEnum);
        document.setCategoryId(Id);
        document = save(document);
        return document;
    }

    /***
     * 自动裁剪
     * @param folder
     * @param id
     * @param aspectRatioW
     * @param aspectRatioH
     * @return
     * @throws IOException
     */
    public Document cropAuto(String folder, Integer id, Integer aspectRatioW, Integer aspectRatioH) throws IOException {
        Document document = findById(id);
        ImageDocument image = new ImageDocument();
        BeanUtils.copyProperties(document, image);

        return imageHandler.cropPic(folder, image, aspectRatioW, aspectRatioH);
    }


    /***
     * 手动裁剪
      * @param folder
     * @param id
     * @param x
     * @param y
     * @param w
     * @param h
     * @param aspectRatioW
     * @param aspectRatioH
     * @return
     * @throws IOException
     */
    public Document cropManual(String folder, Integer id,
                         Integer x, Integer y, Integer w, Integer h,
                         Integer aspectRatioW, Integer aspectRatioH) throws IOException {
        Document document = findById(id);
        ImageDocument image = new ImageDocument();
        BeanUtils.copyProperties(document, image);

        return imageHandler.cropPic(folder, image, x, y, w, h, aspectRatioW, aspectRatioH);

    }

    public Document save(Document document) {
        return documentRepository.save(document);
    }

    public Document findById(Integer id) {
        Optional<Document> optional = documentRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }

        throw new OANoSuchElementException();
    }

    /***
     * 逻辑删除
     * @param id
     */
    public void deleteById(Integer id) {
        Document document = findById(id);
        document.setDelFlag(DataEntity.DEL_FLAG_REMOVE);
        save(document);

//        documentRepository.deleteById(id);
    }

    /***
     * 绑定附件到实例上
     * @param docIds
     * @param categoryId
     */
    public void update(Set<Integer> docIds, Integer categoryId) {
        Validate.notNull(categoryId);
        Validate.notNull(docIds);

        if (docIds.size() == 0) {
            return;
        }

        String sql = "UPDATE sys_document set category_id = ? WHERE id = ?";

        List<Object[]> params = new ArrayList<>(docIds.size());
        for (Integer docId : docIds) {
            params.add(new Object[]{categoryId, docId});
        }

        jdbcTemplate.batchUpdate(sql, params);
    }

    public List<Document> findById(CategoryEnum category, Integer categoryId) {
        Document document = new Document();
        document.setCategoryEnum(category);
        document.setCategoryId(categoryId);
        document.setDelFlag(DataEntity.DEL_FLAG_NORMAL);

        Example example = Example.of(document);
        return documentRepository.findAll(example);
    }
}
