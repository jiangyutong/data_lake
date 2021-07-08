package com.boyoi.core.util;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLParserUtil {

    /**
     * 将语句中的#{xxx}用？代替，${xxx}用params中的xxx的值代替
     * 之后调用JdbcUtil工具，把resultMap的params传入即可
     *
     * @param sql    需要解析的语句   sql:select * from user_${number} where id = #{id}
     * @param params 语句中的参数  params:{number:2,id:10}
     * @return 解析后的sql语句和#{}的参数值，用map封装起来 例如 {"sql":"select * from user_2 where id = ?","params":{"id":10}}
     */
    public static Map SQLParser(String sql, Map params) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            BoundSql boundSql = getBoundSql(sql, params);
            resultMap.put("sql", boundSql.getSqlText());
            Map<String, Object> paramsMap = new HashMap<>();
            for (ParameterMapping pm : boundSql.getParameterMappingList()) {
                if (params.keySet().contains(pm.getContent())) {
                    paramsMap.put(pm.getContent(), params.get(pm.getContent()));
                }
            }
            resultMap.put("params", paramsMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public static BoundSql getBoundSql(String sql, Map params) throws Exception {
        //创建一个params占位符,用来返回#{}中的符号，无参构造器
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        //创建一个动态解析器
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //解析出来的sql
        String parseSql = genericTokenParser.parse(sql);
        //创建一个params占位符，用来返回${}中的符号,带参构造器
        ParameterMappingTokenHandler parameterMappingTokenHandler1 = new ParameterMappingTokenHandler(params);
        GenericTokenParser genericTokenParser1 = new GenericTokenParser("${", "}", parameterMappingTokenHandler1);
        String parseSql1 = genericTokenParser1.parse(parseSql);
        //#{}里面解析出来的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        return new BoundSql(parseSql1, parameterMappings);
    }
}
