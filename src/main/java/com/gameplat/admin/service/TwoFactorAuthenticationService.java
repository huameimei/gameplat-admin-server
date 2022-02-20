package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.GoogleAuthDTO;
import com.gameplat.admin.model.vo.GoogleAuthCodeVO;

public interface TwoFactorAuthenticationService {

  boolean isEnabled();

  GoogleAuthCodeVO create2Fa(String username);

  void bindSecret(GoogleAuthDTO dto);

  void verify2Fa(Long userId, String code);

  void isEnabled2Fa();
}
