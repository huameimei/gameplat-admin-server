package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import lombok.Data;

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
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date beginDate;

    /**
     * 公告的结束日期
     */
    @JsonSerialize(using = Date2LongSerializerUtils.class)
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
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date updateTime;

}
