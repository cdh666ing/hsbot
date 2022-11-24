package com.duheng.hsbot;

import com.zhuangxv.bot.EnableBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBot
public class HsbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(HsbotApplication.class, args);
    }

}
