package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.gameplat.admin.model.entity.TpInterface;
import java.util.List;
import lombok.Data;

@Data
public class TpMerchantPayTypeVO extends Model<TpMerchantPayTypeVO> {

  private Long id;

  private String name;

  private String parameters;

  private String payTypes;

  private String tpInterfaceCode;

  private List<TpPayTypeVO> tpPayTypeVOList;

  private TpInterfaceVO tpInterfaceVO;
}
