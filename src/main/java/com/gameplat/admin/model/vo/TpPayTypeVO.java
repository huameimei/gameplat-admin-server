package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
public class TpPayTypeVO extends Model<TpInterfaceVO> {

  private String payType;

  private String interfaceCode;

  private String name;

  private String code;
}
