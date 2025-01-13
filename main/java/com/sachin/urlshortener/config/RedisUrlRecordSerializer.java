package com.sachin.urlshortener.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sachin.urlshortener.model.UrlRecord;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;

import static com.sachin.urlshortener.common.messagecontants.MessageConstants.ERROR_DESERIALIZING_URL_RECORD;
import static com.sachin.urlshortener.common.messagecontants.MessageConstants.ERROR_SERIALIZING_URL_RECORD;

public class RedisUrlRecordSerializer implements RedisSerializer<UrlRecord> {

    private final ObjectMapper objectMapper;

    public RedisUrlRecordSerializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public byte[] serialize(UrlRecord record) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(record);
        } catch (IOException e) {
            throw new SerializationException(ERROR_SERIALIZING_URL_RECORD, e);
        }
    }

    @Override
    public UrlRecord deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }

        try {
            return objectMapper.readValue(bytes, UrlRecord.class);
        } catch (IOException e) {
            throw new SerializationException(ERROR_DESERIALIZING_URL_RECORD, e);
        }
    }

}
