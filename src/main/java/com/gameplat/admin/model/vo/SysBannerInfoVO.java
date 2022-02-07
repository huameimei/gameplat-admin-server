package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * banner信息VO
 *
 * @author admin
 */
@Data
public class SysBannerInfoVO implements Serializable {


    /**
     * 主键
     */
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     *  banner大类，1 体育banner配置，2 彩票banner配置
     */
    @ApiModelProperty("banner大类，1 体育banner配置，2 彩票banner配置")
    private Integer type;

    /**
     * banner类型
     */
    @ApiModelProperty("banner类型")
    private Integer bannerType;

    /**
     * banner子类型
     */
    @ApiModelProperty("banner子类型")
    private Integer childType;

    /**
     * banner子类型名称
     */
    @ApiModelProperty("banner子类型名称")
    private String childName;

    /**
     * pc端图片地址
     */
    @ApiModelProperty("pc端图片地址")
    private String pcPicUrl;

    /**
     * app端图片地址
     */
    @ApiModelProperty("app端图片地址")
    private String appPicUrl;

    /**
     * 状态
     */
    @ApiModelProperty("状态（0禁用 1启用）")
    private Integer status;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateBy;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 语种
     */
    @ApiModelProperty("语种")
    private String language;

    @ApiModelProperty("跳转地址")
    private String jumpUrl;

    @ApiModelProperty("游戏类别")
    private String gameKind;

    @ApiModelProperty("关联游戏")
    private String gameCode;

}
