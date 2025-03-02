package com.bot;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bot.mapper.ApiMapper;
import com.bot.mapper.SteamKookMapper;
import com.bot.pojo.Api;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SimbotKookBotApplicationTests {

    @Autowired
    private ApiMapper apiMapper;

    @Autowired
    private SteamKookMapper steamKookMapper;

    @Test
    void contextLoads() {
        System.err.println(apiMapper.selectList(new LambdaQueryWrapper<Api>()));
    }

}
