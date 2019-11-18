package com.test.graphql.repository;

import com.test.graphql.entity.Account;
import com.test.graphql.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AccountRepository {

    List<Account> getAccountsByAccountIds(Set<Long> accountIds);
}
