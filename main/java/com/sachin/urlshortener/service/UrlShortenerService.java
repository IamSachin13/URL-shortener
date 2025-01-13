package com.sachin.urlshortener.service;

import com.sachin.urlshortener.dto.LongUrlResponse;
import com.sachin.urlshortener.model.UrlRecord;
import com.sachin.urlshortener.repository.UrlRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static com.sachin.urlshortener.common.constants.Constants.*;
import static com.sachin.urlshortener.common.messagecontants.MessageConstants.*;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlRecordRepository urlRecordRepository;
    private final RedisTemplate<String, UrlRecord> redisTemplate;

    @Value("${application.shorturl.dns}")
    private String shortUrlDns;

    public String shortenUrl(String longUrl, String customAlias) {
        String shortUrl = (customAlias != null && !customAlias.isEmpty())
                ? customAlias : generateRandomShortKey();
        shortUrl = shortUrlDns + BACKSLASH + shortUrl;

        // Check for collision in DB
        if (urlRecordRepository.findByShortUrl(shortUrl).isPresent()) {
            throw new RuntimeException(SHORT_URL_ALIAS_ALREADY_IN_USE + shortUrl);
        }

        // Build new record
        UrlRecord record = new UrlRecord(shortUrl, longUrl, Instant.now(), Instant.now());
        UrlRecord saved = urlRecordRepository.save(record);

        String redisKey = REDIS_KEY_PREFIX + shortUrl;
        redisTemplate.opsForValue().set(redisKey, saved);

        return shortUrl;
    }

    public LongUrlResponse getLongUrl(String shortUrl) {
        String redisKey = REDIS_KEY_PREFIX + shortUrl;

        // Check Redis cache first
        UrlRecord cached = redisTemplate.opsForValue().get(redisKey);
        if (cached != null) {
            cached.setUsageCount(cached.getUsageCount() + 1);
            cached.setUpdatedOn(Instant.now());
            redisTemplate.opsForValue().set(redisKey, cached);
            urlRecordRepository.save(cached);
            return new LongUrlResponse(cached.getLongUrl());
        }

        // Fallback to DB if not found in Redis
        Optional<UrlRecord> opt = urlRecordRepository.findByShortUrl(shortUrl);
        if (opt.isEmpty()) {
            throw new RuntimeException(SHORT_URL_NOT_FOUND + shortUrl);
        }
        UrlRecord record = opt.get();
        record.setUsageCount(record.getUsageCount() + 1);
        record.setUpdatedOn(Instant.now());
        urlRecordRepository.save(record);

        redisTemplate.opsForValue().set(redisKey, record);
        return new LongUrlResponse(record.getLongUrl());
    }

    public long getUsageCount(String shortUrl) {
        String redisKey = REDIS_KEY_PREFIX + shortUrl;

        UrlRecord cached = redisTemplate.opsForValue().get(redisKey);
        if (cached != null) {
            return cached.getUsageCount();
        }

        Optional<UrlRecord> opt = urlRecordRepository.findByShortUrl(shortUrl);
        return opt.map(UrlRecord::getUsageCount).orElse(0L);
    }

    private String generateRandomShortKey() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int rand = ThreadLocalRandom.current().nextInt(0, 62);
            if (rand < 10) {
                sb.append(rand);
            } else if (rand < 36) {
                sb.append((char) ('a' + rand - 10));
            } else {
                sb.append((char) ('A' + rand - 36));
            }
        }
        return sb.toString();
    }
}
