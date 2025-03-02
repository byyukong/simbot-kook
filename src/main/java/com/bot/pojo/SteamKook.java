package com.bot.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 
 * @TableName STEAM_KOOK
 */
@TableName(value ="steam_kook")
@Data
@Accessors(chain = true)
public class SteamKook implements Serializable {
    /**
     * ID
     */
    @TableId
    private String id;

    /**
     * kookid
     */
    private String kookId;

    /**
     * steamid
     */
    private String steamId;

    /**
     * 删除标记(0:未删除；1:已删除)
     */
    @TableLogic(value = "0",delval = "1")
    private String deleteFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}