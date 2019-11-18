package com.test.graphql.config;

public class GraphQLContext {

    private String queryHash;

    public GraphQLContext(String queryHash) {
        this.queryHash = queryHash;
    }

    public String getQueryHash() {
        return queryHash;
    }

}
