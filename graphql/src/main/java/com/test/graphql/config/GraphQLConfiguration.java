package com.test.graphql.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.test.graphql.fetchers.AccountDataFetcher;
import com.test.graphql.fetchers.CustomerDataFetcher;
import com.test.graphql.fetchers.DeviceDataFetcher;
import graphql.ExecutionInput;
import graphql.GraphQL;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentationOptions;
import graphql.execution.preparsed.PreparsedDocumentEntry;
import graphql.language.TypeDefinition;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import com.github.benmanes.caffeine.cache.Cache;

@Configuration
@EnableCaching
public class GraphQLConfiguration {

    private GraphQL graphQL;

    @Autowired
    private CustomerDataFetcher customerDataFetcher;
    @Autowired
    private AccountDataFetcher accountDataFetcher;
    @Autowired
    private DeviceDataFetcher deviceDataFetcher;

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
        preparsedQueryCache = Caffeine.newBuilder().maximumSize(1000).build();
        RuntimeWiring runtimeWiring = buildWiring();
        TypeDefinitionRegistry  schema = loadSchema();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(schema, runtimeWiring);
        
        GraphQL.Builder builder = GraphQL.newGraphQL(graphQLSchema).preparsedDocumentProvider(this::getCachedQuery);

        List<Instrumentation> chainedList = new ArrayList<>();

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
                                typeWiring.dataFetcher("Customer", environment -> customerDataFetcher.getCustomer(environment))
                                        .dataFetcher("Account", environment -> accountDataFetcher.getAccount(environment))
                                        .dataFetcher("Device", environment -> deviceDataFetcher.getDevice(environment))
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
