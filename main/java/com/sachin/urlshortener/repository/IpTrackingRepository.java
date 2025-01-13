package com.sachin.urlshortener.repository;

import com.sachin.urlshortener.model.IpTracking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IpTrackingRepository extends MongoRepository<IpTracking, String> {
    Optional<IpTracking> findByIpAddress(String ipAddress);
}
