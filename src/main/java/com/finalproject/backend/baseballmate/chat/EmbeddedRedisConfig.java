//package com.finalproject.backend.baseballmate.chat;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import redis.embedded.RedisServer;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//
////@Profile("54.180.148.132")
//@Profile("local")
//@Configuration
//public class EmbeddedRedisConfig {
//    private RedisServer redisServer;
//
//    @PostConstruct
//    public void start() {
//        redisServer = RedisServer.builder()
//                .port(6379)
//                .setting("maxmemory 128M")
//                .build();
//        redisServer.start();
//    }
//
//    @PreDestroy
//    public void stop() {
//        if (redisServer != null) {
//            redisServer.stop();
//        }
//    }
//}