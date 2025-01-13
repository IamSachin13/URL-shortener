package com.sachin.urlshortener.repository;

import com.sachin.urlshortener.model.UrlRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRecordRepository extends MongoRepository<UrlRecord, String> {
    Optional<UrlRecord> findByShortUrl(String shortUrl);
}
