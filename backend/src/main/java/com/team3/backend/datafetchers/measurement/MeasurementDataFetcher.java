package com.team3.backend.datafetchers.measurement;

import com.team3.backend.models.Measurement;
import com.team3.backend.models.Metric;
import com.team3.backend.repositories.MeasurementRepository;
import com.team3.backend.repositories.MetricRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MeasurementDataFetcher {

    private MeasurementRepository measurementRepository;
    private MetricRepository metricRepository;

    @Autowired
    public MeasurementDataFetcher(MeasurementRepository measurementRepository, MetricRepository metricRepository) {
        this.measurementRepository = measurementRepository;
        this.metricRepository = metricRepository;
    }

    public DataFetcher<Measurement> createMeasurement() {
        return dataFetchingEnvironment -> {
            Map<String, Object> input = dataFetchingEnvironment.getArgument("input");

            String metricId = (String) input.get("metricId");
            String x = (String) input.get("x");
            String y = (String) input.get("y");
            Double dateTimeMeasured = (Double) input.get("dateTimeMeasured");

            Metric metric = metricRepository.findById(metricId).orElseThrow();

            Measurement measurement = new Measurement(null, x, y, dateTimeMeasured);
            Measurement savedMeasurement = measurementRepository.save(measurement);

            // update the metric with the measurement
            List<Measurement> data = metric.getData();
            data.add(savedMeasurement);
            metric.setData(data);
            metricRepository.save(metric);

            return savedMeasurement;
        };
    }

    public DataFetcher<List<Measurement>> getMeasurementsByMetricId() {
        return dataFetchingEnvironment -> {
            String metricId = dataFetchingEnvironment.getArgument("metricId");

            Metric metric = metricRepository.findById(metricId).orElseThrow();

            return metric.getData();
        };
    }
}
