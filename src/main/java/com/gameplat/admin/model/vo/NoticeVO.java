package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 消息公告出参
 * @date 2021/11/16
 */

@Data
public class NoticeVO implements Serializable {

    private Integer id;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 内容
     */
    private String noticeContent;

    /**
     * 类型 (1滚动公告、2登录公告、3推广公告、4注册公告、5彩票公告、6体育公告)
     */
    private Integer noticeType;

    /**
     * 公告的开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
    private Date beginDate;

    /**
     * 公告的结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
    private Date endDate;

    /**
     * 操作人的账号
     */
    private String operator;

    /**
     * 启用状态 (0.启用、1.禁用)
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
    private Date updateTime;

}
