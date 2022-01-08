package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.AdminLoginDTO;
import com.gameplat.admin.model.vo.UserToken;
import com.gameplat.common.model.bean.RefreshToken;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {

  UserToken login(AdminLoginDTO dto, HttpServletRequest request);

  RefreshToken refreshToken(String refreshToken);

  void logout();
}
