package com.ilongli.redissondemo;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RedissonDemoApplicationTests {

    @Resource
    private RedissonClient redissonClient;

    @Test
    void contextLoads() {

        System.out.println(redissonClient);

    }

}
