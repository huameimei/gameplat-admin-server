package com.gameplat.admin.model.vo;

import com.gameplat.admin.model.bean.Language;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ConfigVO implements Serializable {

  private AdminLoginLimit loginConfig;

  private List<Language> languages;
}
