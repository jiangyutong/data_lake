package com.boyoi.core.config;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * 自定义Jackson反序列化日期类型时应用的类型转换器,一般用于@RequestBody接受参数时使用
 *
 * @author ZhouJL
 * @date 2019/3/11 17:39
 */
public class DateJacksonConverter extends JsonDeserializer<Date> {
    private static String[] pattern =
            new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.S", "yyyy-MM-dd'T'hh:mm:ss.SSSZ",
                    "yyyy.MM.dd", "yyyy.MM.dd HH:mm", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss.S",
                    "yyyy/MM/dd", "yyyy/MM/dd HH:mm", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss.S"};

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        Date targetDate = null;
        String originDate = p.getText();
        if (StrUtil.isNotEmpty(originDate)) {
            try {
                long longDate = Long.valueOf(originDate.trim());
                targetDate = new Date(longDate);
            } catch (NumberFormatException e) {
                try {
                    targetDate = DateUtils.parseDate(originDate, DateJacksonConverter.pattern);
                } catch (ParseException pe) {
                    throw new HttpMessageNotReadableException(p.getCurrentName() + " : " + p.getText());
                }
            }
        }


        return targetDate;
    }

    @Override
    public Class<?> handledType() {
        return Date.class;
    }
}
