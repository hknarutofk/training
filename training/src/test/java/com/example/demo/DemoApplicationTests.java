package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        Date d = new Date(1263370065244368898l);
        System.out.println(d);
        System.out.println(simpleDateFormat.format(d));
        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void oomCrash() {
        List<byte[]> list = new ArrayList<>();
        while (true) {
            list.add(new byte[1024 * 1024]);
            log.debug("{} MB", list.size());
        }
    }

    @Test
    public void oomCrashThread() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<byte[]> list = new ArrayList<>();
                while (true) {
                    list.add(new byte[1024 * 1024]);
                    log.debug("{} MB", list.size());
                }
            }
        });
        thread.start();
        Thread.sleep(99999);
        // 线程退出，进程正常保持
    }

    List<byte[]> list = new ArrayList<>();

    @Test
    public void oomCrashThread2() throws InterruptedException {
        Runnable tinyRun = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    byte[] buff = new byte[1024 * 1024];
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Runnable crashRun = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    list.add(new byte[1024 * 1024]);
                    log.debug("{} MB", list.size());
                }
            }
        };

        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(tinyRun);
            thread.start();
        }
        Thread.sleep(1000);
        Thread crashThread = new Thread(crashRun);
        crashThread.start();
        Thread.sleep(30000);
        // crashThread线程退出，进程正常保持
        log.debug("clear list");
        Thread.sleep(999999);
    }

}
