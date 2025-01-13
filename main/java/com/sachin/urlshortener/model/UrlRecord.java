package com.sachin.urlshortener.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document("urldb")
@AllArgsConstructor
@NoArgsConstructor
public class UrlRecord {

    @Id
    private String id;

    private String shortUrl;
    private String longUrl;

    private Instant createdOn;
    private Instant updatedOn;

    private long usageCount;

    public UrlRecord(String shortUrl, String longUrl, Instant createdOn, Instant updatedOn) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.usageCount = 0;
    }
}
