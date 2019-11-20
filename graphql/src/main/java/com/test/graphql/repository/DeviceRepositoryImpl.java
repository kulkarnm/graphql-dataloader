package com.test.graphql.repository;

import java.util.List;
import java.util.Set;

import com.test.graphql.config.GraphQLContext;
import com.test.graphql.tracer.DBQueryTracer;
import com.test.graphql.tracer.DBQueryTracingSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.test.graphql.entity.Device;
@Repository
public class DeviceRepositoryImpl implements DeviceRepository {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<Device> getDevicesByDeviceIds(Set<Long> deviceIds, GraphQLContext context) {
        Query query = new Query(Criteria.where("id").in(deviceIds));
        DBQueryTracer tracer = new DBQueryTracer("MongoDB", "AccountRepository", query).startTracing();
        List<Device> deviceResponse = mongoTemplate.find(query, Device.class);
        ((DBQueryTracingSummary) context.getDbQueryTracingSummary()).addDbQueryTracer(tracer.stopTracing(deviceResponse));
        return deviceResponse;
    }
}
