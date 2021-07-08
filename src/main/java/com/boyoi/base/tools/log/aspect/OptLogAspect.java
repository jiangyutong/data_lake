package com.boyoi.base.tools.log.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.boyoi.base.service.OptLogService;
import com.boyoi.base.tools.log.annotation.OptLogAnnotation;
import com.boyoi.base.tools.log.enums.BusinessStatus;
import com.boyoi.core.entity.OptLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志记录处理
 *
 * @author ZhouJL
 */
@Aspect
@Slf4j
@Component
public class OptLogAspect {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private OptLogService optLogService;

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.boyoi.base.tools.log.annotation.OptLogAnnotation)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint) {
        handleLog(joinPoint, null);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e);
    }

    private void handleLog(final JoinPoint joinPoint, final Exception e) {
        try {
            // 获得注解
            OptLogAnnotation controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            OptLog optLog = new OptLog();
            optLog.setStatus(BusinessStatus.SUCCESS.ordinal());

            optLog.setOperUrl(request.getRequestURI());

            if (e != null) {
                optLog.setStatus(BusinessStatus.FAIL.ordinal());
                optLog.setErrorMsg(StrUtil.sub(e.getMessage(), 0, 2000));
            }
            String token = request.getHeader("token");
            if (!StrUtil.isEmpty(token)) {
                Object user = redisTemplate.opsForValue().get(token);
                optLog.setOperName(JSONUtil.parseObj(user).get("userName").toString());
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            optLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            optLog.setRequestMethod(request.getMethod());
            // 处理设置注解上的参数
            Object[] args = joinPoint.getArgs();
            getControllerMethodDescription(controllerLog, optLog, args);
            if (optLog.getOperParam().length() > 2048) {
                optLog.setOperParam("请求参数过多，不做记录。");
            }
            String errorMsg = optLog.getErrorMsg();
            if (errorMsg != null && errorMsg.length() > 2048) {
                optLog.setOperParam("错误信息过长，不做记录。");
            }
            // 加入消息队列
//            amqpTemplate.convertAndSend("logQueue", JSONUtil.parse(optLog));
            //存储操作日志
            optLogService.add(optLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log    日志
     * @param optLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(OptLogAnnotation log, OptLog optLog, Object[] args) throws Exception {
        // 设置action动作
        optLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        optLog.setTitle(log.title());
        // 设置操作人类别
        optLog.setOperatorType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息
            setRequestValue(optLog, args);
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param optLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(OptLog optLog, Object[] args) throws Exception {
        List<?> param = new ArrayList<>(Arrays.asList(args)).stream().filter(p -> !(p instanceof ServletResponse))
                .collect(Collectors.toList());
        log.debug("args:{}", param);
        String params = JSONUtil.toJsonStr(param);
        optLog.setOperParam(params);
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private OptLogAnnotation getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(OptLogAnnotation.class);
        }
        return null;
    }
}
