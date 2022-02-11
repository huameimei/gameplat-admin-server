package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.gameplat.admin.model.domain.TpPayType;
import java.util.List;
import lombok.Data;

@Data
public class TpInterfacePayTypeVo extends Model<TpInterfacePayTypeVo> {

  public Long id;

  private String name;

  private String code;

  private Integer parameterType;

  private Integer status;

  private String parameters;

  private String orderQueryVersion;

  private String orderQueryMethod;

  private String orderQueryUrl;

  private Integer orderQueryEnabled;

  private String ipFlg;

  private String ipWhiteList;

  private List<TpPayType> tpPayTypeList;

}
