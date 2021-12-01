package com.team3.backend.repositories;

import com.team3.backend.models.Share;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ShareRepository extends MongoRepository<Share, String> {

    @Query("{sharerId:'?0'}")
    List<Share> findBySharerId(String sharerId);

    @Query("{shareeId:'?0'}")
    List<Share> findByShareeId(String shareeId);

    @Query("{dataId:'?0'}")
    List<Share> findByDataId(String dataId);

    @Query("{dataId: '?0', shareeId: '?1'}")
    Share findByDataIdAndShareeId(String dataId, String shareeId);
}
