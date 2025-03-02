package com.bot.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName system_log
 */
@TableName(value ="system_log")
@Data
@Accessors(chain = true)
public class SystemLog implements Serializable {
    /**
     * ID
     */
    private String id;

    /**
     * 触发的动作
     */
    private String operateName;

    /**
     * 操作用户ID
     */
    private String operateUserId;

    /**
     * 操作用户名
     */
    private String operateUserName;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 0成功/1失败
     */
    private Integer operateResult;

    /**
     * 操作失败原因
     */
    private String operateFailReason;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}