package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
public class TpMerchantVO extends Model<TpMerchantVO> {

  private Long id;

  private String name;

  private String tpInterfaceCode;

  private Integer status;

  private Long rechargeTimes;

  private Long rechargeAmount;

  private String interfaceName;
}
