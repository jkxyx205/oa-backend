package com.yodean.oa.property.vehicle.service;

import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.property.vehicle.dao.VehicleRepository;
import com.yodean.oa.property.vehicle.entity.Vehicle;
import com.yodean.oa.sys.dictionary.core.DictionaryUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.Optional;

/**
 * Created by rick on 5/16/18.
 */
@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;



    @CacheEvict(value = "vehicles", key = "#vehicle.id")
    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @CacheEvict(value = "vehicles", key = "#id")
    public Vehicle update(Vehicle vehicle, int id) {
        vehicle.setId(id);
        return vehicleRepository.update(vehicle);
    }

    /**
     * 修改操作状态
     * @param id
     * @param vehicleStatus
     */
    @CacheEvict(value = "vehicles", key = "#id")
    public void changeStatus(Integer id, Vehicle.VehicleStatus vehicleStatus) {
        Vehicle vehicle = findById(id);
        vehicle.setVehicleStatus(vehicleStatus);
        save(vehicle);
    }

    @CacheEvict(value = "vehicles", key = "#id")
    public void delete(Integer id) {
        vehicleRepository.deleteLogical(id);
    }

    @Cacheable(value = "vehicles")
    public Vehicle findById(Integer id) {
        Optional<Vehicle> optional = vehicleRepository.findById(id);
        if (optional.isPresent()) {
            Vehicle vehicle = optional.get();
            DictionaryUtils.parse(vehicle);
            return vehicle;
        }
        throw new OAException(ResultCode.NOT_FOUND_ERROR);
    }

    /**
     * 列表：根据关键词查询
     * @param keyword
     * @param pageNo
     * @param row
     * @return
     */
    public Page<Vehicle> list(String keyword, Integer pageNo, Integer row) {


        StringUtils.defaultIfBlank(keyword, "");


        Pageable pageable = PageRequest.of(pageNo, row,  new Sort(Sort.Direction.DESC, "updateDate")
                .and(new Sort(Sort.Direction.ASC, "vehicleBrand")));

        Page<Vehicle> list = vehicleRepository.findAll((Specification<Vehicle>) (root, cq, cb) -> {

            Predicate p1 = cb.like(cb.lower(root.get("engineCode").as(String.class)), "%" + keyword.toLowerCase() + "%");
            Predicate p2 = cb.like(cb.lower(root.get("identifyCode").as(String.class)), "%" + keyword.toLowerCase() + "%");
            Predicate p3 = cb.like(cb.lower(root.get("licence").as(String.class)), "%" + keyword.toLowerCase() + "%");

            Predicate p = cb.or(p1, p2, p3);

            return p;
        }, pageable);

        DictionaryUtils.parse(list);

        return list;

    }

    /**
     * 列表组合查询,字符串包含
     * @param vehicle
     * @param pageNo
     * @param row
     * @return
     */
    public Page<Vehicle> list2(Vehicle vehicle, Integer pageNo, Integer row) {

        Pageable pageable = PageRequest.of(pageNo, row,  new Sort(Sort.Direction.DESC, "updateDate")
                .and(new Sort(Sort.Direction.ASC, "vehicleBrand")));


        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
//                .withMatcher("engineCode", match -> match.contains());

        Example<Vehicle> example = Example.of(vehicle, exampleMatcher);


        return vehicleRepository.findAll(example, pageable);
    }
}