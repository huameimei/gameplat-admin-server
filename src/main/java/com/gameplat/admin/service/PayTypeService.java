package com.gameplat.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.PayType;
import com.gameplat.admin.model.dto.PayTypeAddDTO;
import com.gameplat.admin.model.dto.PayTypeEditDTO;
import com.gameplat.admin.model.vo.PayTypeVO;
import java.util.List;

public interface PayTypeService extends IService<PayType> {

  /**
   * 修改支付方式
   *
   * @param dto PayTypeUpdateDTO
   */
  void update(PayTypeEditDTO dto);

  void updateStatus(Long id, Integer status);

  /**
   * 保存
   *
   * @param dto PayTypeAddDTO
   */
  void save(PayTypeAddDTO dto);

  /**
   * 删除支付方式
   *
   * @param id Long
   */
  void delete(Long id);

  /**
   * 支付方式列表
   *
   * @return List
   */
  List<PayTypeVO> queryList(String name);

  /**
   * 支付方式列表
   *
   * @param page Page
   * @return IPage
   */
  IPage<PayType> queryPage(Page<PayType> page);

  List<PayTypeVO> queryEnablePayTypes();

}
