package com.anurag.notifyhub.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
  final private StringRedisTemplate stringRedisTemplate;

  public RedisService(StringRedisTemplate stringRedisTemplate) {
    this.stringRedisTemplate = stringRedisTemplate;
  }

  public boolean exists(String key) {
    return stringRedisTemplate.hasKey(key);
  }

  public void save(String key, long ttlInHours) {
    stringRedisTemplate.opsForValue().set(key,
        "processed", ttlInHours, TimeUnit.HOURS);
  }

}
