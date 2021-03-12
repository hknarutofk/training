package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yeqiang
 * @since 3/10/21 11:08 AM
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RedisClusterTest {
    @Autowired
    RedisTemplate<String, String> template;

    @Test
    public void test() {
        while (true) {
            String key = "t-" + System.currentTimeMillis();
            String value = key;
            template.opsForValue().set(key, value);
            String v = (String) template.opsForValue().get(key);
            if (v == null) {
                throw new RuntimeException("v is null");
            }
            System.out.println("----------------" + v);
        }
    }
}
