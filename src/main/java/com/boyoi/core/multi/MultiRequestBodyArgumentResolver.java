package com.boyoi.core.multi;

import cn.hutool.core.util.StrUtil;
import com.boyoi.core.entity.CommonException;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.util.ValidationUtil;
import org.apache.commons.io.IOUtils;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MultiRequestBody解析器
 * 解决的问题：
 * 1、单个字符串等包装类型都要写一个对象才可以用@RequestBody接收；
 * 2、多个对象需要封装到一个对象里才可以用@RequestBody接收。
 * 主要优势：
 * 1、支持通过注解的value指定JSON的key来解析对象。
 * 2、支持通过注解无value，直接根据参数名来解析对象
 * 3、支持基本类型的注入
 * 4、支持GET和其他请求方式注入
 * 5、支持通过注解无value且参数名不匹配JSON串key时，根据属性解析对象。
 * 6、支持多余属性(不解析、不报错)、支持参数“共用”（不指定value时，参数名不为JSON串的key）
 * 7、支持当value和属性名找不到匹配的key时，对象是否匹配所有属性。
 *
 * @author ZhouJL
 * @date 2019-03-12 13:47:03
 */
public class MultiRequestBodyArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String JSONBODY_ATTRIBUTE = "JSON_REQUEST_BODY";

    /**
     * 设置支持的方法参数类型
     *
     * @param parameter 方法参数
     * @return 支持的类型
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 支持带@MultiRequestBody注解的参数
        return parameter.hasParameterAnnotation(MultiRequestBody.class);
    }

    /**
     * 参数解析，利用fastjson
     * 注意：非基本类型返回null会报空指针异常，要通过反射或者JSON工具类创建一个空对象
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String jsonBody = getRequestBody(webRequest);
        JSONObject jsonObject = JSONUtil.parseObj(jsonBody);
        if (jsonObject == null) {
            throw new CommonException(ResultCode.PARAM_IS_BLANK);
        }
        MultiRequestBody parameterAnnotation = parameter.getParameterAnnotation(MultiRequestBody.class);
        String key = parameterAnnotation != null ? parameterAnnotation.value() : null;
        Object value;
        if (StringUtils.isNotEmpty(key)) {
            value = jsonObject.get(key);
            if (value == null && parameterAnnotation.required()) {
                throw new IllegalArgumentException(String.format("required param %s is not present", key));
            }
        } else {
            key = parameter.getParameterName();
            value = jsonObject.get(key);
        }
        Class<?> parameterType = parameter.getParameterType();
        if (value != null) {
            if (parameterType.isPrimitive()) {
                return parsePrimitive(parameterType.getName(), value);
            }
            if (isBasicDataTypes(parameterType)) {
                return parseBasicTypeWrapper(parameterType, value);
            } else if (parameterType == String.class) {
                if (StrUtil.isBlank(value.toString().trim())) {
                    throw new CommonException(ResultCode.PARAM_IS_BLANK);
                } else {
                    return value.toString();
                }
            } else if (parameterType == List.class) {
                if (parameterAnnotation.groups().length != 0) {
                    List list = JSONUtil.toBean(JSONUtil.parseObj(value.toString()), parameter.getGenericParameterType(), false);
                    for (Object o : list) {
                        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(o, parameterAnnotation.groups());
                        if (validResult.hasErrors()) {
                            throw new CommonException(ResultCode.PARAM_ERROR).setData(validResult.getAllErrors());
                        }
                    }
                    return JSONUtil.toBean(JSONUtil.parseObj(value.toString()), parameter.getGenericParameterType(), false);
                } else {
                    return JSONUtil.parseArray(value.toString());
                }
            }
            return JSONUtil.toBean(JSONUtil.parseObj(value.toString()), parameterType, false);
        }
        if (isBasicDataTypes(parameterType)) {
            if (parameterAnnotation.required()) {
                throw new IllegalArgumentException(String.format("required param %s is not present", key));
            }
            return null;
        }
        if (parameterType == String.class) {
            if (parameterAnnotation.required()) {
                throw new IllegalArgumentException(String.format("required param %s is not present", key));
            }
            return null;
        }
        if (!parameterAnnotation.parseAllFields()) {
            if (parameterAnnotation.required()) {
                throw new IllegalArgumentException(String.format("required param %s is not present", key));
            }
            return null;
        }
        Object result;
        try {
            result = JSONUtil.toBean(JSONUtil.parseObj(jsonObject.toString()), parameterType, false);
            if (parameterAnnotation.groups().length != 0) {
                ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(result, parameterAnnotation.groups());
                if (validResult.hasErrors()) {
                    throw new CommonException(ResultCode.PARAM_ERROR).setData(validResult.getAllErrors());
                }
            }
        } catch (JSONException jsonException) {
            result = null;
        }
        if (!parameterAnnotation.required()) {
            return result;
        } else {
            boolean haveValue = false;
            Field[] declaredFields = parameterType.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                if (field.get(result) != null) {
                    haveValue = true;
                    break;
                }
            }
            if (!haveValue) {
                throw new IllegalArgumentException(String.format("required param %s is not present", key));
            }
            return result;
        }
    }

    /**
     * 基本类型解析
     */
    private Object parsePrimitive(String parameterTypeName, Object value) {
        final String booleanTypeName = "boolean";
        if (booleanTypeName.equals(parameterTypeName)) {
            return Boolean.valueOf(value.toString());
        }
        final String intTypeName = "int";
        if (intTypeName.equals(parameterTypeName)) {
            return Integer.valueOf(value.toString());
        }
        final String charTypeName = "char";
        if (charTypeName.equals(parameterTypeName)) {
            return value.toString().charAt(0);
        }
        final String shortTypeName = "short";
        if (shortTypeName.equals(parameterTypeName)) {
            return Short.valueOf(value.toString());
        }
        final String longTypeName = "long";
        if (longTypeName.equals(parameterTypeName)) {
            return Long.valueOf(value.toString());
        }
        final String floatTypeName = "float";
        if (floatTypeName.equals(parameterTypeName)) {
            return Float.valueOf(value.toString());
        }
        final String doubleTypeName = "double";
        if (doubleTypeName.equals(parameterTypeName)) {
            return Double.valueOf(value.toString());
        }
        final String byteTypeName = "byte";
        if (byteTypeName.equals(parameterTypeName)) {
            return Byte.valueOf(value.toString());
        }
        return null;
    }

    /**
     * 基本类型包装类解析
     */
    private Object parseBasicTypeWrapper(Class<?> parameterType, Object value) {
        if (Number.class.isAssignableFrom(parameterType)) {
            Number number = (Number) value;
            if (parameterType == Integer.class) {
                return number.intValue();
            } else if (parameterType == Short.class) {
                return number.shortValue();
            } else if (parameterType == Long.class) {
                return number.longValue();
            } else if (parameterType == Float.class) {
                return number.floatValue();
            } else if (parameterType == Double.class) {
                return number.doubleValue();
            } else if (parameterType == Byte.class) {
                return number.byteValue();
            }
        } else if (parameterType == Boolean.class) {
            return value.toString();
        } else if (parameterType == Character.class) {
            return value.toString().charAt(0);
        }
        return null;
    }

    /**
     * 判断是否为基本数据类型包装类
     */
    private boolean isBasicDataTypes(Class clazz) {
        Set<Class> classSet = new HashSet<>();
        classSet.add(Integer.class);
        classSet.add(Long.class);
        classSet.add(Short.class);
        classSet.add(Float.class);
        classSet.add(Double.class);
        classSet.add(Boolean.class);
        classSet.add(Byte.class);
        classSet.add(Character.class);
        return classSet.contains(clazz);
    }

    /**
     * 获取请求体JSON字符串
     */
    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        // 有就直接获取
        String jsonBody = (String) webRequest.getAttribute(JSONBODY_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);
        // 没有就从请求中读取
        if (jsonBody == null) {
            try {
                jsonBody = IOUtils.toString(servletRequest.getReader());
                webRequest.setAttribute(JSONBODY_ATTRIBUTE, jsonBody, NativeWebRequest.SCOPE_REQUEST);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonBody;
    }
}
