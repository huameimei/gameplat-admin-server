package com.gameplat.admin.util;


import com.gameplat.admin.model.bean.SportGameConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: summer
 * @description: 配置类基类
 * @create: 2020/8/11 13:46
 **/
@Component
@Slf4j
@Data
public class SportRequestUtil implements Serializable {


    /**
     * @return 获取平台配置信息
     */


    public Map<String, Object> basePrams(Object param, SportGameConfig config, int pageType){
        Map<String, Object> paramMap = JSONUtil.transBean2Map(param);
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        paramMap.put("appId", config.getAppId());
        paramMap.put("nonceStr", RandomStringUtils.randomAlphanumeric(32));
        if (pageType == 1) {
            //pageType  1 分页  2不分页
            Integer pageNum = Integer.valueOf(ObjectUtils.isNotEmpty(paramMap.get("pageNum")) ? paramMap.get("pageNum").toString() : "1" );
            Integer pageSize = Integer.valueOf(ObjectUtils.isNotEmpty(paramMap.get("pageSize")) ? paramMap.get("pageSize").toString() : "10" );
            paramMap.put("pageNum", (pageNum - 1 ) * pageSize );
            paramMap.put("pageSize", pageSize);
        }
        paramMap.put("sign", SportSignUtil.getSportsign(paramMap, config.getAppKey()));
        return paramMap;
    }
}
