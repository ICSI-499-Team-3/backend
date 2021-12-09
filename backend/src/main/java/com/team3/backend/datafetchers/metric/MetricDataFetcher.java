package com.team3.backend.datafetchers.metric;

import com.team3.backend.models.Measurement;
import com.team3.backend.models.Metric;
import com.team3.backend.models.Share;
import com.team3.backend.repositories.MeasurementRepository;
import com.team3.backend.repositories.MetricRepository;
import com.team3.backend.repositories.ShareRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tony Comanzo (entire class)
 */
@Component
public class MetricDataFetcher {

    private MetricRepository metricRepository;

    private MeasurementRepository measurementRepository;

    private ShareRepository shareRepository;

    @Autowired
    public MetricDataFetcher(MetricRepository metricRepository, MeasurementRepository measurementRepository, ShareRepository shareRepository) {
        this.metricRepository = metricRepository;
        this.measurementRepository = measurementRepository;
        this.shareRepository = shareRepository;
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

            // delete share documents with this metric
            List<Share> shares = shareRepository.findByDataId(metricId);
            shares.forEach(share -> shareRepository.delete(share));

            List<Measurement> data = metric.getData();
            for (Measurement measurement : data) {
                measurementRepository.deleteById(measurement.getId());
            }
            metricRepository.deleteById(metricId);
            return metric;
        };
    }

    public DataFetcher<List<Metric>> sync() {
        return dataFetchingEnvironment -> {
            Map<String, Object> input = dataFetchingEnvironment.getArgument("input");
            String userId = (String) input.get("userId");
            System.out.println("Starting...");

            List<Metric> updatedMetrics = new ArrayList<>();

            // get metrics by user id and see if the incoming metrics exist on the user yet
            List<Metric> existingMetrics = metricRepository.findByUserId(userId);
            Set<String> metricTitles = existingMetrics.stream().map(metric -> metric.getTitle()).collect(Collectors.toSet());

            List<LinkedHashMap<String, Object>> metrics = (List<LinkedHashMap<String, Object>>) input.get("metrics");
            for (LinkedHashMap<String, Object> incomingMetric : metrics) {
                String title = (String) incomingMetric.get("title");

                // if the metric exists on the user already, get the id, otherwise create the metric
                String metricId;
                if (metricTitles.contains(title)) {
                    metricId = existingMetrics.stream().filter(existingMetric -> existingMetric.getTitle().equals(title)).collect(Collectors.toList()).get(0).getId();
                } else {
                    String xUnits = (String) incomingMetric.get("xUnits");
                    String yUnits = (String) incomingMetric.get("yUnits");
                    Metric newMetric = metricRepository.save(new Metric(null, userId, title, xUnits, yUnits, new ArrayList<>()));
                    metricId = newMetric.getId();
                }

                // sync measurements for the metric, comparing timestamps:

                // make sorted list of incoming measurements
                List<LinkedHashMap<String, Object>> data = (List<LinkedHashMap<String, Object>>) incomingMetric.get("data");
                System.out.println("Making a list...");
                data.sort((o1, o2) -> {
                    Double o1Date = (Double) o1.get("dateTimeMeasured");
                    Double o2Date = (Double) o2.get("dateTimeMeasured");
                    return o1Date.compareTo(o2Date);
                });

                // get the measurements that exist already for the metric and sort them
                Metric metric = metricRepository.findById(metricId).get();
                List<Measurement> existingMeasurements = metric.getData();
                existingMeasurements.sort(Comparator.comparingDouble(Measurement::getDateTimeMeasured));

                /* now sync the measurements.
                 * if the metric was just created, will just add all incoming measurements
                 */
                List<Measurement> mergedMeasurements = new ArrayList<>();
                int i = 0;
                int j = 0;
                System.out.println("Checkin it twice...");
                while (i < existingMeasurements.size() && j < data.size()) {
                    Measurement currentExisting = existingMeasurements.get(i);
                    LinkedHashMap<String, Object> currentIncoming = data.get(j);

                    double incomingDateTimeMeasured = (Double) currentIncoming.get("dateTimeMeasured");

                    /* if the current incoming measurement's date is before the
                     * current existing measurement's date, insert it
                     *
                     * if the current incoming measurement's date is the same as
                     * the current existing measurement's date, keep the existing
                     *
                     * if the current incoming measurement's date is after the
                     * current existing measurement's date, insert the existing
                     */
                    if (incomingDateTimeMeasured < currentExisting.getDateTimeMeasured()) {
                        String x = (String) currentIncoming.get("x");
                        String y = (String) currentIncoming.get("y");
                        Measurement measurement = new Measurement(null, x, y, incomingDateTimeMeasured);
                        mergedMeasurements.add(measurementRepository.save(measurement));
                        j++;
                    } else if (incomingDateTimeMeasured == currentExisting.getDateTimeMeasured()) {
                        mergedMeasurements.add(currentExisting);
                        i++;
                        j++;
                    } else {
                        mergedMeasurements.add(currentExisting);
                        i++;
                    }
                }

                // add the remaining
                if (i < existingMeasurements.size()) {
                    mergedMeasurements.addAll(existingMeasurements.subList(i, existingMeasurements.size()));
                }
                while (j < data.size()) {
                    LinkedHashMap<String, Object> current = data.get(j);
                    String x = (String) current.get("x");
                    String y = (String) current.get("y");
                    double dateTimeMeasured = (Double) current.get("dateTimeMeasured");
                    Measurement measurement = new Measurement(null, x, y, dateTimeMeasured);
                    mergedMeasurements.add(measurementRepository.save(measurement));
                    j++;
                }

                metric.setData(mergedMeasurements);
                updatedMetrics.add(metricRepository.save(metric));
            }

            System.out.println("Finished...");
            return updatedMetrics;
        };
    }
}
