package com.test.graphql.loaders;

import com.test.graphql.entity.Customer;
import com.test.graphql.repository.CustomerRepository;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.MappedBatchLoaderWithContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Component
public class CustomerBatchDataLoader {

    @Autowired
    private CustomerRepository customerRepository;

    public MappedBatchLoaderWithContext<Long, Customer> customerBatchLoader = new MappedBatchLoaderWithContext<Long, Customer>() {
        @Override
        public CompletionStage<Map<Long, Customer>> load(Set<Long> keys, BatchLoaderEnvironment env) {
            return CompletableFuture.supplyAsync(
                    () -> {
                        Map<Long, Customer> results = new HashMap<>();
                        List<Customer> customerList = customerRepository.getCustomersByCustomerIds(keys);

                        if (null == customerList || customerList.size() == 0) {
                            return results;
                        }

                        for (Customer customer : customerList) {
                            results.put(customer.getId(), customer);
                        }
                        return results;
                    });
        }
    };
} 
