package com.gameplat.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.model.dto.GoogleAuthDTO;
import com.gameplat.admin.model.vo.ConfigVO;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;

import java.util.List;
import java.util.Map;

public interface CommonService {

  AdminLoginLimit getLoginLimit();

  Map<Object, List<JSONObject>> getDictByTypes(String types);

  ConfigVO getConfig();

  /**
   * 用户绑定谷歌密钥
   *
   * @param authDTO GoogleAuthDTO
   */
  void bindSecret(GoogleAuthDTO authDTO);
}
