package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.bean.AdminRedisBean;
import com.gameplat.admin.model.bean.TokenInfo;
import com.gameplat.admin.model.entity.SysUser;
import com.gameplat.admin.model.vo.UserEquipmentVO;

/**
 * @author Lenovo
 */
public interface SysUserService extends IService<SysUser> {


    SysUser getUserByAccount(String account);

    TokenInfo login(String account, String password, String requestIp, UserEquipmentVO equipment, String userAgentString)
            throws Exception;

    void logout(Long adminId);

    TokenInfo getTokenInfo(Long uid);

    AdminRedisBean getPrivilege(Long adminId);
}
