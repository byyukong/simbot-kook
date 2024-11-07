package com.bot.config;

import love.forte.simbot.component.kook.event.KookChannelMessageEvent;
import love.forte.simbot.event.ChatChannelMessageEvent;
import love.forte.simbot.quantcat.common.annotations.ContentTrim;
import love.forte.simbot.quantcat.common.annotations.Filter;
import love.forte.simbot.quantcat.common.annotations.Listener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class MyHandles {
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
    @Filter("你好")
    public CompletableFuture<?> onMessage(KookChannelMessageEvent event) {
        System.out.println("KookChannelMessageEvent: " + event);
        return event.replyAsync("你也好");
        // 可以直接返回任意 Future 类型，
        // 或者返回 EventResult，其中包裹着 Future 类型。
    }
}