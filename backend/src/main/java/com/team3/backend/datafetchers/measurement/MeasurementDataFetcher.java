package com.team3.backend.datafetchers.measurement;

import com.team3.backend.models.Measurement;
import com.team3.backend.models.Metric;
import com.team3.backend.repositories.MeasurementRepository;
import com.team3.backend.repositories.MetricRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Tony Comanzo (entire class)
 */
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

    public DataFetcher<Measurement> updateMeasurement() {
        return dataFetchingEnvironment -> {
            Map<String, Object> input = dataFetchingEnvironment.getArgument("input");
            String id = (String) input.get("id");
            String metricId = (String) input.get("metricId");
            String x = (String) input.get("x");
            String y = (String) input.get("y");
            Double dateTimeMeasured = (Double) input.get("dateTimeMeasured");

            Metric metric = metricRepository.findById(metricId).orElseThrow();

            Measurement measurement = measurementRepository.findById(id).orElseThrow();

            measurement.setX(x);
            measurement.setY(y);
            measurement.setDateTimeMeasured(dateTimeMeasured);

            List<Measurement> data = metric.getData();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getId().equals(id)) {
                    data.set(i, measurement);
                }
            }
            metricRepository.save(metric);

            return measurementRepository.save(measurement);
        };
    }

    public DataFetcher<List<Measurement>> deleteMeasurement() {
        return dataFetchingEnvironment -> {
            String metricId = dataFetchingEnvironment.getArgument("metricId");
            List<String> input = dataFetchingEnvironment.getArgument("input");

            // delete nested Measurements from Metric
            Metric metric = metricRepository.findById(metricId).orElseThrow();
            List<Measurement> data = metric.getData();
            data.removeIf(measurement -> input.contains(measurement.getId()));
            metric.setData(data);
            metricRepository.save(metric);

            // delete from Measurement collection
            List<Measurement> deletedMeasurements = new ArrayList<>();
            for (String id : input) {
                Optional<Measurement> measurement = measurementRepository.findById(id);
                if (measurement.isPresent()) {
                    deletedMeasurements.add(measurement.get());
                    measurementRepository.deleteById(id);
                }
            }

            return deletedMeasurements;
        };
    }
}
