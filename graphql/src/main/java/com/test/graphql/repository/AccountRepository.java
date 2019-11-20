package com.test.graphql.repository;

import java.util.List;
import java.util.Set;

import com.test.graphql.config.GraphQLContext;
import com.test.graphql.entity.Account;


public interface AccountRepository {

    List<Account> getAccountsByAccountIds(Set<Long> accountIds, GraphQLContext context);
}
