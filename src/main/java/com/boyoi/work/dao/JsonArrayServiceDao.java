package com.boyoi.work.dao;


import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface JsonArrayServiceDao {
    int addBatch(@Param("insertMap") Map insertMap);
}
