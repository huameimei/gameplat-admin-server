package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.proxy.DivideFixConfig;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.model.vo.GameDivideVo;

import java.util.Map;

/**
 * @Description : 固定比例分红模式配置
 * @Author : cc
 * @Date : 2022/2/22
 */
public interface DivideFixConfigService extends IService<DivideFixConfig> {
    void add(String userName, String lang);

    Map<String, Object> getFixConfigForEdit(String userName, String s);

    void edit(DivideConfigDTO divideConfigDTO, String lang);

    void remove(String ids);

    GameDivideVo getConfigByGameCode(String superName, String code);

}
