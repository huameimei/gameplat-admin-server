package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.domain.proxy.RecommendConfig;
import com.gameplat.admin.model.dto.RecommendConfigDto;
import com.gameplat.admin.model.vo.GameDivideVo;

import java.util.List;
import java.util.Map;

public interface RecommendConfigService extends IService<RecommendConfig> {
    /**
     * 获取代理配置
     * @return
     */
    RecommendConfig getRecommendConfig();

    /**
     * 获取层层代分红配置
     * @param lang
     * @return
     */
    Map<String, List<GameDivideVo>> getDefaultLayerDivideConfig(String lang);

    /**
     * 初始化层层代或固定比例分红模式配置预设
     * @param lang
     * @return
     */
    String initDivideConfig(String lang);

    /**
     * 初始化裂变配置
     * @param dictDataList
     * @param lang
     * @return
     */
    String initFissionDivideConfig(String lang);

    /**
     * 获取固定模式配置
     * @param lang
     * @return
     */
    Map<String, List<GameDivideVo>> getDefaultFixDivideConfig(String lang);

    /**
     * 获取列表模式配置
     * @param lang
     * @return
     */
    Map<String, Object> getDefaultFissionDivideConfig(String lang);

    /**
     * 编辑代理配置
     * @param recommendConfigDto
     */
    void edit(RecommendConfigDto recommendConfigDto);
}
