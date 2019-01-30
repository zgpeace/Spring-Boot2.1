package com.zgpeace.demoredis;

import com.zgpeace.demoredis.bean.City;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoredisApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, City> cityRedisTemplate;

    @Test
    public void test() throws Exception {
        String key = "yourState";
        String value = "passion";
        stringRedisTemplate.opsForValue().set(key, value);
        Assert.assertEquals("passion", stringRedisTemplate.opsForValue().get(key));
    }

    @Test
    public void testRedisCity() throws Exception {
        City city = new City("San Jose", "California", "America");
        cityRedisTemplate.opsForValue().set(city.getName(), city);

        city = new City("Vancouver", "British Columbi", "Canada");
        cityRedisTemplate.opsForValue().set(city.getName(), city);

        Assert.assertEquals("California", cityRedisTemplate.opsForValue().get("San Jose").getState());
        Assert.assertEquals("Canada", cityRedisTemplate.opsForValue().get("Vancouver").getCountry());
    }

    @Test
    public void contextLoads() {
    }

}























