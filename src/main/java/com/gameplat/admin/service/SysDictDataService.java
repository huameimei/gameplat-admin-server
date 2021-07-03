package com.gameplat.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysDictDataAddDto;
import com.gameplat.admin.model.dto.SysDictDataEditDto;
import com.gameplat.admin.model.dto.SysDictDataQueryDto;
import com.gameplat.admin.model.entity.SysDictData;
import com.gameplat.admin.model.vo.SysDictDataVo;
import java.util.List;

public interface SysDictDataService extends IService<SysDictData> {

  List<SysDictData> selectDictDataListByType(String dictType);

  <T> T getDictData(String dictType, Class<T> t);

  SysDictData getSysDictData(String dictType, String dictLabel);

  IPage<SysDictDataVo> queryPage(IPage<SysDictData> page, SysDictDataQueryDto queryDto);

  void save(SysDictDataAddDto sysDictDataAddDto);

  void delete(Long id);

  void update(SysDictDataEditDto sysDictDataEditDto);

  /**
   * 找出数据并存到JSON文件中
   * @param dictType
   */
  void findDataInitFile(String dictType);

  String getDefaultJson();

}
