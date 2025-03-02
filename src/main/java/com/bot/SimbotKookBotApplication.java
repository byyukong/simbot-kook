package com.bot;

import love.forte.simbot.spring.EnableSimbot;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSimbot
@MapperScan("com.bot.mapper")
@SpringBootApplication
public class SimbotKookBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimbotKookBotApplication.class, args);
    }

}
