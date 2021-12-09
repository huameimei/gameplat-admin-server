package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class TpMerchantVO extends Model<TpMerchantVO> {

  private Long id;

  private String name;

  private String tpInterfaceCode;

  private Integer status;

  private Long rechargeTimes;

  private BigDecimal rechargeAmount;

  private String interfaceName;

  private String payTypes;

  private Date createTime;

  private String createBy;

  private String updateBy;

  private Date updateTime;


}
