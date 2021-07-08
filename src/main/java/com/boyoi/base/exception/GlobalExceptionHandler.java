package com.boyoi.base.exception;

import com.boyoi.core.entity.CommonException;
import com.boyoi.core.entity.ParameterInvalidItem;
import com.boyoi.core.entity.Result;
import com.boyoi.core.enums.ResultCode;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理类
 *
 * @author ZhouJL
 * @date 2018/12/26 14:42
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 400 - Bad Request 参数缺少
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingServletRequestParameterException(HttpServletRequest req, MissingServletRequestParameterException e) {
        log.warn("自动捕获异常，参数缺少! 请求URI: {}, 异常信息: {}", req.getRequestURI(), e.getMessage() + " (" + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber() + ")");
        e.printStackTrace();
        return Result.failure(ResultCode.PARAM_NOT_COMPLETE);
    }

    /**
     * 400 - Bad Request 参数解析失败
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadableException(HttpServletRequest req, HttpMessageNotReadableException e) {
        log.warn("自动捕获异常，参数解析失败! 请求URI: {}, 异常信息: {}", req.getRequestURI(), e.getMessage() + " (" + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber() + ")");
        e.printStackTrace();
        return Result.failure(ResultCode.PARAM_ERROR);
    }

    /**
     * 400 - Bad Request 参数校验失败
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException e) {
        e.printStackTrace();
        List<Object> parameterInvalidItemList = new ArrayList<>();
        List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrorList) {
            ParameterInvalidItem parameterInvalidItem = new ParameterInvalidItem();
            parameterInvalidItem.setFieldName(fieldError.getField());
            parameterInvalidItem.setMessage(fieldError.getDefaultMessage());
            parameterInvalidItem.setValue(fieldError.getRejectedValue());
            parameterInvalidItemList.add(parameterInvalidItem);
        }
        log.warn("自动捕获异常，参数校验失败! 请求URI: {}, 异常信息: {}", req.getRequestURI(), e.getMessage() + " (" + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber() + ")");
        return Result.failure(ResultCode.PARAM_CHECK_ERROR, parameterInvalidItemList);
    }

    /**
     * 400 - Bad Request 参数校验失败
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleServiceException(HttpServletRequest req, ConstraintViolationException e) {
        e.printStackTrace();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder msg = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            msg.append(constraintViolation.getMessage());
            msg.append("!");
        }
        log.warn("自动捕获异常，参数校验失败! 请求URI: {}, 异常信息: {}", req.getRequestURI(), e.getMessage() + " (" + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber() + ")");
        return Result.failure(ResultCode.PARAM_CHECK_ERROR, msg);
    }

    /**
     * 参数类型错误
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public Result handleFormatException(HttpServletRequest req, InvalidFormatException e) {
        e.printStackTrace();
        log.warn("自动捕获异常，参数类型错误! 请求URI: {}, 异常信息: {}", req.getRequestURI(), e.getMessage() + " (" + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber() + ")");
        return Result.failure(ResultCode.PARAM_TYPE_BIND_ERROR);
    }

    /**
     * 400 - Bad Request 参数校验失败
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public Result handleValidationException(HttpServletRequest req, ValidationException e) {
        e.printStackTrace();
        log.warn("自动捕获异常，参数校验失败! 请求URI: {}, 异常信息: {}", req.getRequestURI(), e.getMessage() + " (" + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber() + ")");
        return Result.failure(ResultCode.PARAM_CHECK_ERROR);
    }

    /**
     * 404 - 未找到接口
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result noHandlerFoundException(HttpServletRequest req, NoHandlerFoundException e) {
        e.printStackTrace();
        log.warn("自动捕获异常，未找到接口! 请求URI: {}, 异常信息: {}", req.getRequestURI(), e.getMessage() + " (" + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber() + ")");
        return Result.failure(ResultCode.INTERFACE_NOT_FOUNT);
    }

    /**
     * 405 - 接口请求类型错误
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupportedException(HttpServletRequest req, HttpRequestMethodNotSupportedException e) {
        e.printStackTrace();
        log.warn("自动捕获异常，接口请求类型错误! 请求URI: {}, 异常信息: {}", req.getRequestURI(), e.getMessage() + " (" + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber() + ")");
        return Result.failure(ResultCode.INTERFACE_TYPE_ERROR);
    }

    /**
     * 数据库外键关联
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result handleDataIntegrityViolationException(HttpServletRequest req, DataIntegrityViolationException e) {
        e.printStackTrace();
        log.warn("自动捕获异常，外键关联错误! 请求URI: {}, 异常信息: {}", req.getRequestURI(), e.getMessage() + " (" + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber() + ")");
        return Result.failure(ResultCode.FAILURE);
    }

    /**
     * 自定义异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(CommonException.class)
    public Result handleCommonException(HttpServletRequest req, CommonException e) {
        e.printStackTrace();
        log.warn("自动捕获异常，自定义异常! 请求URI: {}, 异常信息: {}", req.getRequestURI(), e.getMessage() + " (" + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber() + ")");
        return Result.failure(e.getResultCode(), e.getData());
    }

    /**
     * 文件过大异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result handleMaxUploadSizeException(HttpServletRequest req, MaxUploadSizeExceededException e) {
        e.printStackTrace();
        log.warn("自动捕获异常，文件过大异常! 请求URI: {}, 异常信息: {}", req.getRequestURI(), e.getMessage() + " (" + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber() + ")");
        return Result.failure(ResultCode.DATA_IS_MAX);
    }

    /**
     * 其他异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result handleException(HttpServletRequest req, Exception e) {
        e.printStackTrace();
        log.warn("自动捕获异常，自定义异常! 请求URI: {}, 异常信息: {}", req.getRequestURI(), e.getMessage() + " (" + e.getStackTrace()[0].getFileName() + ":" + e.getStackTrace()[0].getLineNumber() + ")");
        return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
    }

}
