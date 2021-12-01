package com.team3.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shares")
public class Share {

    @Id
    private String id;

    private String sharerId;

    private String shareeId;

    private boolean sharedLog;

    private boolean sharedMetric;

    private String dataId;

    public Share(String id, String sharerId, String shareeId, boolean sharedLog, boolean sharedMetric, String dataId) {
        this.id = id;
        this.sharerId = sharerId;
        this.shareeId = shareeId;
        this.sharedLog = sharedLog;
        this.sharedMetric = sharedMetric;
        this.dataId = dataId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSharerId() {
        return sharerId;
    }

    public void setSharerId(String sharerId) {
        this.sharerId = sharerId;
    }

    public String getShareeId() {
        return shareeId;
    }

    public void setShareeId(String shareeId) {
        this.shareeId = shareeId;
    }

    public boolean isSharedLog() {
        return sharedLog;
    }

    public void setSharedLog(boolean sharedLog) {
        this.sharedLog = sharedLog;
    }

    public boolean isSharedMetric() {
        return sharedMetric;
    }

    public void setSharedMetric(boolean sharedMetric) {
        this.sharedMetric = sharedMetric;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
}
