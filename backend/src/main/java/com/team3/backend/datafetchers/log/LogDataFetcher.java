package com.team3.backend.datafetchers.log;

import com.team3.backend.models.Log;
import com.team3.backend.repositories.LogRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
            int datetimeOfActivity = dataFetchingEnvironment.getArgument("datetimeOfActivity");
            String notes = dataFetchingEnvironment.getArgument("notes");
            List<String> categories = dataFetchingEnvironment.getArgument("categories");
            List<String> mood = dataFetchingEnvironment.getArgument("mood");

            Log log = new Log(null, datetimeOfActivity, notes, categories, mood);
            return logRepository.save(log);
        };
    }


}
