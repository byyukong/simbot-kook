package com.bot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bot.pojo.SystemLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 17195
 * @description 针对表【system_log】的数据库操作Mapper
 * @createDate 2024-11-08 00:28:30
 * @Entity com.bot.pojo.Api
 */
@Mapper
public interface SystemLogMapper extends BaseMapper<SystemLog> {
}
