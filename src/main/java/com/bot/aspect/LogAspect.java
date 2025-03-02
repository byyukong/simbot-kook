package com.bot.aspect;

import com.alibaba.fastjson.JSON;
import com.bot.annotation.Operation;
import com.bot.mapper.SystemLogMapper;
import com.bot.pojo.SystemLog;
import love.forte.simbot.component.kook.event.KookChannelMessageEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Component
@Aspect
public class LogAspect {

    private final SystemLogMapper systemLogMapper;

    public LogAspect(SystemLogMapper systemLogMapper) {
        this.systemLogMapper = systemLogMapper;
    }

 
    @Pointcut(value = "@annotation(com.bot.annotation.Operation)")
    private void pointCut() {
    }
 
    @Around(value = "pointCut()")
    public Object recordLog(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        Operation annotation = method.getAnnotation(Operation.class);
        String value;
        if (!Objects.isNull(annotation)) {
            value = annotation.value();
            Object[] args = pjp.getArgs();
            KookChannelMessageEvent event = (KookChannelMessageEvent) args[0];

            systemLogMapper.insert(
                    new SystemLog()
                            .setId(UUID.randomUUID().toString().replace("-",""))
                            .setOperateName(value)
                            .setOperateUserId(event.getAuthor().getId().toString())
                            .setOperateUserName(event.getAuthor().getName())
                            .setOperateTime(new Date())
                            .setOperateResult(0)
            );

        }
        return pjp.proceed();
    }
}