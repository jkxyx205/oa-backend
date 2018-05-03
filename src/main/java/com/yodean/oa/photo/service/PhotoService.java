package com.yodean.oa.photo.service;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.DocumentCategory;
import com.yodean.oa.common.plugin.document.dto.ImageDocument;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.plugin.document.service.DocumentService;
import com.yodean.oa.photo.dao.PhotoRepository;
import com.yodean.oa.photo.entity.Photo;
import com.yodean.oa.sys.document.service.AuthorityService;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.mapping.Collection;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rick on 4/27/18.
 */
@Service
public class PhotoService {

    @Resource
    private PhotoRepository photoRepository;

    @Resource
    private AuthorityService authorityService;

    @Resource
    private DocumentService documentService;

    @Transactional
    public Photo save(Photo photo, List<MultipartFile> files) {
        photo.setDocumentList(authorityService.upload(files, "photo", null, DocumentCategory.NOTE, photo.getId()));
        photo.setDocumentList(wrapper2ImageDocument(photo.getDocumentList()));
        photoRepository.save(photo);
        return photo;
    }

    public List<Photo> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Photo _ex = new Photo();
        _ex.setDelFlag(DataEntity.DEL_FLAG_NORMAL);
        Example<Photo> example = Example.of(_ex);

        List<Photo> photos = photoRepository.findAll(example, sort);

        photos.forEach(photo -> {
            photo.setDocumentList(wrapper2ImageDocument(documentService.findById(DocumentCategory.NOTE, photo.getId())));
        });
        return photos;
    }

    private List<Document> wrapper2ImageDocument(List<Document> list) {
        if (CollectionUtils.isEmpty(list)) return Collections.emptyList();

        List<Document> imageList = new ArrayList<>(list.size());

        list.forEach(document -> {
            imageList.add(new ImageDocument(document));
        });

        return imageList;
    }

    public void delete(Integer id) {
        photoRepository.deleteLogical(id);
    }
}
