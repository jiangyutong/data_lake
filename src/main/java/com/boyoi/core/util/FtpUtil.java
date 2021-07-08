package com.boyoi.core.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.net.ftp.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ftp上传下载工具类
 *
 * @author ZhouJL
 * @date 2018/12/24 16:58
 */
@ComponentScan(basePackages = {"cn.hutool.extra.spring"})
public class FtpUtil {

    /**
     * 文件服务器段口
     */
    private static final int PORT = 21;
    /**
     * 文件服务器基础目录
     */
    private static final String BASE_PATH = "/home/ftpuser/files";

    public static String upload(String context, String filePath, String fileName) {
        FTPClient ftp = new FTPClient();
        try {
            String host = SpringUtil.getProperty("ftp.host");
            String userName = SpringUtil.getProperty("ftp.userName");
            String password = SpringUtil.getProperty("ftp.password");
            int reply;
            // 连接FTP服务器
            ftp.connect(host, PORT);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            // 登录
            ftp.login(userName, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return null;
            }
            ftp.setControlEncoding("UTF-8");
            ftp.setBufferSize(1024 * 1024 * 10);
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);

            conf.setServerLanguageCode("zh");
            ftp.enterLocalPassiveMode();
            String active = SpringUtil.getProperty("spring.profiles.active");
            if ("dev".equals(active) || "test".equals(active) || "test2".equals(active)) {
                filePath = "/test/dataLake/" + filePath + "/" + DateUtil.format(new Date(), "yyyy") + "/" + DateUtil.format(new Date(), "MM") + "/" + DateUtil.format(new Date(), "dd");
            } else {
                filePath = "/prod/dataLake/" + filePath + "/" + DateUtil.format(new Date(), "yyyy") + "/" + DateUtil.format(new Date(), "MM") + "/" + DateUtil.format(new Date(), "dd");
            }
            //切换到上传目录
            if (!ftp.changeWorkingDirectory(BASE_PATH + filePath)) {
                //如果目录不存在创建目录
                String[] dirs = ("files/" + filePath).split("/");
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) {
                        continue;
                    }
                    if (!ftp.changeWorkingDirectory(dir)) {
                        if (!ftp.makeDirectory(dir)) {
                            return null;
                        } else {
                            ftp.changeWorkingDirectory(dir);
                        }
                    }
                }
            }
            //设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            /**
             * FTP协议里面，规定文件名编码为iso-8859-1
             */
            if (!ftp.storeFile(new String(fileName.getBytes(StandardCharsets.UTF_8),
                    StandardCharsets.ISO_8859_1), new ByteArrayInputStream(context.getBytes()))) {
                return null;
            }
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return filePath + "/" + fileName;
    }


    /**
     * Description: 向FTP服务器上传文件
     *
     * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
     * @param files    文件对象
     * @return 成功返回true，否则返回false
     */
    public static List<String> uploadFile(String filePath, MultipartFile[] files) {
        FTPClient ftp = new FTPClient();
        List<String> paths = new ArrayList<>();
        try {
            String host = SpringUtil.getProperty("ftp.host");
            String userName = SpringUtil.getProperty("ftp.userName");
            String password = SpringUtil.getProperty("ftp.password");
            int reply;
            // 连接FTP服务器
            ftp.connect(host, PORT);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            // 登录
            ftp.login(userName, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return null;
            }
            ftp.setControlEncoding("UTF-8");
            ftp.setBufferSize(1024 * 1024 * 10);
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);

            conf.setServerLanguageCode("zh");
            ftp.enterLocalPassiveMode();
            String active = SpringUtil.getProperty("spring.profiles.active");
            if ("dev".equals(active) || "test".equals(active) || "test2".equals(active)) {
                filePath = "/test/dataLake/" + filePath + "/" + DateUtil.format(new Date(), "yyyy") + "/" + DateUtil.format(new Date(), "MM") + "/" + DateUtil.format(new Date(), "dd");
            } else {
                filePath = "/prod/dataLake/" + filePath + "/" + DateUtil.format(new Date(), "yyyy") + "/" + DateUtil.format(new Date(), "MM") + "/" + DateUtil.format(new Date(), "dd");
            }
            //切换到上传目录
            if (!ftp.changeWorkingDirectory(BASE_PATH + filePath)) {
                //如果目录不存在创建目录
                String[] dirs = ("files/" + filePath).split("/");
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) {
                        continue;
                    }
                    if (!ftp.changeWorkingDirectory(dir)) {
                        if (!ftp.makeDirectory(dir)) {
                            return null;
                        } else {
                            ftp.changeWorkingDirectory(dir);
                        }
                    }
                }
            }
            //设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            for (MultipartFile file : files) {
                String type = file.getOriginalFilename().substring(
                        // 取文件格式后缀名
                        Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf("."));
                String fileNameHead = file.getOriginalFilename().substring(0,
                        file.getOriginalFilename().lastIndexOf("."));
                fileNameHead = fileNameHead.replaceAll(" ", "");
                // 取当前时间戳和模块名作为文件名
                String fileName = fileNameHead + "_" + System.currentTimeMillis() + type;
                //上传文件
                /**
                 * FTP协议里面，规定文件名编码为iso-8859-1
                 */
                if (!ftp.storeFile(new String(fileName.getBytes(StandardCharsets.UTF_8),
                        StandardCharsets.ISO_8859_1), file.getInputStream())) {
                    return null;
                }
                paths.add(filePath + "/" + fileName);
            }
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return paths;
    }


    /**
     * Description: 从FTP服务器下载文件
     *
     * @return
     */
    public static HttpServletResponse downloadFile(String filePath, HttpServletResponse response) {
        FTPClient ftp = new FTPClient();
        InputStream fis = null;
        try {
            String host = SpringUtil.getProperty("ftp.host");
            String userName = SpringUtil.getProperty("ftp.userName");
            String password = SpringUtil.getProperty("ftp.password");
            int reply;
            ftp.connect(host, PORT);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            // 登录
            ftp.login(userName, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return null;
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.setBufferSize(1024 * 1024 * 10);
            ftp.setControlEncoding("UTF-8");
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
            conf.setServerLanguageCode("zh");
            ftp.enterLocalPassiveMode();
            // 转移到FTP服务器目录
            String cloudFileName = StrUtil.subAfter(filePath, "/", true);
            fis = ftp.retrieveFileStream(BASE_PATH + new String(filePath.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            byte[] buffer = new byte[1024];
            response.reset();
            int len;
            while ((len = fis.read(buffer)) > 0) {
                response.getOutputStream().write(buffer, 0, len);
            }
            fis.close();
            ftp.completePendingCommand();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(cloudFileName.getBytes()));
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("application/octet-stream");
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return response;
    }

    /**
     * Description: 从FTP服务器批量下载文件
     *
     * @return
     */
    public static HttpServletResponse downloadFiles(List<String> filePaths, HttpServletResponse response) {
        String downloadFilename = System.currentTimeMillis() + ".zip";
        FTPClient ftp = new FTPClient();
        try {
            String host = SpringUtil.getProperty("ftp.host");
            String userName = SpringUtil.getProperty("ftp.userName");
            String password = SpringUtil.getProperty("ftp.password");
            int reply;
            ftp.connect(host, PORT);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            // 登录
            ftp.login(userName, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return null;
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.setBufferSize(1024 * 1024 * 10);
            ftp.setControlEncoding("UTF-8");
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
            conf.setServerLanguageCode("zh");
            ftp.enterLocalPassiveMode();
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(downloadFilename.getBytes()));
            response.setContentType("application/octet-stream");
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            for (int i = 0; i < filePaths.size(); i++) {
                String cloudFileName = StrUtil.subAfter(filePaths.get(i), "/", true);
                zos.putNextEntry(new ZipEntry(cloudFileName));
                InputStream fis = ftp.retrieveFileStream(BASE_PATH + new String(filePaths.get(i).getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                fis.close();
                ftp.completePendingCommand();
            }
            zos.flush();
            zos.close();
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return response;
    }


    /**
     * Description: 从FTP服务器下载图片转BASE64
     *
     * @return
     */
    public static String downloadFileToBase64(String filePath) {
        String returnStr = "";
        FTPClient ftp = new FTPClient();
        InputStream fis = null;
        try {
            String host = SpringUtil.getProperty("ftp.host");
            String userName = SpringUtil.getProperty("ftp.userName");
            String password = SpringUtil.getProperty("ftp.password");
            int reply;
            ftp.connect(host, PORT);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            // 登录
            ftp.login(userName, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return null;
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.setBufferSize(1024 * 1024 * 10);
            ftp.setControlEncoding("UTF-8");
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
            conf.setServerLanguageCode("zh");
            ftp.enterLocalPassiveMode();
            // 转移到FTP服务器目录
            fis = ftp.retrieveFileStream(BASE_PATH + new String(filePath.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            byte[] data = null;

            // 读取图片字节数组
            data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            ftp.completePendingCommand();
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            returnStr = "data:image/png;base64," + encoder.encode(Objects.requireNonNull(data));
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return returnStr;
    }

    /**
     * Description: 从FTP服务器下载图片
     *
     * @return 返回byte
     */
    public static byte[] downloadFileToByte(String filePath) {
        FTPClient ftp = new FTPClient();
        InputStream fis;
        byte[] data = null;
        byte[] data2 = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            String host = SpringUtil.getProperty("ftp.host");
            String userName = SpringUtil.getProperty("ftp.userName");
            String password = SpringUtil.getProperty("ftp.password");
            int reply;
            ftp.connect(host, PORT);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            // 登录
            ftp.login(userName, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return null;
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.setBufferSize(1024 * 1024 * 10);
            ftp.setControlEncoding("UTF-8");
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
            conf.setServerLanguageCode("zh");
            ftp.enterLocalPassiveMode();
            // 转移到FTP服务器目录
            fis = ftp.retrieveFileStream(BASE_PATH + new String(filePath.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            // 读取图片字节数组
            data = new byte[1024];
            int len1;
            while (-1 != (len1 = fis.read(data))) {
                bos.write(data, 0, len1);
            }

            data2 = bos.toByteArray();

            fis.close();
            ftp.completePendingCommand();
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return data2;
    }
}


