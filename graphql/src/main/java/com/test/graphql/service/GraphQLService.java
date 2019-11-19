package com.test.graphql.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.dataloader.BatchLoaderContextProvider;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.dataloader.DataLoaderRegistry;
import org.dataloader.MappedBatchLoaderWithContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.test.graphql.config.GraphQLContext;
import com.test.graphql.controller.Message;
import com.test.graphql.controller.Response;
import com.test.graphql.fetchers.AccountDataFetcher;
import com.test.graphql.fetchers.CustomerDataFetcher;
import com.test.graphql.fetchers.DeviceDataFetcher;
import com.test.graphql.loaders.AccountBatchDataLoader;
import com.test.graphql.loaders.CustomerBatchDataLoader;
import com.test.graphql.loaders.DeviceBatchDataLoader;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;

@Service
public class GraphQLService {

    private GraphQL graphQL;
    
    @Autowired
    private CustomerDataFetcher customerDataFetcher;
    
    @Autowired
    private AccountDataFetcher accountDataFetcher;
    
    @Autowired
    private DeviceDataFetcher deviceDataFetcher;

    @Autowired 
    private AccountBatchDataLoader accountBatchDataLoader;
    
    @Autowired
    private DeviceBatchDataLoader deviceBatchDataLoader;
    
    @Autowired
    private CustomerBatchDataLoader customerBatchDataLoader;
    
    @Autowired
    public GraphQLService(GraphQL graphQL) {
        this.graphQL = graphQL;
    }

    public Response resolve(String queryHash, String query, Map<String, Object> variables) throws ExecutionException, InterruptedException {

        GraphQLContext context = new GraphQLContext(queryHash);

        ExecutionInput executionInput = ExecutionInput
                .newExecutionInput()
                .query(query)
                .variables(variables)
                .context(context).dataLoaderRegistry(getDataLoaders(context))
                .build();

        CompletableFuture<Response> promise = graphQL.executeAsync(executionInput).thenApply(result -> getResponse(result));

        return promise.get();
    }

    private Response getResponse(ExecutionResult result) {
        Response response;
        if (result.getErrors().size() > 0) {
            return new Response(HttpStatus.FORBIDDEN, getErrorMap(result.getErrors()));
        } else {
            return new Response(HttpStatus.OK, result.getData());
        }
    }

    private Map<String,Object> getErrorMap(List<GraphQLError> errors) {
        Map<String,Object> errorResponseData = new HashMap<>();
        List<String> messages = new ArrayList<>();

        for (GraphQLError error : errors) {
            Message msg = new Message(getErrorCodes(error), getErrorTitle(error), getErrorDetail(error));
            errorResponseData.put(getErrorCodes(error), msg);
        }
         //errorResponseData.put("Error", String.join("/", messages).toString());
        return errorResponseData;
    }

    private String getErrorDetail(GraphQLError error) {
        //TODO
        List<String> listPath = new ArrayList<>();
        if(error.getPath() != null ) {
            for(Object obj : error.getPath())
            listPath.add(obj.toString());
        }
        return error.getPath() != null ? String.join("/", listPath) : null;
    }

    private String getErrorTitle(GraphQLError error) {
        //TODO
        return "INTERNAL_SERVER_ERROR";
    }

    private String getErrorCodes(GraphQLError error) {
        //TODO
        return "ERROR_CODE";
    }

    private DataLoaderRegistry getDataLoaders(GraphQLContext context){
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        BatchLoaderContextProvider contextProvider = () -> context;

        DataLoaderOptions loaderOptions = DataLoaderOptions.newOptions().setBatchLoaderContextProvider(contextProvider);

        registerDataLoader(dataLoaderRegistry,loaderOptions, accountBatchDataLoader.accountBatchLoader, "AccountLoader");
        registerDataLoader(dataLoaderRegistry,loaderOptions, customerBatchDataLoader.customerBatchLoader, "CustomerLoader");
        registerDataLoader(dataLoaderRegistry,loaderOptions, deviceBatchDataLoader.deviceBatchLoader, "DeviceLoader");
        return dataLoaderRegistry;
    }

    private<K,V> void registerDataLoader(DataLoaderRegistry dataLoaderRegistry,
                                         DataLoaderOptions dataLoaderOptions,
                                         MappedBatchLoaderWithContext<K,V> batchLoader,
                                         String name){
        DataLoader<K,V> dataLoader = DataLoader.newMappedDataLoader(batchLoader,dataLoaderOptions);
        dataLoaderRegistry.register(name,dataLoader);
    }
} 
