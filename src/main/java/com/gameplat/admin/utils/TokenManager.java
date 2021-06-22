package com.gameplat.admin.utils;

import cn.hutool.core.util.RandomUtil;
import com.gameplat.admin.constant.Constants;
import com.gameplat.common.exception.BusinessException;
import com.gameplat.common.util.Md5Utils;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author Administrator
 *
 */
@Slf4j
public class TokenManager {

	/**
	 * 获取admin token
	 * @param request
	 * @return
	 */
	public static String getAdminRequestToken(HttpServletRequest request){
		return HttpUtils.getCookie(request, Constants.ADMIN_TOKEN_NAME);
	}
	
	/**
	 * 跟据token获取用户ID
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static Long getUidByToken(String token) throws Exception {
		if (StringUtil.isBlank(token)) {
			return null;
		}
		String s = Xxtea.decryptToXxteaByBase64(token, Constants.LOGIN_TOKEN_KEY);
		String[] array = s.split("\\|");
		if (array.length != 4 && array.length != 5) {
			log.info("token 格式不正确：" + token);
			throw new BusinessException("UC/TOKEN_INVALID", "uc.token_invalid", null);
		}
		return new Long(array[1]);
	}

	/**
	 * 生产用户登录token
	 * @param uid
	 * @param platCode
	 * @param loginTime
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static String createToken(Long uid, String platCode, Date loginTime, String ip) throws Exception {
		String s = loginTime.getTime() + "|" + uid + "|" + ip + "|" + platCode;
		return Xxtea.encryptToXxteaByBase64(s, Constants.LOGIN_TOKEN_KEY);
	}

	public static String createFVToken(Long uid, String platCode, Date loginTime, String ip) throws Exception {
		String s = loginTime.getTime() + "|" + uid + "|" + ip + "|" + platCode + "|" + RandomUtil.randomString(5) + RandomUtil.randomNumbers(5);
		return Xxtea.encryptToXxteaByBase64(s, Constants.LOGIN_TOKEN_KEY);
	}

	public static void checkFVToken(String fvToken) throws BusinessException {
		try {
			Xxtea.decryptToXxteaByBase64(fvToken, Constants.LOGIN_TOKEN_KEY);
		} catch (Exception e) {
			throw new BusinessException("非法fv");
		}
	}

	public static String createTokenWithUserAgent(Long uid, String platCode, Date loginTime, String ip, String userAgent) throws Exception {
		if (userAgent == null) {
			userAgent = "";
		}
		String s = loginTime.getTime() + "|" + uid + "|" + ip + "|" + platCode + "|" + Md5Utils.MD5(userAgent);
		return Xxtea.encryptToXxteaByBase64(s, Constants.LOGIN_TOKEN_KEY);
	}

	public static boolean validateUserAgent(String token, String userAgent) throws Exception {
		String[] rawToken = Xxtea.decryptToXxteaByBase64(token, Constants.LOGIN_TOKEN_KEY).split("\\|");
		if (rawToken.length != 5) {
			throw new BusinessException("UC/TOKEN_INVALID", "uc.token_invalid", null);
		}
		if (userAgent == null) {
			userAgent = "";
		}
		return StringUtils.equals(rawToken[4], Md5Utils.MD5(userAgent));
	}
}
