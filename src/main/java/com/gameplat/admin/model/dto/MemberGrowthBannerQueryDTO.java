package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/1/15
 */
@Data
public class MemberGrowthBannerQueryDTO implements Serializable {

  @Schema(description = "终端: 0 WEB  1 H5  2 ANDRIOD  3 IOS")
  private Integer cilentType;

  @Schema(description = "匹配界面: 0 H5 ANDRIOD IOS 对应福利中心轮播   2 对应VIP详情轮播    PC 1对应轮播")
  private Integer areaType;
}
