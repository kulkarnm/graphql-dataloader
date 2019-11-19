package com.test.graphql.controller;

import com.test.graphql.config.AuthCheck;
import com.test.graphql.service.GraphQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class GraphQLController {
    @Autowired
    private GraphQLService graphQLService;

    @Autowired
    private AuthCheck authCheck;

    @PostMapping(value="/graphql")
    public Response query(
            @RequestBody Map<String,Object> body,
            HttpServletRequest request){
        try {
            System.out.println("query: " + (String)body.get("query"));
            String queryHash = authCheck.isAuthGranted(request, (String)body.get("query"));
            System.out.println(" queryHash: " + queryHash);
            return graphQLService.resolve(queryHash, (String)body.get("query"),(Map<String,Object>)body.get("variables"));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
} 
