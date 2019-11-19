package com.test.graphql.fetchers;

import java.util.concurrent.CompletableFuture;

import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import com.test.graphql.entity.Account;

import graphql.schema.DataFetchingEnvironment;

@Component
public class AccountDataFetcher {
    public CompletableFuture<Account> getAccount(DataFetchingEnvironment env) {
        Long accountId = Long.parseLong(env.getArgument("accountId"));

        DataLoader<Long, Account> accountDataLoader = env.getDataLoader("AccountLoader");

        return accountDataLoader.load(accountId);
    }
} 
