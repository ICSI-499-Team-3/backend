package com.team3.backend.datafetchers.log;

import com.team3.backend.models.Log;
import com.team3.backend.repositories.LogRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Tony Comanzo, Lauren Velez, Emma Wirth
 */
@Component
public class LogDataFetcher {

    private LogRepository logRepository;

    @Autowired
    LogDataFetcher(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    /**
     * @author Tony Comanzo
     */
    public DataFetcher getAllLogs() {
        return dataFetchingEnvironment -> logRepository.findAll();
    }

    /**
     * @author Tony Comanzo
     */
    public DataFetcher getLogsByUserId() {
        return dataFetchingEnvironment -> {
            String userId = dataFetchingEnvironment.getArgument("userId");
            return logRepository.findByUserId(userId);
        };
    }

    /**
     * @author Tony Comanzo 
     */
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

    /**
     * @author Emma Wirth, Lauren Velez
     */
    public DataFetcher<Log> deleteLog() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            Log log = logRepository.findById(id).orElseThrow();
            logRepository.deleteById(id);

            return log;
        };
    }
}

