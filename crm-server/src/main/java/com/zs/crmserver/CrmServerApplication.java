package com.zs.crmserver;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@MapperScan(basePackages = { "com.zs.crmserver.mapper" })
@SpringBootApplication
public class CrmServerApplication implements CommandLineRunner {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(CrmServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //springboot项目启动后，把redisTemplate这个Bean修改一下，修改一下key和value的序列化方式

        //设置key序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //对象映射工具，Java对象 和 json对象进行相互转化
        ObjectMapper mapper = new ObjectMapper();
        //设置可见性
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //激活类型
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.EVERYTHING);

        //设置value序列化
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(mapper, Object.class));

        //设置hashKey序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        //设置haskValue序列化
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(mapper, Object.class));
    }
}
