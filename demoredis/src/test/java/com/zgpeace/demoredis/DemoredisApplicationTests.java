package com.zgpeace.demoredis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoredisApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() throws Exception {
        String key = "yourState";
        String value = "passion";
        stringRedisTemplate.opsForValue().set(key, value);
        Assert.assertEquals("passion", stringRedisTemplate.opsForValue().get(key));
    }

    @Test
    public void contextLoads() {
    }

}

