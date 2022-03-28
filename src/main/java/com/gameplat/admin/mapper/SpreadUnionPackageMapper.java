package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.dto.SpreadUnionPackageDTO;
import com.gameplat.admin.model.vo.SpreadUnionPackageVO;
import com.gameplat.model.entity.spread.SpreadUnionPackage;

import java.util.List;

public interface SpreadUnionPackageMapper extends BaseMapper<SpreadUnionPackage> {

  /**
   * 联盟包设置列表
   *
   * @param dto SpreadUnionPackageDTO
   * @return List
   */
  List<SpreadUnionPackageVO> getUnionPackage(SpreadUnionPackageDTO dto);
}
