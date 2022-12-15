package com.ilongli.redissondemo;

import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Resource
    private RedissonClient redissonClient;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        RSemaphore selock = redissonClient.getSemaphore("selock");
        selock.trySetPermits(2);
        System.out.println("设定selock的阈值为：2");

    }

}