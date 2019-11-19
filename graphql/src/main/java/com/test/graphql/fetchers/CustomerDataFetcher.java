package com.test.graphql.fetchers;

import java.util.concurrent.CompletableFuture;

import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import com.test.graphql.entity.Customer;

import graphql.schema.DataFetchingEnvironment;

@Component
public class CustomerDataFetcher {
    public CompletableFuture<Customer> getCustomer(DataFetchingEnvironment env) {
        Long customerId = Long.parseLong(env.getArgument("customerId"));

        DataLoader<Long, Customer> customerDataLoader = env.getDataLoader("CustomerLoader");

        return customerDataLoader.load(customerId);
    }
} 
