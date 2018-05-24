package com.yodean.oa.property.material.service;

import com.yodean.oa.property.material.dao.StorageRepository;
import com.yodean.oa.property.material.entity.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rick on 5/24/18.
 */
@Service
public class StorageService {

    @Autowired
    private StorageRepository storageRepository;

    /**
     * 添加库位
     *
     * @param storage
     */
    public Integer save(Storage storage) {
        storageRepository.save(storage);
        return storage.getId();
    }

    /**
     * 根据Id查找
     *
     * @param id
     * @return
     */
    public Storage findById(Integer id) {
        return storageRepository.get(id);
    }

}
