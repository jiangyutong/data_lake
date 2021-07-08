package com.boyoi.core.generator.invoker;


import com.boyoi.core.generator.invoker.base.AbstractBuilder;
import com.boyoi.core.generator.invoker.base.AbstractInvoker;
import com.boyoi.core.generator.invoker.base.Invoker;
import com.boyoi.core.generator.utils.GeneratorUtil;
import com.boyoi.core.generator.utils.StringUtil;

import java.sql.SQLException;

/**
 * 单表构建
 *
 * @author ZhouJL
 * @date 2019/2/14 10:11
 */
public class SingleInvoker extends AbstractInvoker {

    @Override
    protected void getTableInfos() throws SQLException {
        tableInfos = connectionUtil.getMetaData(tableName);
    }

    @Override
    protected void initTasks() {
        taskQueue.initSingleTasks(className, tableName, tableInfos, moduleName);
    }

    public static class Builder extends AbstractBuilder {
        private SingleInvoker invoker = new SingleInvoker();

        public Builder setTableName(String tableName) {
            invoker.setTableName(tableName);
            return this;
        }

        public Builder setClassName(String className) {
            invoker.setClassName(className);
            return this;
        }

        public Builder setModuleName(String moduleName) {
            invoker.setModuleName(moduleName);
            return this;
        }

        @Override
        public Invoker build() {
            if (!isParamtersValid()) {
                return null;
            }
            return invoker;
        }

        @Override
        public void checkBeforeBuild() throws Exception {
            if (StringUtil.isBlank(invoker.getTableName())) {
                throw new Exception("Expect table's name, but get a blank String.");
            }
            if (StringUtil.isBlank(invoker.getClassName())) {
                invoker.setClassName(GeneratorUtil.generateClassName(invoker.getTableName()));
            }
        }
    }

}
