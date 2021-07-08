package com.boyoi.work.service.impl;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.boyoi.work.dao.JsonArrayServiceDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class JsonArrayServiceImpl {

    @Resource
    private JsonArrayServiceDao dao;

    int addBatch(JSONArray jsonArray, String tableName, int numToInsert) {
        int start = 0;
        int end = (int) (jsonArray.size() / numToInsert);
        int changedNum = 0;
        for (int i = 0; i < numToInsert; i++) {
            String lineColumn = getColumnNames((JSONObject) jsonArray.get(0));
            Map insertMap = new HashMap();
            insertMap.put("lineColumn", lineColumn);
            insertMap.put("tableName", tableName);
            List<Map> lineList = new ArrayList();
            for (int j = start; j < end; j++) {
                Map value = new HashMap();
                JSONObject jobj = jsonArray.getJSONObject(j);
                for (String key : jobj.keySet()) {
                    value.put(key, jobj.get(key));
                }
                lineList.add(value);
            }
            insertMap.put("lineList", lineList);
            changedNum += dao.addBatch(insertMap);
            start = end;
            if (i == numToInsert - 1) {
                end = jsonArray.size();
            } else {
                end = (int) (i + 2) * (jsonArray.size() / numToInsert);
            }
        }
        return changedNum;
    }

    String getColumnNames(JSONObject jobj) {
        String ColumnNames = new String();
        for (String key : jobj.keySet()) {
            ColumnNames += key + ",";
        }
        ColumnNames.substring(0, ColumnNames.length() - 1);
        return ColumnNames;
    }

}
