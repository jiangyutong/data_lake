package com.boyoi.core.controller;

import com.boyoi.core.entity.Result;
import com.boyoi.core.enums.ResultCode;
import com.boyoi.core.multi.MultiRequestBody;
import com.boyoi.core.util.FtpUtil;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * ftp 文件上传
 *
 * @author ZhouJL
 * @date 2020/5/7 10:08
 */
@RestController
@RequestMapping(value = "/fileFtp")
public class FileFtpController {

    @PostMapping("/upload")
    public Result upload(@RequestParam("files") MultipartFile[] files, String folder) {
        Assert.notNull(files, ResultCode.DATA_FILE_BLANK.getMessage());
        Assert.isTrue(files.length != 0, ResultCode.DATA_FILE_BLANK.getMessage());
        Assert.isTrue(files.length <= 5, ResultCode.DATA_IS_MAX.getMessage());
        for (MultipartFile file : files) {
            Assert.isTrue(file.getSize() > 0, ResultCode.DATA_FILE_NULL.getMessage());
        }
        Assert.notNull(folder, ResultCode.DATA_FOLDER_NULL.getMessage());
        return Result.success(FtpUtil.uploadFile(folder, files));
    }

    @PostMapping("/download")
    public void download(@RequestBody Map<String, String> param, HttpServletResponse response) {
        FtpUtil.downloadFile(param.get("filePath"), response);
    }

    @PostMapping("/downloadBatch")
    public void downloadBatch(@MultiRequestBody List<String> filePaths, HttpServletResponse response) {
        FtpUtil.downloadFiles(filePaths, response);
    }
}
