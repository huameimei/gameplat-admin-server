package com.gameplat.admin.model.vo;

import lombok.Data;
import org.dozer.Mapping;

import java.util.Date;

/**
 * 白名单Vo
 *
 * @author three
 */
@Data
public class AuthIpVo {

  /** 序号 */
  private Long id;
  /** ip */
  @Mapping(value = "allowIp")
  private String ip;
  /** ip类型 */
  private String ipType;
  /** 创建时间 */
  private Date createTime;
  /** 修改时间 */
  private Date updateTime;
  /** 备注 */
  private String remark;
}
