package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.proxy.DivideFissionConfig;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.model.vo.GameDivideVo;

import java.util.Map;

/**
 * @Description : 裂变分红模式配置
 * @Author : cc
 * @Date : 2022/2/22
 */
public interface DivideFissionConfigService extends IService<DivideFissionConfig> {
    void add(String userName, String s);

    Map<String, Object> getFissionConfigForEdit(String userName, String s);

    void edit(DivideConfigDTO divideConfigDTO, String lang);

    void remove(String ids);

    GameDivideVo getConfigByFirstCode(String superName, String code);

    DivideFissionConfig getByAccount(String account);
}
