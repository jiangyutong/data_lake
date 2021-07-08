package com.boyoi.core.entity;

import com.boyoi.core.util.OptDateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述：
 * 实体类父类
 *
 * @author ZhouJL
 * @Date 2018/11/27 18:08
 */
@ToString
@EqualsAndHashCode
public class BaseEntity implements Serializable {


    private static final long serialVersionUID = 1L;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Getter
    private Date optTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Getter
    @Setter
    private Date createTime;

    @Getter
    @Setter
    private String optTimeStr;

    @Getter
    @Setter
    private String optPerson;

    public void setOptTime(Date optTime) {
        if (optTime != null) {
            this.optTime = optTime;
            this.optTimeStr = OptDateUtil.dateToStr(optTime);
        }
    }
}
