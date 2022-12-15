package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisIdWorker {

    private StringRedisTemplate stringRedisTemplate;

    public RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    // 开始时间戳
    private static final long BEGIN_TIMESTAMP = 1640995200L;

    public long nextId(String keyPrefix) {
        // 生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timeStamp = nowSecond - BEGIN_TIMESTAMP;

        // 生成序列号
        // 获取当前时间，作为key的一部分
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 设置key自增长
        // 泛黄是因为提示可能空指针异常，但此处不会出现空指针异常，不必担心
        // 此处不用包装类Long，是因为后面还要进行运算
        long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);

        // 拼接成id
        // 位运算：时间戳向左移动32位，给序列号留位置
        return timeStamp << 32 | count;
    }

    /*public static void main(String[] args) {
        // 获取开始时间戳
        LocalDateTime time = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        long second = time.toEpochSecond(ZoneOffset.UTC);
        System.out.println("second:" + second);// 1640995200L
    }*/
}
