package com.boyoi.core.config;

import com.boyoi.core.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 枚举转换的工厂类
 *
 * @author ZhouJL
 * @date 2019/5/27 19:28
 */
@Component
public class EnumConvertFactory implements ConverterFactory<String, BaseEnum> {

    @SuppressWarnings("rawtypes")
    private static final Map<Class, Converter> CONVERTER_MAP = new WeakHashMap<>();

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        Converter result = CONVERTER_MAP.get(targetType);
        if (result == null) {
            result = new IntegerStrToEnum<T>(targetType);
            CONVERTER_MAP.put(targetType, result);
        }
        return result;
    }

    class IntegerStrToEnum<T extends BaseEnum> implements Converter<String, T> {
        @SuppressWarnings("unused")
        private final Class<T> enumType;
        private Map<String, T> enumMap = new HashMap<>();

        public IntegerStrToEnum(Class<T> enumType) {
            this.enumType = enumType;
            T[] enums = enumType.getEnumConstants();
            for (T e : enums) {
                enumMap.put(e.getValue() + "", e);
            }
        }


        @Override
        public T convert(String source) {
            T result = enumMap.get(source);
            if (result == null) {
                throw new IllegalArgumentException("No element matches " + source);
            }
            return result;
        }
    }
}
