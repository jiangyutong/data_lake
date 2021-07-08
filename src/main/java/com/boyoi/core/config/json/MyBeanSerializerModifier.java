package com.boyoi.core.config.json;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.List;
import java.util.Set;

/**
 * 该类控制将null值处理成空集合还是空字符串
 *
 * @author ZhouJL
 * @date 2019/3/28 16:32
 */
public class MyBeanSerializerModifier extends BeanSerializerModifier {

    /**
     * 数组类型
     */
    private JsonSerializer nullArrayJsonSerializer = new MyNullArrayJsonSerializer();
    /**
     * 字符串等类型
     */
    private JsonSerializer nullJsonSerializer = new MyNullJsonSerializer();

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List beanProperties) {
        //循环所有的beanPropertyWriter
        for (Object beanProperty : beanProperties) {
            BeanPropertyWriter writer = (BeanPropertyWriter) beanProperty;
            //判断字段的类型，如果是array，list，set则注册nullSerializer
            if (isArrayType(writer)) {
                //给writer注册一个自己的nullSerializer
                writer.assignNullSerializer(this.nullArrayJsonSerializer);
            } else {
                writer.assignNullSerializer(this.nullJsonSerializer);
            }
        }
        return beanProperties;
    }

    /**
     * 判断是什么类型
     */
    private boolean isArrayType(BeanPropertyWriter writer) {
        Class clazz = writer.getPropertyType();
        return clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class);
    }

}
