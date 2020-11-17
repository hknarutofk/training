package com.example.demo;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

/**
 * @author yeqiang
 * @since 10/23/20 9:34 AM
 */
public class LockTest {

    static int counter = 0;
    static Lock lock = new ReentrantLock();
    static Semaphore semaphore = new Semaphore(0);

    @Test
    public void testMultiThreadLock() throws InterruptedException, IOException {
        System.out.println(ManagementFactory.getRuntimeMXBean().getName());
        Thread.sleep(10000);
        System.out.println("start");
        int threadNum = 8;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (counter < 1000000000l) {
                    lock.lock();
                    counter++;
                    lock.unlock();
                }
                semaphore.release();
            }
        };
        long start = System.currentTimeMillis();
        for (int i = 0; i < 8; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
        }
        semaphore.acquire(1);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
