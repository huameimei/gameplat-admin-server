package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
public class PpInterfaceVO extends Model<PpInterfaceVO> {

  private Long id;

  private String name;

  private String code;

  private String parameters;

  private Integer status;

  private String limitInfo;
}