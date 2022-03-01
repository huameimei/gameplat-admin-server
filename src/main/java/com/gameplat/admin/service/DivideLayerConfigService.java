package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.proxy.DivideLayerConfig;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.model.vo.DivideLayerConfigVo;
import com.gameplat.admin.model.vo.GameDivideVo;

import java.util.Map;

/**
 * @Description : 层层代分红模式配置
 * @Author : cc
 * @Date : 2022/2/22
 */
public interface DivideLayerConfigService extends IService<DivideLayerConfig> {

    IPage<DivideLayerConfigVo> page(PageDTO<DivideLayerConfig> page, DivideConfigDTO dto);

    void add(String userName, String lang);

    void edit(DivideConfigDTO divideConfigDTO, String s);

    Map<String, Object> getLayerConfigForEdit(String userName, String lang);

    void remove(String ids);

    Map<String, Object> getLayerConfigForLinkAdd(String userName, String lang);

    Map<String, Object> getLayerConfigForLinkEdit(Long id, String lang);

    String getRealSuperName(String superPath, Integer userLevel);

    GameDivideVo getConfigByGameCode(String userName, String code);
}
