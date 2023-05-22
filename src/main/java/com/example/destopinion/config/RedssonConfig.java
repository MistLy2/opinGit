package com.example.destopinion.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedssonConfig {
    @Bean
    public Redisson redisson(){
        Config config = new Config();

        config.useSingleServer()
                .setAddress("redis://124.223.59.64:6379")
               .setPassword("200211")//注意这里是redis客户端，默认是没有密码的，并不是redis服务端
                .setConnectionPoolSize(50)
                .setIdleConnectionTimeout(10000)
                .setConnectTimeout(3000)
                .setTimeout(3000)
                .setDatabase(0);

        return (Redisson) Redisson.create(config);

    }
}
