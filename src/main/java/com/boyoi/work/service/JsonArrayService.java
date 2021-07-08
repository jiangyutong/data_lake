package com.boyoi.work.service;


import cn.hutool.json.JSONArray;

public interface JsonArrayService {
    /**
     * 传入JsonArray数组时，插入到相对应的表中
     *
     * @param jsonArray   传入的json数据
     * @param tableName   数据库表的名字
     * @param numToInsert 分多少次插入数据库
     * @return 总共插入数据库的条数
     */
    int addBatch(JSONArray jsonArray, String tableName, int numToInsert);
}
