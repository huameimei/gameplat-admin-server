package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.proxy.DivideFixConfig;
import com.gameplat.admin.model.domain.proxy.DivideLayerConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Description : 固定比例分红模式配置
 * @Author : cc
 * @Date : 2022/2/22
 */
public interface DivideFixConfigMapper extends BaseMapper<DivideFixConfig> {
    @Select("select * from divide_fix_config where user_name = #{userName}")
    DivideFixConfig getByUserName(@Param("userName") String userName);
}
