package com.boyoi.core.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取地址类
 *
 * @author zhoujialin
 */
public class AddressUtils {
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    public static String getRealAddressByIp(String ip) {
        String address = "内网IP";
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        String rspStr = HttpUtil.get("http://whois.pconline.com.cn/ip.jsp?ip=" + ip);
        if (StrUtil.isEmpty(rspStr)) {
            log.error("获取地理位置异常 {}", ip);
            return address;
        }
        try {
            address = rspStr.replace("\r", "").replace("\n", "");
            String[] s = address.split(" ");
            if (StrUtil.isEmpty(s[0])) {
                address = s[1];
            } else {
                address = s[0];
            }

        } catch (Exception e) {
            log.error("获取地理位置异常 {}", ip);
        }
        return address;
    }
}
