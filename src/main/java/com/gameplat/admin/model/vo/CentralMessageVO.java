package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.common.util.Date2LongSerializerUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 中心通知消息出参
 * @date 2021/11/18
 */
@Data
public class CentralMessageVO implements Serializable {

  /** 编号 */
  private Integer id;

  /** 公告标题 */
  private String title;

  /** 内容 */
  private String content;

  /** 接收人 */
  private String receiverAccount;

  /** 生效时间 */
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date effectTime;

  /** 失效时间 */
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date expireTime;

  /** 更新时间 */
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;
}
