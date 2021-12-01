package com.team3.backend.datafetchers.share;

import com.team3.backend.models.Share;
import com.team3.backend.models.User;
import com.team3.backend.repositories.ShareRepository;
import com.team3.backend.repositories.UserRepository;
import graphql.GraphQLException;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ShareDataFetcher {

    private ShareRepository shareRepository;
    private UserRepository userRepository;

    @Autowired
    public ShareDataFetcher(ShareRepository shareRepository, UserRepository userRepository) {
        this.shareRepository = shareRepository;
        this.userRepository = userRepository;
    }

    public DataFetcher<List<Share>> getSharesBySharerId() {
        return dataFetchingEnvironment -> {
            String sharerId = dataFetchingEnvironment.getArgument("id");
            return shareRepository.findBySharerId(sharerId);
        };
    }

    public DataFetcher<List<Share>> getSharesByShareeId() {
        return dataFetchingEnvironment -> {
            String shareeId = dataFetchingEnvironment.getArgument("id");
            return shareRepository.findByShareeId(shareeId);
        };
    }

    public DataFetcher<List<Share>> getSharesByDataId() {
        return dataFetchingEnvironment -> {
            String dataId = dataFetchingEnvironment.getArgument("id");
            return shareRepository.findByDataId(dataId);
        };
    }

    public DataFetcher<List<User>> getShareesByDataId() {
        return dataFetchingEnvironment -> {
            String dataId = dataFetchingEnvironment.getArgument("id");
            List<Share> shares = shareRepository.findByDataId(dataId);

            // for each sharee id, get the user and filter out null values if
            // the sharee id can't be found
            List<User> users = shares.stream()
                    .map(share -> userRepository.findById(share.getShareeId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return users;
        };
    }

    public DataFetcher<Share> createShare() {
        return dataFetchingEnvironment -> {
            Map<String, Object> input = dataFetchingEnvironment.getArgument("input");

            String sharerId = (String) input.get("sharerId");
            String shareeEmail = (String) input.get("shareeEmail");
            boolean sharedLog = (Boolean) input.get("sharedLog");
            boolean sharedMetric = (Boolean) input.get("sharedMetric");
            String dataId = (String) input.get("dataId");

            if (sharedLog == sharedMetric) {
                throw new GraphQLException("sharedLog and sharedMetric cannot be equal");
            }

            User sharee = userRepository.findByEmail(shareeEmail);
            if (sharee == null) {
                throw new Exception("No user with the email " + shareeEmail + " exists");
            }

            String shareeId = sharee.getId();

            if (shareRepository.findByDataIdAndShareeId(dataId, shareeId) != null) {
                throw new Exception("That data is already shared with " + shareeEmail);
            }

            Share newShare = new Share(null, sharerId, shareeId, sharedLog, sharedMetric, dataId);

            return shareRepository.save(newShare);
        };
    }

    public DataFetcher<Share> deleteShare() {
        return dataFetchingEnvironment -> {
            Map<String, Object> input = dataFetchingEnvironment.getArgument("input");

            String shareeId = (String) input.get("shareeId");
            String dataId = (String) input.get("dataId");
            Share share = shareRepository.findByDataIdAndShareeId(dataId, shareeId);
            shareRepository.delete(share);
            return share;
        };
    }
}
