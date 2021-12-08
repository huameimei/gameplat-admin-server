package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author lily
 * @description 修改公告消息入参
 * @date 2021/11/16
 */

@Data
public class NoticeEditDTO implements Serializable {

    private Integer id;

    /**
     * 类型 (1滚动公告、2登录公告、3推广公告、4注册公告、5彩票公告、6体育公告)
     */
    private Integer noticeType;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 内容
     */
    private String noticeContent;

    /**
     * 公告的开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginDate;

    /**
     * 公告的结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 操作人的账号
     */
    private String operator;

}
