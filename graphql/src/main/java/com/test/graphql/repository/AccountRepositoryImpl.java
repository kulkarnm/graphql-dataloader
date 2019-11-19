package com.test.graphql.repository;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.test.graphql.entity.Account;
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
