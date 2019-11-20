package com.test.graphql.config;

import com.test.graphql.tracer.DBQueryTracingSummary;

public class GraphQLContext {

    private String queryHash;

    private DBQueryTracingSummary dbQueryTracingSummary = new DBQueryTracingSummary();


    public GraphQLContext(String queryHash) {
        this.queryHash = queryHash;
    }

    public String getQueryHash() {
        return queryHash;
    }

    public Object getDbQueryTracingSummary() {
        return dbQueryTracingSummary;
    }
}
