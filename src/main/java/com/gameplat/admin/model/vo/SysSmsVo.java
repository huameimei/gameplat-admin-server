package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class SysSmsVo implements Serializable {

  private Long id;

  /** 用户手机号码 */
  private String phone;

  /** 验证码 */
  private String validCode;

  /** 发送内容 */
  private String content;

  /** 发送状态 （0：发送成功 1：发送失败 3已使用） */
  private Integer status;

  /** 创建时间 */
  private Date createTime;

  /** 短信类型 (1 登录短信 2 绑定手机号 3账户安全开启) */
  private Integer smsType;

  /** 请求ip */
  private String requestIp;
}
