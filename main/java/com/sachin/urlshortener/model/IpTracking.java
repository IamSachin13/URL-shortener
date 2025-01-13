package com.sachin.urlshortener.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "ip_tracking")
@Getter
@Setter
public class IpTracking {

    @Id
    private String id;

    private String ipAddress;
    private long requestCount;
    private Instant lastRequestTime;
    private boolean blocked;
}
