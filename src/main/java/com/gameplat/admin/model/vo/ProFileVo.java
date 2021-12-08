package com.gameplat.admin.model.vo;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 用戶信息Vo
 * @author three
 */
@Data
public class ProFileVo {

  /**
   * 用户名称
   */
  private String userName;
  /**
   * 昵称
   */
  private String nickName;
  /**
   * 是否修改密码
   */
  private Integer isChange;
  /**
   * 电话
   */
  private String phone;
  /**
   * 权限列表
   */
  private List<String> permis;
  /**
   * 用户个性设置
   */
  private String setting;
  /**
   * 创建时间
   */
  private Date createTime;

}
