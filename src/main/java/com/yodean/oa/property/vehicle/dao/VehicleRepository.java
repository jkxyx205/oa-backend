package com.yodean.oa.property.vehicle.dao;

import com.yodean.oa.common.dao.ExtendedRepository;
import com.yodean.oa.property.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by rick on 5/16/18.
 */
public interface VehicleRepository extends ExtendedRepository<Vehicle, Integer>, JpaSpecificationExecutor {
}
