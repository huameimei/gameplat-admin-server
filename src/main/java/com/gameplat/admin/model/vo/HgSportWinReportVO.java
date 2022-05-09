package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.util.I18nSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Locale;

/**
 * @author aBen
 * @date 2022/2/6 19:12
 * @desc
 */
@Data
public class HgSportWinReportVO implements Serializable {

  @ApiModelProperty(value = "投注类型名字")
  @Excel(name = "投注类型", width = 20, isImportField = "true_st")
  private String betTypeName;

  @ApiModelProperty(value = "投注类型")
  private String betType;

  @ApiModelProperty(value = "投注人数")
  @Excel(name = "投注人数", width = 15, isImportField = "true_st")
  private Long userCount = 0L;

  @ApiModelProperty(value = "投注次数")
  @Excel(name = "投注次数", width = 15, isImportField = "true_st")
  private Long betCount = 0L;

  @ApiModelProperty(value = "投注金额")
  @Excel(name = "投注金额", width = 15, isImportField = "true_st")
  private BigDecimal betAmount = BigDecimal.ZERO;

  @ApiModelProperty(value = "有效投注金额")
  @Excel(name = "有效投注金额", width = 15, isImportField = "true_st")
  private BigDecimal validAmount = BigDecimal.ZERO;

  @ApiModelProperty(value = "派奖金额")
  @Excel(name = "派奖金额", width = 15, isImportField = "true_st")
  private BigDecimal sendPrizeAmount = BigDecimal.ZERO;

  @ApiModelProperty(value = "人均投注金额")
  @Excel(name = "人均投注金额", width = 15, isImportField = "true_st")
  private BigDecimal averageBetAmount = BigDecimal.ZERO;

  @ApiModelProperty(value = "游戏输赢金额")
  @Excel(name = "游戏输赢金额", width = 15, isImportField = "true_st")
  private BigDecimal gameWinAmount = BigDecimal.ZERO;

  @ApiModelProperty(value = "会员账号")
  private String memberAccount;

  @ApiModelProperty(value = "代理账号")
  private String proxyAccount;

  @ApiModelProperty(value = "是否包含直属下级")
  private Integer isDirectly;

  @ApiModelProperty(value = "开始时间")
  private Long beginTime;

  @ApiModelProperty(value = "结束时间")
  private Long endTime;

  public void setBetTypeName(String jsonStr) {
    if (StringUtils.isNotEmpty(jsonStr)) {
      Locale locale = LocaleContextHolder.getLocale();
      JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
      String str = jsonObject.getStr(locale.toLanguageTag());
      // 如果浏览器语言不在5种语言内，则默认返回中文
      if (StringUtils.isEmpty(str)) {
        str = jsonObject.getStr("zh-CN");
      }
      this.betTypeName = str;
    }
  }

}
