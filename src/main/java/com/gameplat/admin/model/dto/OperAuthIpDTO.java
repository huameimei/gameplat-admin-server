package com.gameplat.admin.model.dto;

import java.util.Date;
import lombok.Data;

/**
 * ip白名单操作类
 * @author three
 */
@Data
public class OperAuthIpDTO {

  /**
   * 序号
   */
  private Long id;
  /**
   * ip类型
   */
  private String ipType;
  /**
   * ip
   */
  private String ip;

  /** 创建者 */
  private String createBy;
  /** 创建时间 */
  private Date createTime;
  /**
   * 更新人
   */
  private String updateBy;
  /**
   * 更新时间
   */
  private Date updateTime;
  /**
   * 备注
   */
  private String remark;
}
