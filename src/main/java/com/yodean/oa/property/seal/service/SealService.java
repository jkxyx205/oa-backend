package com.yodean.oa.property.seal.service;

import com.yodean.oa.property.seal.dao.SealRepository;
import com.yodean.oa.property.seal.entity.Seal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rick on 6/1/18.
 */
@Service
public class SealService {
    @Autowired
    private SealRepository sealRepository;


    /**
     * 保存印章
     * @param seal
     * @return
     */
    public Integer save(Seal seal) {
        sealRepository.save(seal);
        return seal.getId();
    }

    /**
     * 编辑印章
     * @param seal
     * @param id
     */
    public void update(Seal seal, Integer id) {
        seal.setId(id);
        sealRepository.update(seal);
    }

    /**
     * 删除印章
     * @param id
     * @return
     */
    public int delete(Integer id) {
        List list = sealRepository.deleteLogical(id);
        return list.size();
    }
}
