package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.bean.UserWithdrawLimitInfo;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.dto.OperDictDataDTO;
import com.gameplat.admin.model.dto.SysDictDataDTO;
import com.gameplat.admin.model.vo.DictDataVo;
import com.gameplat.admin.model.vo.MemberWithdrawDictDataVo;

import java.util.List;

/**
 * 字典数据
 *
 * @author three
 */
public interface SysDictDataService extends IService<SysDictData> {

  IPage<DictDataVo> selectDictDataList(PageDTO<SysDictData> page, SysDictDataDTO dictData);

  List<SysDictData> getDictList(SysDictData dictData);

  List<SysDictData> getDictDataByType(String dictType);

  List<SysDictData> getDictDataByTypes(List<String> dictTypes);

  SysDictData getDictData(String dictType, String dictLabel);

  String getDictDataValue(String dictType, String dictLabel);

  void insertDictData(OperDictDataDTO operDictDataDTO);

  void updateDictData(OperDictDataDTO operDictDataDTO);

  void deleteDictDataByIds(List<Long> ids);

  void updateStatus(Long id, Integer status);

  IPage<MemberWithdrawDictDataVo> queryWithdrawPage(Page<SysDictData> page);

  void addOrUpdateUserWithdrawLimit(
      String dictType, String dictLabel, UserWithdrawLimitInfo limitInfo);

  void delete(String dictType, String dictLabel);

  List<SysDictData> getDictListAll(SysDictDataDTO dictData);

  void updateByTypeAndLabel(SysDictData data);
}
