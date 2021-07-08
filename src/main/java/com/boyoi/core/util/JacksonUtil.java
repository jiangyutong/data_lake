package com.boyoi.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * jackson 工具
 *
 * @author ZhouJL
 * @date 2019/2/22 14:18
 */
public class JacksonUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JacksonUtil.class);
    private static ObjectMapper objectMapper;
    private ObjectMapper objectMapperCustom;

    public JacksonUtil(JsonInclude.Include... include) {
        objectMapperCustom = new ObjectMapper();
        for (JsonInclude.Include i : include)
            objectMapperCustom.setSerializationInclusion(i);
    }

    static {
        objectMapper = new ObjectMapper();
    }

    /**
     * 转化json string
     *
     * @param obj 对象
     * @return string
     */
    public String toJsonCustom(Object obj) {
        try {
            return objectMapperCustom.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{}";
    }

    /**
     * 转化json string
     *
     * @param obj 对象
     * @return string
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{}";
    }

    /**
     * 解析json为对象
     */
    public static <T> T parse(String json, Class<T> clazz) {
        if (json == null || json.length() == 0) {
            return null;
        }

        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        return null;
    }

}
