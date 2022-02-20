package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.AdminLoginDTO;
import com.gameplat.admin.model.vo.UserToken;
import com.gameplat.common.model.bean.RefreshToken;
import com.gameplat.security.context.UserCredential;

import javax.servlet.http.HttpServletRequest;
import java.sql.Ref;

public interface AuthenticationService {

  UserToken login(AdminLoginDTO dto, HttpServletRequest request);

  RefreshToken refreshToken(String refreshToken);

  void logout();

  RefreshToken verify2Fa(UserCredential credential, String code);
}
