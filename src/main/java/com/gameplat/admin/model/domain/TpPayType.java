package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tp_pay_type")
public class TpPayType {

  @TableId(type = IdType.AUTO)
  public Long id;

  @ApiModelProperty(value = "支付类型")
  private String payType;

  @ApiModelProperty(value = "第三方入款接口编码")
  private String interfaceCode;

  @ApiModelProperty(value = "版本号")
  private String version;

  @ApiModelProperty(value = "字符集")
  private String charset;

  @ApiModelProperty(value = "0 - 表单提交, 1 - HttpClient获取")
  private Integer dispatchType;

  @ApiModelProperty(value = "调度地址")
  private String dispatchUrl;

  @ApiModelProperty(value = "调度方法POST/GET")
  private String dispatchMethod;

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "第三方通道编码")
  private String code;

  @ApiModelProperty(value = "视图类型0-跳转/1-二维码/2-原生调用")
  private Integer viewType;

  @ApiModelProperty(value = "银行代码JSON")
  private String banks;

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
}
