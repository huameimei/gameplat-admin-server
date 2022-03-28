package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.sys.SysDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统字典数据Mapper
 *
 * @author three
 */
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

  /**
   * 根据type查询数据
   *
   * @param type 类型
   * @param status 是否启用
   * @return
   */
  List<SysDictData> findDataByType(@Param("type") String type, @Param("status") String status);
}
