package com.test.graphql.repository;

import com.test.graphql.entity.Account;
import com.test.graphql.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
@Repository
public class AccountRepositoryImpl implements AccountRepository {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<Account> getAccountsByAccountIds(Set<Long> accountIds) {
        Query query = new Query(Criteria.where("accountIds").in(accountIds));
        return mongoTemplate.find(query, Account.class);
    }
}