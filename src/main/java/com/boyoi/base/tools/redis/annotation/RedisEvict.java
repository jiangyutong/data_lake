package com.boyoi.base.tools.redis.annotation;

import java.lang.annotation.*;

/**
 * <p>File：RedisEvict.java</p>
 * <p>Title: redis删除注解</p>
 * <p>Description:</p>
 *
 * @author ZhouJL
 * @version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisEvict {
    String key();

    String fieldKey();
}
