package com.test.graphql.repository;

import com.test.graphql.entity.Customer;
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
    public List<Customer> getCustomersByCustomerIds(Set<Long> customerIds) {
        Query query = new Query(Criteria.where("id").in(customerIds));
        return mongoTemplate.find(query, Customer.class);
    }
}
