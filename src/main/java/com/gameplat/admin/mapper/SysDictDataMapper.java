package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.SysDictData;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 系统字典数据Mapper
 *
 * @author three
 */
public interface SysDictDataMapper extends BaseMapper<SysDictData> {
    /**
     * 根据type查询数据
     * @param type 类型
     * @param status 是否启用
     * @return
     */
    public List<SysDictData> findDataByType(@Param("type")String type, @Param("status")String status);
}
