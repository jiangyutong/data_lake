package com.boyoi.core.generator.application;


import com.boyoi.core.generator.invoker.SingleInvoker;
import com.boyoi.core.generator.invoker.base.Invoker;

/**
 * 代码生成工具启动类
 *
 * @author ZhouJL
 * @date 2019/2/14 10:06
 */
public class Main {

    public static void main(String[] args) {
        single();
    }

    public static void single() {
        Invoker invoker = new SingleInvoker.Builder()
                .setTableName("query_log")
                .setClassName("QueryLog")
                .setModuleName("数据写入日志")
                .build();
        invoker.execute();
    }

}
