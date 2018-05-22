package com.yodean.oa.property.vehicle.service;

import com.yodean.oa.property.vehicle.entity.Vehicle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * Created by rick on 5/16/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VehicleServiceTest {

    @Autowired
    private VehicleService vehicleService;


    @Test
    public void save() throws Exception {

        Vehicle vehicle = new Vehicle();
        vehicle.setBuyDate(new Date());
        vehicle.setCapacity(12);
        vehicle.setEngineCode("23232323233");
        vehicle.setIdentifyCode("s23232232");
        vehicle.setKeeper(1);
        vehicle.setLicence("苏E 23bded");
        vehicle.setVehicleType(Vehicle.VehicleType.SMALL);
//        vehicle.setVehicleBrand(Vehicle.VehicleBrand.NS);
        vehicle.setVehicleStatus(Vehicle.VehicleStatus.AVAILABLE);
        vehicleService.save(vehicle);
    }

    @Test
    public void update() throws Exception {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        vehicle.setLicence("京Q 123456");
        vehicleService.update(vehicle, 1);
    }

    @Test
    public void delete() throws Exception {
        vehicleService.delete(1);
    }

    @Test
    public void findById() throws Exception {
        Vehicle vehicle = vehicleService.findById(1);
        System.out.println(vehicle);
    }

    @Test
    public void list() {
       Page<Vehicle> page = vehicleService.list("co", 0, 15);
       System.out.println(page);
    }

    @Test
    public void list2() {
        Vehicle vehicle = new Vehicle();
        vehicle.setEngineCode("eg");
        vehicle.setIdentifyCode("co");
        Page<Vehicle> page = vehicleService.list2(vehicle, 0, 15);
        System.out.println(page);
    }



}