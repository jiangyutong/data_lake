package com.boyoi.core.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class DataCheck {
    /**
     * SHA1加密
     */
    public static String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static String createData(int length) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(rand.nextInt(10));
        }
        String data = sb.toString();
        return data;
    }

    public static String post(String requestUrl, String params, String token) throws Exception {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            //创建连接
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            //设置http连接属性
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 提交 POST请求
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            //设置http头 消息
            //设定 请求格式 application/json
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            //设定响应的信息的格式为application/json
            connection.setRequestProperty("Accept", "application/json");
            //替换成获取到的AccessToken
            connection.setRequestProperty("Authorization", "OPEN-ACCESS-TOKEN AccessToken=" + token);

            connection.connect();
            //添加 请求内容
            OutputStream out = connection.getOutputStream();
            out.write(params.toString().getBytes("utf-8"));
            out.flush();
            out.close();

//            读取响应
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("返回错误");
            }

            reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            reader.close();
            connection.disconnect();
            System.out.println("响应结果:" + sb.toString());
            return sb.toString();

        } catch (Exception e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
