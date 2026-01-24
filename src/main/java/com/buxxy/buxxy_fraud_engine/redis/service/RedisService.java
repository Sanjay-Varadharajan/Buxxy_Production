package com.buxxy.buxxy_fraud_engine.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String,String> redisTemplate;

    public void setValue(String key,String value,long ttlSeconds){
        redisTemplate.opsForValue().set(key,value,ttlSeconds, TimeUnit.SECONDS);
    }

    public String getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

    public boolean exists(String key){
        return redisTemplate.hasKey(key) != null && redisTemplate.hasKey(key);
    }
}