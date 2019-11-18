package com.test.graphql.fetchers;

import com.test.graphql.entity.Account;
import com.test.graphql.entity.Customer;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class AccountDataFetcher {
    public CompletableFuture<Account> getAccount(DataFetchingEnvironment env) {
        Long accountId = Long.parseLong(env.getArgument("accountId"));

        DataLoader<Long, Account> accountDataLoader = env.getDataLoader("accountDataLoader");

        return accountDataLoader.load(accountId);
    }
} 
