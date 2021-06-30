package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
public class PayTypeVO extends Model<PayTypeVO> {

  private Long id;

  private String name;

  private String code;
}
