package com.test.graphql.fetchers;

import com.test.graphql.entity.Customer;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CustomerDataFetcher {
    public CompletableFuture<Customer> getCustomer(DataFetchingEnvironment env) {
        Long customerId = Long.parseLong(env.getArgument("customerId"));

        DataLoader<Long, Customer> customerDataLoader = env.getDataLoader("customerDataLoader");

        return customerDataLoader.load(customerId);
    }
} 
