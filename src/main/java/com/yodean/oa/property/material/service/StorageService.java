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
     * 编辑库位
     * @param storage
     * @param id
     */
    public void update(Storage storage, Integer id) {
        storage.setId(id);
        storageRepository.update(storage);
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

    /**
     * 切换状态
     */
    public void switchStatus(Integer id, Character status) {
        Storage storage = storageRepository.load(id);
        storage.setStatus(status);
        save(storage);
    }

    /**
     * 删除
     * @param id
     */
    public void delete(Integer id) {
        storageRepository.deleteLogical(id);
    }
}
