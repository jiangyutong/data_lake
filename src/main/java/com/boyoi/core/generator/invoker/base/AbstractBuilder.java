package com.boyoi.core.generator.invoker.base;

/**
 * 初始构建
 *
 * @author ZhouJL
 * @date 2019/2/14 10:09
 */
public abstract class AbstractBuilder {

    /**
     * 构建
     *
     * @return 构建内容
     */
    public abstract Invoker build();

    public boolean isParamtersValid() {
        try {
            checkBeforeBuild();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 构建后
     *
     * @throws Exception
     */
    public abstract void checkBeforeBuild() throws Exception;

}
