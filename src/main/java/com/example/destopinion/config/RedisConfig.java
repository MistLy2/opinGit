package com.example.destopinion.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.Config;
import org.redisson.Redisson;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
@EnableRedisRepositories
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//
//        // 使用Jackson2JsonRedisSerialize 替换默认序列化
//        @SuppressWarnings("rawtypes")
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//
//        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        // 设置value的序列化规则和 key的序列化规则
//
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//
//
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
        RedisTemplate<Object,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        return redisTemplate;
    }

//    @Bean
//    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory connectionFactory){
//    RedisTemplate<Object,Object> redisTemplate=new RedisTemplate<>();
//    redisTemplate.setKeySerializer(new StringRedisSerializer());
//    redisTemplate.setValueSerializer(new StringRedisSerializer());
//    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//    redisTemplate.setHashValueSerializer(new StringRedisSerializer());
//    redisTemplate.setDefaultSerializer(new StringRedisSerializer());
//    redisTemplate.setConnectionFactory(connectionFactory);
//
//    return redisTemplate;
//}

    @Bean//保证注入能够拿到DefaultRedisScript
    public DefaultRedisScript<Boolean> deductMyStock() {
        DefaultRedisScript<Boolean> script = new DefaultRedisScript<>();
        script.setResultType(Boolean.class);
        script.setScriptSource( new ResourceScriptSource(new ClassPathResource("like.lua")) );
        return script;
    }

}
