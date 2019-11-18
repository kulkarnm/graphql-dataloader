package com.test.graphql.repository;

import com.test.graphql.entity.Customer;
import com.test.graphql.entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Set;

public class DeviceRepositoryImpl implements DeviceRepository {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<Device> getDevicesByDeviceIds(Set<Long> deviceIds) {
        Query query = new Query(Criteria.where("deviceIds").in(deviceIds));
        return mongoTemplate.find(query, Device.class);
    }
}
