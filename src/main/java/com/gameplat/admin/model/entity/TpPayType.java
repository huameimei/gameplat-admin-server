package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gameplat.common.model.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tp_pay_type")
public class TpPayType extends BaseEntity {

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
}
