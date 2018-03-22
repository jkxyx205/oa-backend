package com.yodean.oa.common.plugin.document.service;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.CategoryEnum;
import com.yodean.oa.common.exception.OANoSuchElementException;
import com.yodean.oa.common.plugin.document.dao.DocumentRepository;
import com.yodean.oa.common.plugin.document.dto.ImageDocument;
import com.yodean.oa.common.plugin.document.entity.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

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
}
