package com.team3.backend.datafetchers.metric;

import com.team3.backend.models.Measurement;
import com.team3.backend.models.Metric;
import com.team3.backend.repositories.MeasurementRepository;
import com.team3.backend.repositories.MetricRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MetricDataFetcher {

    private MetricRepository metricRepository;

    private MeasurementRepository measurementRepository;

    @Autowired
    public MetricDataFetcher(MetricRepository metricRepository, MeasurementRepository measurementRepository) {
        this.metricRepository = metricRepository;
        this.measurementRepository = measurementRepository;
    }

    public DataFetcher<List<Metric>> getMetricsByUserId() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return metricRepository.findByUserId(userId);
        };
    }

    public DataFetcher<Metric> createMetric() {
        return dataFetchingEnvironment -> {
            Map<String, Object> input = dataFetchingEnvironment.getArgument("input");

            String userId = (String) input.get("userId");
            String title = (String) input.get("title");
            String xUnits = (String) input.get("xUnits");
            String yUnits = (String) input.get("yUnits");
            List<Measurement> data = new ArrayList<>();

            Metric metric = new Metric(null, userId, title, xUnits, yUnits, data);

            return metricRepository.save(metric);
        };
    }

    public DataFetcher<Metric> getMetricById() {
        return dataFetchingEnvironment -> {
            String metricId = dataFetchingEnvironment.getArgument("metricId");

            Metric metric = metricRepository.findById(metricId).orElseThrow();
            return metric;
        };
    }

    public DataFetcher<Metric> deleteMetric() {
        return dataFetchingEnvironment -> {
            String metricId = dataFetchingEnvironment.getArgument("metricId");
            Metric metric = metricRepository.findById(metricId).orElseThrow();
            List<Measurement> data = metric.getData();
            for (Measurement measurement : data) {
                measurementRepository.deleteById(measurement.getId());
            }
            metricRepository.deleteById(metricId);
            return metric;
        };
    }
}
