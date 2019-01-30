package com.zgpeace.demoredis.dao;

import com.zgpeace.demoredis.bean.City;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CityRedisConfig {

    @Bean
    RedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, City> redisTemplate(RedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, City> template = new RedisTemplate<String, City>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new RedisObjedctSerializer());
        return template;
    }
}

















