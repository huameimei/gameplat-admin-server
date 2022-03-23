package com.gameplat.admin.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author aBen
 * @date 2021/8/5 10:10
 * @desc
 */
@Data
public class GameReportExportVO {

  /** 游戏大类ID */
  private Integer gameTypeId;

  /** 游戏大类名字 */
  private String gameTypeName;

  /** 游戏报表数据 */
  private List<GameFinancialReportVO> gameFinancialReportList;
}
