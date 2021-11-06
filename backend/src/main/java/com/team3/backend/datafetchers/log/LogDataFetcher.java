package com.team3.backend.datafetchers.log;

import com.team3.backend.models.Log;
import com.team3.backend.repositories.LogRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LogDataFetcher {

    private LogRepository logRepository;

    @Autowired
    LogDataFetcher(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public DataFetcher getAllLogs() {
        return dataFetchingEnvironment -> logRepository.findAll();
    }

    public DataFetcher getLogsByEmail() {
        return dataFetchingEnvironment -> {
            String email = dataFetchingEnvironment.getArgument("email");
            return logRepository.findByEmail(email);
        };
    }

    public DataFetcher createLog() {
        return dataFetchingEnvironment -> {
            Map<String, Object> input = dataFetchingEnvironment.getArgument("input");

            double dateTimeOfActivity = (Double) input.get("dateTimeOfActivity");
            String notes = (String) input.get("notes");
            List<String> categories = (List<String>) input.get("categories");
            List<String> mood = (List<String>) input.get("mood");

            Log log = new Log(null, dateTimeOfActivity, notes, categories, mood);
            return logRepository.save(log);
        };
    }

}
