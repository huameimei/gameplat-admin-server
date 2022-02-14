package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
@TableName("tp_interface")
public class TpInterface {

  @TableId(type = IdType.AUTO)
  public Long id;

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "编码")
  private String code;

  @ApiModelProperty(value = "参数类型 1-Parameter,2-Payload")
  private Integer parameterType;

  @ApiModelProperty(value = "状态: [1 - 启用, 0 - 禁用]")
  private Integer status;

  @ApiModelProperty(value = "接口参数JSON")
  private String parameters;

  @ApiModelProperty(value = "支付查询接口版本号")
  private String orderQueryVersion;

  @ApiModelProperty(value = "订单查询方法: [GET, POST]")
  private String orderQueryMethod;

  @ApiModelProperty(value = "支付查询接口调度地址")
  private String orderQueryUrl;

  @ApiModelProperty(value = "是否支持订单查询: [0 - 否, 1 - 是]")
  private Integer orderQueryEnabled;

  @ApiModelProperty(value = "是否需要设置ip白名单: [0 - 否, 1 - 是]")
  private String ipFlg;

  @ApiModelProperty(value = "支付回调ip白名单集合")
  private String ipWhiteList;

  @ApiModelProperty(value = "是否系统充值接口 [0 - 否, 1 - 是]")
  private Integer isSystemInterface;

  /**
   * 创建者
   */
  @TableField(fill = FieldFill.INSERT)
  @ApiModelProperty(value = "创建者")
  private String createBy;

  /**
   * 创建时间
   */
  @TableField(fill = FieldFill.INSERT)
  @ApiModelProperty(value = "创建时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /**
   * 更新者
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @ApiModelProperty(value = "更新者")
  private String updateBy;

  /**
   * 更新时间
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @ApiModelProperty(value = "更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;

  @TableField(exist = false)
  private List<TpPayType> tpPayTypeList;
}
