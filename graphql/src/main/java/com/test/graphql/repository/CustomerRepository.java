package com.test.graphql.repository;

import java.util.List;
import java.util.Set;

import com.test.graphql.config.GraphQLContext;
import com.test.graphql.entity.Customer;
public interface CustomerRepository {

    List<Customer> getCustomersByCustomerIds (Set<Long> customerIds, GraphQLContext context);
}
