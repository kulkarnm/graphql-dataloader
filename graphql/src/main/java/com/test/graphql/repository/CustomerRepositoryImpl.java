package com.test.graphql.repository;

import com.test.graphql.config.GraphQLContext;
import com.test.graphql.entity.Customer;
import com.test.graphql.entity.Device;
import com.test.graphql.tracer.DBQueryTracer;
import com.test.graphql.tracer.DBQueryTracingSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<Customer> getCustomersByCustomerIds(Set<Long> customerIds, GraphQLContext context) {
        Query query = new Query(Criteria.where("id").in(customerIds));
        DBQueryTracer tracer = new DBQueryTracer("MongoDB", "AccountRepository", query).startTracing();
        List<Customer> customerResponse = mongoTemplate.find(query, Customer.class);
        ((DBQueryTracingSummary) context.getDbQueryTracingSummary()).addDbQueryTracer(tracer.stopTracing(customerResponse));
        return customerResponse;
    }
}
