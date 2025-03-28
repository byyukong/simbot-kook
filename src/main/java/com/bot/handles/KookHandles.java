package com.bot.handles;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bot.annotation.Operation;
import com.bot.mapper.ApiMapper;
import com.bot.mapper.SteamKookMapper;
import com.bot.pojo.Api;
import com.bot.pojo.SteamKook;
import com.bot.pojo.steam.vo.GetNumberOfCurrentPlayersVo;
import com.bot.pojo.steam.vo.GetRecentlyPlayedGamesVo;
import com.bot.pojo.steam.vo.SteamGameVo;
import com.bot.util.OkHttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.component.kook.event.KookChannelMessageEvent;
import love.forte.simbot.component.kook.message.KookCardMessage;
import love.forte.simbot.event.ChatChannelMessageEvent;
import love.forte.simbot.kook.objects.card.*;
import love.forte.simbot.quantcat.common.annotations.ContentTrim;
import love.forte.simbot.quantcat.common.annotations.Filter;
import love.forte.simbot.quantcat.common.annotations.FilterValue;
import love.forte.simbot.quantcat.common.annotations.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class KookHandles {

    @Autowired
    private ApiMapper apiMapper;

    @Autowired
    private SteamKookMapper steamKookMapper;

    /**
     * 此处是一个标准库中通用的类型：子频道消息事件
     * 在KOOK组件中，它的真实实现类型会是 KookChannelMessageEvent
     */
    @Listener
    public void onChannelMessage(ChatChannelMessageEvent event) {
        System.out.println("ChatChannelMessageEvent: " + event);
    }

    /**
     * 此处监听的是KOOK组件中的专属类型：普通频道内的消息事件。
     * 并且过滤消息：消息中的文本消息去除前后空字符后，等于 '你好'
     */
    @Listener
    @ContentTrim
    @Filter("一言")
    @Operation("一言")
    public CompletableFuture<?> onMessage(KookChannelMessageEvent event) {
        Map<String, Object> map = OkHttpClientUtil.get("https://v1.hitokoto.cn");
        return event.replyAsync(
            new KookCardMessage(
                new CardMessage(
                    List.of(
                        new Card(
                            Theme.SUCCESS,
                            "#aaaaaa",
                            Size.LG,
                            List.of(
                                new CardModule.Section(
                                    CardModule.SectionMode.LEFT,
                                    new CardElement.PlainText(map.get("hitokoto").toString(), true),
                                    null
                                ),
                                new CardModule.Header(
                                        new CardElement.PlainText("-" + map.get("from"), true)
                                )
                            )
                        )
                    )
                )
            )
        );
    }
    @Listener
    @ContentTrim
    @Filter("帮助")
    @Operation("帮助")
    public CompletableFuture<?> help(KookChannelMessageEvent event) {
        return event.replyAsync(
            new KookCardMessage(
                new CardMessage(
                    List.of(
                        new Card(
                            Theme.SUCCESS,
                            "#aaaaaa",
                            Size.LG,
                            List.of(
                                new CardModule.Section(
                                    CardModule.SectionMode.LEFT,
                                    new CardElement.PlainText(
                                """
                                        随机输出句子：一言
                                        绑定Steam：steam bd 你的SteamID
                                        更新Steam绑定：steam change 你的SteamID
                                        查询最近两周游玩信息：steam info
                                        """,
                                    true
                                    ),
                                    null
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    @Listener
    @ContentTrim
    @Filter("steam bd {{steamId}}")
    @Operation("steam bd")
    public CompletableFuture<?> steambd(KookChannelMessageEvent event,@FilterValue("steamId") String steamId) {
        String kookId = event.getAuthor().getId().toString();
        Long steamBdCount = steamKookMapper.selectCount(new LambdaQueryWrapper<SteamKook>().eq(SteamKook::getKookId, kookId));

        // 检查用户是否已经绑定 Steam，并且 SteamID 格式正确
        if (steamBdCount != null && steamBdCount > 0) {
            return event.replyAsync("您已绑定Steam，如需更改绑定请使用steam change 你的SteamID");
        }

        if (steamId.length() == 17) {
            steamKookMapper.insert(
                    new SteamKook()
                            .setId(UUID.randomUUID().toString().replace("-", ""))
                            .setKookId(kookId)
                            .setSteamId(steamId)
            );
            return event.replyAsync("绑定steam成功！");
        }

        return event.replyAsync("格式不正确，格式为：steam bd 你的SteamID");
    }

    @Listener
    @ContentTrim
    @Filter("steam change {{steamId}}")
    @Operation("steam change")
    public CompletableFuture<?> steamChange(KookChannelMessageEvent event,@FilterValue("steamId") String steamId) {
        String kookId = event.getAuthor().getId().toString();
        Long steamBdCount = steamKookMapper.selectCount(new LambdaQueryWrapper<SteamKook>().eq(SteamKook::getKookId, kookId));

        // 检查用户是否已绑定 Steam
        if (steamBdCount != null && steamBdCount > 0) {
            // 检查 SteamID 格式是否正确
            if (steamId.length() == 17) {
                steamKookMapper.update(
                        new LambdaUpdateWrapper<SteamKook>()
                                .set(SteamKook::getSteamId, steamId)
                                .eq(SteamKook::getKookId, kookId)
                );
                return event.replyAsync("Steam ID 更新成功！");
            }
            return event.replyAsync("格式不正确，格式为：steam change 你的SteamID");
        }

        return event.replyAsync("您还未绑定Steam，如需绑定请使用steam bd 你的SteamID");
    }


    @Listener
    @ContentTrim
    @Filter("steam info")
    @Operation("steam info")
    public CompletableFuture<?> steamInfo(KookChannelMessageEvent event) {
        SteamKook steamKook = steamKookMapper.selectOne(new LambdaQueryWrapper<SteamKook>().eq(SteamKook::getKookId, event.getAuthor().getId().toString()));
        if (null != steamKook) {
            Api apiInfoById = apiMapper.selectById("e6a96a429f534e648bcb971d13cbee4d");
            String url = String.format("%s?key=%s&steamid=%s&count=10",apiInfoById.getAppUrl(), apiInfoById.getAppKey(), steamKook.getSteamId());
            Map<String, Object> res = OkHttpClientUtil.get(url.toString());
            GetRecentlyPlayedGamesVo getRecentlyPlayedGamesVo = JSON.parseObject(res.get("response").toString(), GetRecentlyPlayedGamesVo.class);
            log.info("最近两周游玩：{}",JSON.toJSONString(getRecentlyPlayedGamesVo));
            List<CardModule> cardModuleList = new ArrayList<>();
            if (null == getRecentlyPlayedGamesVo.getGames()) {
                return event.replyAsync("未找到最近两周游玩数据！");
            }

            for (SteamGameVo game : getRecentlyPlayedGamesVo.getGames()) {
                /*String onlineNumber = OkHttpClientUtil.get("http://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1?appid=" + game.getAppid()).get("response").toString();
                GetNumberOfCurrentPlayersVo number = JSON.parseObject(onlineNumber, GetNumberOfCurrentPlayersVo.class);*/
                cardModuleList.add(
                    new CardModule.Section(
                        CardModule.SectionMode.LEFT,
                        new CardElement.PlainText(
                                String.format(
                                        "游戏名称：%s\n最近两周游戏时长：%s\n总时长：%s",
                                        game.getName(),
                                        String.format("%.2f",Double.parseDouble(game.getPlaytime2weeks()) / 60),
                                        String.format("%.2f",Double.parseDouble(game.getPlaytimeForever()) / 60)
                                ),
                                true
                        )/*,
                        new CardElement.Image(
                                "https://steamcdn-a.akamaihd.net/steam/apps/" + game.getAppid() + "/header.jpg",
                                "",
                                Size.LG,
                                false
                        )*/
                    )
                );
            }

            return event.replyAsync(
                new KookCardMessage(
                    new CardMessage(
                        List.of(
                            new Card(
                                    Theme.SUCCESS,
                                    "#aaaaaa",
                                    Size.LG,
                                    cardModuleList
                            )
                        )
                    )
                )
            );
        }

        return event.replyAsync("请先使用：steam bd 绑定steam！");

    }
}