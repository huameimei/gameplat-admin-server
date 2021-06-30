package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
public class TpInterfaceVO extends Model<TpInterfaceVO> {

  private Long id;

  private String name;

  private String code;

  private String parameters;
}
