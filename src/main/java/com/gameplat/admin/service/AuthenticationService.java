package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.AdminLoginDTO;
import com.gameplat.admin.model.dto.GoogleAuthDTO;
import com.gameplat.admin.model.vo.GoogleAuthCodeVO;
import com.gameplat.admin.model.vo.UserToken;
import com.gameplat.common.model.bean.RefreshToken;
import com.gameplat.security.context.UserCredential;
import org.apache.kafka.common.security.auth.Login;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {

  UserToken login(AdminLoginDTO dto, HttpServletRequest request);

  RefreshToken refreshToken(String refreshToken);

  void logout();

  GoogleAuthCodeVO create2FA(String username);

  RefreshToken verify2FA(UserCredential credential, String code);

  RefreshToken bindSecret(UserCredential credential, GoogleAuthDTO authDTO);
}
