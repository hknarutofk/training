package com.example.demo;

import java.lang.reflect.Field;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import sun.misc.Unsafe;

/**
 * 原子操作测试：值类型操作如++非原子性，CPU内有高速缓存，多线程非安全 java.util.concurrent.atomic包下对应有原子操作封装类 以AtomicLong自增为例，CPU消耗远远高于long自增(约2个数量级)
 * 
 * @author yeqiang
 * @since 5/28/20 8:58 AM
 */
public class AtomicOptTest {
    static final long INC_LOOP_COUNT = 100000000l;
    static final Semaphore semaphore = new Semaphore(0);
    static AtomicLong atomicLong = new AtomicLong(0l);
    Runnable counterIncSuccFun = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < INC_LOOP_COUNT; i++) {
                atomicLong.addAndGet(1);
            }
            semaphore.release();
        }
    };
    private volatile long counter = 0l;
    Runnable counterIncFailFun = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < INC_LOOP_COUNT; i++) {
                counter++;
            }
            semaphore.release();
        }
    };

    /**
     * 失败的测试
     * 
     * @throws InterruptedException
     */
    @Test
    public void testMultiThreadCounterIncFail() throws InterruptedException {
        int THREAD_COUNT = 2;
        long result = INC_LOOP_COUNT * THREAD_COUNT;

        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread t = new Thread(counterIncFailFun);
            t.start();
        }

        semaphore.acquire(THREAD_COUNT);
        System.out.println(counter);

    }

    @Test
    public void testMultiThreadCounterIncSucc() throws InterruptedException {

        int THREAD_COUNT = 2;
        long result = INC_LOOP_COUNT * THREAD_COUNT;

        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread t = new Thread(counterIncSuccFun);
            t.start();
        }

        semaphore.acquire(THREAD_COUNT);
        System.out.println(atomicLong.get());

    }

    @Test
    public void singleAtomicLongTest() {
        AtomicLong al = new AtomicLong(0);
        for (int i = 0; i < 100; i++) {
            long result = al.addAndGet(1);
            System.out.println(result);
        }

    }

    /**
     * https://segmentfault.com/a/1190000018037554?utm_source=tag-newest
     * 
     * @throws NoSuchFieldException
     */
    @Test
    public void unsafeTest() throws NoSuchFieldException {
        Unsafe unsafe = null;
        try {
            // 获取 Unsafe 内部的私有的实例化单例对象
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            // 无视权限
            field.setAccessible(true);
            unsafe = (Unsafe)field.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        long offset = unsafe.objectFieldOffset(AtomicOptTest.class.getDeclaredField("counter"));
        System.out.println(offset);
        System.out.println(unsafe.getLong(this, offset));
        /**
         * Unsafe 中提供了一套原子化的判断和值替换 api unsafe.getAndAddLong 内部替换了数值，但是返回的是原始数值
         */
        System.out.println(unsafe.getAndAddLong(this, offset, 1));
        /**
         * 由于unsafe.getAndAddLong 内存直接替换了数值，因此counter已经是+1了
         */
        System.out.println(counter);
    }
}
