package com.bot.pojo.steam.vo;

import lombok.Data;

import java.util.List;

@Data
public class GetRecentlyPlayedGamesVo {

    private List<SteamGameVo> games;

    private String totalCount;

}
