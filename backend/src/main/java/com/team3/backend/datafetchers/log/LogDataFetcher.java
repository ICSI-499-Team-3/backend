package com.team3.backend.datafetchers.log;

import com.team3.backend.models.Log;
import com.team3.backend.models.User;
import com.team3.backend.repositories.LogRepository;
import com.team3.backend.repositories.UserRepository;
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

    public DataFetcher getLogsByUserId() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return logRepository.findByUserId(userId);
        };
    }

    public DataFetcher createLog() {
        return dataFetchingEnvironment -> {
            Map<String, Object> input = dataFetchingEnvironment.getArgument("input");

            String userId = (String) input.get("userId");
            double dateTimeOfActivity = (Double) input.get("dateTimeOfActivity");
            String notes = (String) input.get("notes");
            List<String> categories = (List<String>) input.get("categories");
            List<String> mood = (List<String>) input.get("mood");

            Log log = new Log(null, userId, dateTimeOfActivity, notes, categories, mood);
            return logRepository.save(log);
        };
    }

    public DataFetcher getLogsByUserIdDelete() {
        return dataFetchingEnvironment -> {
            Map<String, Object> input = dataFetchingEnvironment.getArgument("input");

            String userId = (String) input.get("userId");
            double dateTimeOfActivity = (Double) input.get("dateTimeOfActivity");
            String notes = (String) input.get("notes");
            List<String> categories = (List<String>) input.get("categories");
            List<String> mood = (List<String>) input.get("mood");

            Log log = new Log(null, userId, dateTimeOfActivity, notes, categories, mood);
            return input.remove(log);

           // return logRepository.save(log);
            //return logRepository.deleteById();
        };
    }
}
