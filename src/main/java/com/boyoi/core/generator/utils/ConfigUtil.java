package com.boyoi.core.generator.utils;

import com.boyoi.core.generator.entity.Configuration;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.net.URL;

/**
 * 配置文件工具类
 *
 * @author ZhouJL
 * @date 2019/2/14 10:13
 */
public class ConfigUtil {
    private static Configuration configuration;

    static {
        URL url = ConfigUtil.class.getClassLoader().getResource("generator.yaml");
        // 用户未提供配置文件
        if (url.getPath().contains("jar")) {
            System.err.println("Can not find file named 'generator.yaml' at resources path, please make sure that you have defined that file.");
            System.exit(0);
        } else {
            InputStream inputStream = ConfigUtil.class.getClassLoader().getResourceAsStream("generator.yaml");
            Yaml yaml = new Yaml();
            configuration = yaml.loadAs(inputStream, Configuration.class);
        }
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

}
