package com.bot.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName API
 */
@TableName(value ="api")
@Data
public class Api implements Serializable {
    /**
     * ID
     */
    @TableId
    private String id;

    /**
     * 接口KEY
     */
    private String appKey;

    /**
     * 接口URL
     */
    private String appUrl;

    /**
     * 备注
     */
    private String appRemark;

    /**
     * 删除标记(0:未删除；1:已删除)
     */
    @TableLogic(value = "0",delval = "1")
    private String deleteFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}