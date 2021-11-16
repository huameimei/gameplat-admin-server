package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description 公告信息表
 * @date 2021/11/16
 */
@Data
@TableName("notice")
public class Notice implements Serializable {


    @TableId(type = IdType.AUTO)
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
    private Long beginDate;

    /**
     * 公告的结束日期
     */
    private Long endDate;

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
    private Long updateTime;

    /**
     * 添加时间
     */
    private Long createTime;

}
