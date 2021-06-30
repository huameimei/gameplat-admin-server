package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pp_interface")
public class PpInterface extends BaseEntity<PpInterface> {

  private String name;

  private String code;

  private String version;

  private String charset;

  private Integer dispatchType;

  private String dispatchUrl;

  private String dispatchMethod;

  private Integer status;

  private String parameters;

  private Integer orderQueryEnabled;

  private String orderQueryVersion;

  private String orderQueryUrl;

  private String orderQueryMethod;

  private String limtInfo;

  private Integer asynNotifyStatus;
}
