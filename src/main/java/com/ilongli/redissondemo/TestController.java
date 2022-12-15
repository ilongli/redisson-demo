package com.ilongli.redissondemo;

import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author ilongli
 * @date 2022/12/15 10:14
 */
@RestController
public class TestController {

    @Resource
    private RedissonClient redisson;

    /**
     * 加的锁自动会有一个30秒的TTL，所以就算加锁的服务器宕机了，最后锁也会被释放
     * @param lock
     * @return
     */
    @GetMapping("test-lock")
    public String testLock(String lock) {

        RLock rLock = redisson.getLock(lock);

        rLock.lock();

        String threadName = Thread.currentThread().getName();

        try {
            System.out.println("[" + threadName + "]获取锁:" + lock);
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            rLock.unlock();
            System.out.println("[" + threadName + "]释放锁:" + lock);
        }

        return "ok:" + lock;
    }

    /**
     * 读写锁
     *
     * 读锁 + 读锁：相当于没加锁，可以并发读。
     * 读锁 + 写锁：写锁需要等待读锁释放锁。
     * 写锁 + 写锁：互斥，需要等待对方的锁释放。
     * 写锁 + 读锁：读锁需要等待写锁释放。
     * @param lock
     * @param rw
     * @return
     */
    @GetMapping("test-rwLock")
    public String testRwLock(String lock, String rw) {

        if (!Objects.equals("r", rw)
            && !Objects.equals("w", rw)
        ) {
            throw new IllegalArgumentException("rw参数非法:" + rw);
        }

        RReadWriteLock readWriteLock = redisson.getReadWriteLock(lock);

        // 加锁
        RLock rLock = null;
        if (Objects.equals("r", rw)) {
            // 读锁
            rLock = readWriteLock.readLock();
        } else {
            // 写锁
            rLock = readWriteLock.writeLock();
        }
        rLock.lock();

        String threadName = Thread.currentThread().getName();

        try {
            System.out.println("[" + threadName + "]获取" + rw + "锁:" + lock);
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            rLock.unlock();
            System.out.println("[" + threadName + "]释放" + rw + "锁:" + lock);
        }

        return "ok:" + lock + ":" + rw;
    }


    /**
     * 分布式信号量
     * @param lock
     * @return
     * @throws InterruptedException
     */
    @GetMapping("test-semaphore")
    public String testSemaphore(String lock) throws InterruptedException {

        RSemaphore semaphore = redisson.getSemaphore(lock);

        semaphore.acquire();

        String threadName = Thread.currentThread().getName();

        try {
            System.out.println("[" + threadName + "]获取锁:" + lock);
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
            System.out.println("[" + threadName + "]释放锁:" + lock);
        }

        return "ok:" + lock;
    }




}
