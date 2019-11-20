package com.test.graphql.repository;

import java.util.List;
import java.util.Set;

import com.test.graphql.config.GraphQLContext;
import com.test.graphql.tracer.DBQueryTracer;
import com.test.graphql.tracer.DBQueryTracingSummary;
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
    public List<Account> getAccountsByAccountIds(Set<Long> accountIds, GraphQLContext context) {
        Query query = new Query(Criteria.where("id").in(accountIds));
        DBQueryTracer tracer = new DBQueryTracer("MongoDB", "AccountRepository", query).startTracing();
        List<Account> accountResponse =  mongoTemplate.find(query, Account.class);
        ((DBQueryTracingSummary) context.getDbQueryTracingSummary()).addDbQueryTracer(tracer.stopTracing(accountResponse));
        return accountResponse;
    }
}
