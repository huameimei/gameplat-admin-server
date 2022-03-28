package com.gameplat.admin.model.bean;

import lombok.Data;

/**
 * 系统语言
 *
 * @author three
 */
@Data
public class Language {

  private String languageCode;
  private String languageName;
  private String currencyCode;
  private String currencyName;
  private String currencyMark;
}
