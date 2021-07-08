package com.boyoi.work.service.impl;


import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IbatisTestImpl {

    public void test() {
        final Map<String, Object> mapper = new HashMap<String, Object>();
        mapper.put("name", "张三");
        mapper.put("pwd", "123456");

        //先初始化一个handler
        TokenHandler handler = new TokenHandler() {
            @Override
            public String handleToken(String content) {
                System.out.println(content);
                return (String) mapper.get(content);
            }
        };
        GenericTokenParser parser = new GenericTokenParser("${", "}", handler);
        System.out.println("************" + parser.parse("用户：${name}，你的密码是:${pwd}"));
        return;
    }
}
