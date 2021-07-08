package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysDictDataAddDTO;
import com.gameplat.admin.model.dto.SysDictDataEditDTO;
import com.gameplat.admin.model.dto.SysDictDataQueryDTO;
import com.gameplat.admin.model.entity.SysDictData;
import com.gameplat.admin.model.vo.SysDictDataVO;
import java.util.List;

public interface SysDictDataService extends IService<SysDictData> {

  List<SysDictData> selectDictDataListByType(String dictType);

  <T> T getDictData(String dictType, Class<T> t);

  SysDictData getSysDictData(String dictType, String dictLabel);

  IPage<SysDictDataVO> queryPage(IPage<SysDictData> page, SysDictDataQueryDTO queryDto);

  void save(SysDictDataAddDTO sysDictDataAddDto);

  void delete(Long id);

  void update(SysDictDataEditDTO sysDictDataEditDto);

  String getDefaultJson();
}
