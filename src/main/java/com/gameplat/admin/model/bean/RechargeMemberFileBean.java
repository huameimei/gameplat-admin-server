package com.gameplat.admin.model.bean;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author kb
 * @Date 2022/3/19 19:26
 * @Version 1.0
 */
@Data
public class RechargeMemberFileBean implements Serializable {


  /**
   * 会员账号
   */
  @ExcelProperty(index = 0, value = "会员账号")
  private String username;

}
