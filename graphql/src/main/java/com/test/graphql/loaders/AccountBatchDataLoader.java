package com.test.graphql.loaders;

import com.test.graphql.entity.Account;
import com.test.graphql.entity.Customer;
import com.test.graphql.repository.AccountRepository;
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
public class AccountBatchDataLoader {

    @Autowired
    private AccountRepository accountRepository;

    public MappedBatchLoaderWithContext<Long, Account> accountBatchLoader = new MappedBatchLoaderWithContext<Long, Account>() {
        @Override
        public CompletionStage<Map<Long, Account>> load(Set<Long> keys, BatchLoaderEnvironment env) {
            return CompletableFuture.supplyAsync(
                    () -> {
                        Map<Long, Account> results = new HashMap<>();
                        List<Account> accountList = accountRepository.getAccountsByAccountIds(keys);

                        if (null == accountList || accountList.size() == 0) {
                            return results;
                        }

                        for (Account account : accountList) {
                            results.put(account.getId(), account);
                        }
                        return results;
                    });
        }
    };
} 
