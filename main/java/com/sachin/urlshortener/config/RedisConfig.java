package com.sachin.urlshortener.config;

import com.sachin.urlshortener.model.UrlRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private Integer redisPort;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(redisHost);
        factory.setPort(redisPort);
        return factory;
    }

    @Bean
    public RedisTemplate<String, UrlRecord> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, UrlRecord> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new RedisUrlRecordSerializer());

        return template;
    }
}
