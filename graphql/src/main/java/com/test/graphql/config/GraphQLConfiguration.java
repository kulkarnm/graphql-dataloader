package com.test.graphql.config;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.test.graphql.fetchers.RootDataFetcher;

import graphql.ExecutionInput;
import graphql.GraphQL;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentationOptions;
import graphql.execution.preparsed.PreparsedDocumentEntry;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Configuration
@EnableCaching
public class GraphQLConfiguration {

    private GraphQL graphQL;
    
    @Autowired
    private RootDataFetcher rootDataFetcher;

    private Cache<String, PreparsedDocumentEntry> preparsedQueryCache;
    
    private static String[] SCHEMA_FILES = 
            new String[] {
            "schema/root.graphql",
            "schema/customer.graphql",
                    "schema/account.graphql",
                    "schema/device.graphql"
            };

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        System.out.println("^^^IN init method^^^");
        preparsedQueryCache = Caffeine.newBuilder().maximumSize(1000).build();
        RuntimeWiring runtimeWiring = buildWiring();
        TypeDefinitionRegistry  schema = loadSchema();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(schema, runtimeWiring);

        GraphQL.Builder builder = GraphQL.newGraphQL(graphQLSchema).preparsedDocumentProvider(this::getCachedQuery);

        List<Instrumentation> chainedList = new ArrayList<>();

        chainedList.add(new TracingInstrumentation(TracingInstrumentation.Options.newOptions().includeTrivialDataFetchers(false)));

        DataLoaderDispatcherInstrumentationOptions options = DataLoaderDispatcherInstrumentationOptions.newOptions().includeStatistics(true);
        DataLoaderDispatcherInstrumentation dispatcherInstrumentation = new DataLoaderDispatcherInstrumentation(options);
        chainedList.add(dispatcherInstrumentation);

        ChainedInstrumentation chainedInstrumentation = new ChainedInstrumentation(chainedList);

        this.graphQL = builder.instrumentation(chainedInstrumentation).build();
    }

    private PreparsedDocumentEntry getCachedQuery(ExecutionInput executionInput,
                                                  Function<ExecutionInput, PreparsedDocumentEntry> computeFunction) {
        GraphQLContext context = (GraphQLContext) executionInput.getContext();
        return preparsedQueryCache.get(context.getQueryHash(), (key) -> computeFunction.apply(executionInput));
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring
                .newRuntimeWiring()
                .type("Query",
                        typeWiring ->
                                typeWiring.dataFetcher("customer", rootDataFetcher.getCustomerDataFetcher())
                                        .dataFetcher("account", rootDataFetcher.getAccountDataFetcher())
                                        .dataFetcher("device", rootDataFetcher.getDeviceDataFetcher())
                ).build();
    }

    private TypeDefinitionRegistry loadSchema() throws IOException {
        TypeDefinitionRegistry typeDefinitionRegistry = new TypeDefinitionRegistry();
        SchemaParser schemaParser = new SchemaParser();
        for(String schemaFileName: SCHEMA_FILES){
            URL url = Resources.getResource(schemaFileName);
            String sdl = Resources.toString(url, Charsets.UTF_8);
            typeDefinitionRegistry.merge(schemaParser.parse(sdl));
        }
        return typeDefinitionRegistry;
    }
} 
