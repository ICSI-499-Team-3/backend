package com.team3.backend;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.team3.backend.datafetchers.log.LogDataFetcher;
import com.team3.backend.datafetchers.measurement.MeasurementDataFetcher;
import com.team3.backend.datafetchers.metric.MetricDataFetcher;
import com.team3.backend.datafetchers.share.ShareDataFetcher;
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

/**
 * @author Tony Comanzo, Habib Affinnih, Emma Wirth, Lauren Velez
 */
@Component
public class GraphQLProvider {

    @Autowired
    UserDataFetcher userDataFetcher;

    @Autowired
    LogDataFetcher logDataFetcher;

    @Autowired
    MetricDataFetcher metricDataFetcher;

    @Autowired
    MeasurementDataFetcher measurementDataFetcher;

    @Autowired
    ShareDataFetcher shareDataFetcher;

    private GraphQL graphQL;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    /**
     * @author Tony Comanzo 
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    /**
     * Creates the GraphQLSchema instance and wires in code to fetch data
     * @author Tony Comanzo 
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
     * @author Tony Comanzo, Habib Affinnih, Lauren Velez, Emma Wirth
     * @return
     */
    private RuntimeWiring buildWiring() {

        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("GetAllUsers", userDataFetcher.getAllUsers())
                        .dataFetcher("GetUserByEmail", userDataFetcher.getUserByEmail())
                        .dataFetcher("GetUserByEmailAndPassword", userDataFetcher.getUserByEmailAndPassword())
                        .dataFetcher("SendPasswordResetCode", userDataFetcher.sendPasswordResetCode())
                        .dataFetcher("VerifyPasswordResetCode", userDataFetcher.verifyPasswordResetCode())
                        .dataFetcher("GetAllLogs", logDataFetcher.getAllLogs())
                        .dataFetcher("GetLogsByUserId", logDataFetcher.getLogsByUserId())
                        .dataFetcher("GetMetricsByUserId", metricDataFetcher.getMetricsByUserId())
                        .dataFetcher("GetMetricById", metricDataFetcher.getMetricById())
                        .dataFetcher("GetMeasurementsByMetricId", measurementDataFetcher.getMeasurementsByMetricId())
                        .dataFetcher("GetSharesBySharerId", shareDataFetcher.getSharesBySharerId())
                        .dataFetcher("GetSharesByShareeId", shareDataFetcher.getSharesByShareeId())
                        .dataFetcher("GetSharesByDataId", shareDataFetcher.getSharesByDataId())
                        .dataFetcher("GetShareesByDataId", shareDataFetcher.getShareesByDataId())
                        .dataFetcher("GetSharedLogsByShareeId", shareDataFetcher.getSharedLogsByShareeId())
                        .dataFetcher("GetSharedMetricsByShareeId", shareDataFetcher.getSharedMetricsByShareeId())
                        .dataFetcher("GetSharersByShareeId", shareDataFetcher.getSharersByShareeId())
                        .dataFetcher("GetLogsBySharerAndShareeId", shareDataFetcher.getLogsBySharerAndShareeId())
                        .dataFetcher("GetMetricsBySharerAndShareeId", shareDataFetcher.getMetricsBySharerAndShareeId())
                )
                .type(newTypeWiring("Mutation")
                        .dataFetcher("CreateUser", userDataFetcher.createUser())
                        .dataFetcher("UpdateUserName", userDataFetcher.updateUserName())
                        .dataFetcher("UpdateUserEmail", userDataFetcher.updateUserEmail())
                        .dataFetcher("UpdateUserPassword", userDataFetcher.updateUserPassword())
                        .dataFetcher("UpdateUserPreExistingConditions", userDataFetcher.updateUserPreExistingConditions())
                        .dataFetcher("ResetUserPassword", userDataFetcher.resetUserPassword())
                        .dataFetcher("CreateLog", logDataFetcher.createLog())
                        .dataFetcher("CreateMetric", metricDataFetcher.createMetric())
                        .dataFetcher("DeleteMetric", metricDataFetcher.deleteMetric())
                        .dataFetcher("DeleteLog", logDataFetcher.deleteLog())
                        .dataFetcher("CreateMeasurement", measurementDataFetcher.createMeasurement())
                        .dataFetcher("UpdateMeasurement", measurementDataFetcher.updateMeasurement())
                        .dataFetcher("DeleteMeasurement", measurementDataFetcher.deleteMeasurement())
                        .dataFetcher("CreateShare", shareDataFetcher.createShare())
                        .dataFetcher("DeleteShare", shareDataFetcher.deleteShare())
                        .dataFetcher("Sync", metricDataFetcher.sync())
                )
                .build();
    }
}
