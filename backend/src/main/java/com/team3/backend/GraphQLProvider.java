package com.team3.backend;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.team3.backend.datafetchers.log.LogDataFetcher;
import com.team3.backend.datafetchers.user.UserDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {

    @Autowired
    UserDataFetcher userDataFetcher;

    @Autowired
    LogDataFetcher logDataFetcher;

    private GraphQL graphQL;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    /**
     * Creates the GraphQLSchema instance and wires in code to fetch data
     * @param sdl
     * @return a GraphQLSchema
     */
    private GraphQLSchema buildSchema(String sdl) {
        // the parsed version of our schema file
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(sdl);

        RuntimeWiring runtimeWiring = buildWiring();

        // combines the TypeDefinitionRegistry with RuntimeWiring to actually make the GraphQLSchema
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
    }

    /**
     * Uses graphQLDataFetchers bean to register DataFetchers
     * @return
     */
    private RuntimeWiring buildWiring() {

        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("getAllUsers", userDataFetcher.getAllUsers())
                        .dataFetcher("getUserByEmail", userDataFetcher.getUserByEmail())
                        .dataFetcher("getAllLogs", logDataFetcher.getAllLogs())
                        .dataFetcher("getLogsByUserEmail", logDataFetcher.getLogsByEmail())
                )
                .type(newTypeWiring("Mutation")
                        .dataFetcher("createUser", userDataFetcher.createUser())
                        .dataFetcher("createLog", logDataFetcher.createLog())
                )
                .build();

    }
}
