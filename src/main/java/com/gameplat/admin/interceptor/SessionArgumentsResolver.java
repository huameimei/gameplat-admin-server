package com.gameplat.admin.interceptor;

import com.gameplat.admin.constant.Constants;
import com.gameplat.admin.model.bean.TokenInfo;
import com.gameplat.admin.model.entity.SysUser;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.admin.utils.HttpUtils;
import com.gameplat.admin.utils.TokenManager;
import com.gameplat.common.exception.BusinessException;
import java.lang.reflect.Field;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jodd.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SessionArgumentsResolver implements HandlerMethodArgumentResolver {

	private static Logger logger = LoggerFactory.getLogger(SessionArgumentsResolver.class);

	@Resource
	private SysUserService sysUserService;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(Session.class) != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		Class<?> paramType = parameter.getParameterType();
		Session sessionAnnotation = parameter.getParameterAnnotation(Session.class);
		if (sessionAnnotation != null) {
			Long adminId = getAdminId(request, response);
			if(adminId == null){
				return null;
			}
      SysUser admin = sysUserService.getById(adminId);
			if(admin == null){
				sysUserService.logout(adminId);
				throw new BusinessException("UC/TOKEN_INVALID", "uc.token_invalid", null);
			}
			if (SysUser.class == paramType) {
				return admin;
			} else {
				String parameterName = sessionAnnotation.value();
				if (StringUtils.isEmpty(parameterName)) {
					parameterName = parameter.getParameterName();
				}
				if (parameterName.equals("adminId")) {
					return admin.getId();
				}
				Field field = FieldUtils.getDeclaredField(SysUser.class, parameterName, true);
				if (field != null) {
					return field.get(admin);
				} else {
					logger.error("错误的参数名称：" + parameterName + "，请查看" + SysUser.class);
				}
			}
		}
		return null;
	}

	private Long getAdminId(HttpServletRequest request, HttpServletResponse response) {
		String token = TokenManager.getAdminRequestToken(request);
		if (StringUtil.isBlank(token)) {
			return null;
		}
		try {
			Long uid = TokenManager.getUidByToken(token);
			if (uid == null) {
				return null;
			}
			TokenInfo tokenInfo = sysUserService.getTokenInfo(uid);
			if (tokenInfo == null) {
				return null;
			}
			if (!tokenInfo.getToken().equals(token)) {
				// token失效
				return null;
			}
			return tokenInfo.getUid();
		} catch (Exception e) {
			HttpUtils.removeCookie(request, response, Constants.TOKEN_NAME);
			logger.error("SessionArgumentsResolver, adminUid error", e);
		}
		return null;
	}

}
