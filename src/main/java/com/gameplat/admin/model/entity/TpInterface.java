package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gameplat.common.model.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tp_interface")
public class TpInterface extends BaseEntity<TpInterface> {

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "编码")
  private String code;

  @ApiModelProperty(value = "版本号")
  private String version;

  @ApiModelProperty(value = "字符集")
  private String charset;

  @ApiModelProperty(value = "调度类型: [0 - 表单提交, 1 - HttpClient获取]")
  private Integer dispatchType;

  @ApiModelProperty(value = "调度方法: [GET, POST]")
  private String dispatchMethod;

  @ApiModelProperty(value = "调度地址")
  private String dispatchUrl;

  @ApiModelProperty(value = "状态: [0 - 启用, 1 - 禁用]")
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
}
