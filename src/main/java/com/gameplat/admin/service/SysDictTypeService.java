package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.SysDictType;
import com.gameplat.admin.model.dto.OperDictTypeDTO;
import com.gameplat.admin.model.vo.DictTypeVO;

import java.util.List;

/**
 * 字典类型 业务层处理
 *
 * @author three
 */
public interface SysDictTypeService extends IService<SysDictType> {

  /**
   * 字典类型列表
   *
   * @param typeDTO
   * @return List
   */
  List<DictTypeVO> selectDictTypeList(OperDictTypeDTO typeDTO);

  void addDictType(OperDictTypeDTO typeDTO);

  void editDictType(OperDictTypeDTO typeDTO);

  void removeDictTypeById(Long id);
}
