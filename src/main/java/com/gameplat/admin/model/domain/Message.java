package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 站内信
 *
 * @author: kenvin
 * @date: 2021/4/28 15:53
 * @desc:
 */

@Data
@TableName("message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "消息标题")
    private String title;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "总类别:0.默认(3) 1.游戏(2) 2.足球(2) 3.直播(2) 4.系统(1)")
    private Integer category;

    @ApiModelProperty(value = "位置: 0.默认(1) 1.推荐(2) 2.首页大厅(2,3) 3.彩票首页(3) 4.体育首页(3) 5.游戏首页(3)")
    private Integer position;

    @ApiModelProperty(value = "展示类型: 0.默认(1) 1.滚动(2) 2.文本弹窗(3) 3.图片弹窗(3)")
    private Integer showType;

    @ApiModelProperty(value = "PC端图片")
    private String pcImage;

    @ApiModelProperty(value = "APP端图片")
    private String appImage;

    @ApiModelProperty(value = "弹出次数:0.默认(1,2) 1.只弹一次(3) 2.多次弹出(3)")
    private Integer popsCount;

    @ApiModelProperty(value = "推送范围:1.全部会员 2.部分会员 3.在线会员 4.充值层级 5.VIP等级 6.代理线")
    private Integer pushRange;

    @ApiModelProperty(value = "关联账号")
    private String linkAccount;

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "升序排序")
    private Integer sort;

    @ApiModelProperty(value = "消息类型（1.系统消息、2.平台公告、3.个人弹窗消息）")
    private Integer type;

    @ApiModelProperty(value = "语种")
    private String language;

    @ApiModelProperty(value = "状态：0 禁用 1 启用")
    private Integer status;

    @ApiModelProperty(value = "是否即时消息: 0 否  1 是")
    private Integer immediateFlag;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
