package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysSmsAreaConvert;
import com.gameplat.admin.mapper.SysSmsAreaMapper;
import com.gameplat.admin.model.dto.SmsAreaAddDTO;
import com.gameplat.admin.model.dto.SmsAreaEditDTO;
import com.gameplat.admin.model.dto.SmsAreaQueryDTO;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import com.gameplat.admin.service.SysSmsAreaService;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.sys.SysSmsArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SysSmsAreaServiceImpl extends ServiceImpl<SysSmsAreaMapper, SysSmsArea>
    implements SysSmsAreaService {

  @Autowired private SysSmsAreaConvert areaConvert;

  @Override
  public IPage<SysSmsAreaVO> findSmsAreaList(PageDTO<SysSmsArea> page, SmsAreaQueryDTO queryDTO) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(queryDTO.getCode()), SysSmsArea::getCode, queryDTO.getCode())
        .eq(ObjectUtils.isNotEmpty(queryDTO.getName()), SysSmsArea::getName, queryDTO.getName())
        .orderByDesc(SysSmsArea::getCreateTime)
        .page(page)
        .convert(areaConvert::toVo);
  }

  @Override
  public void addSmsArea(SmsAreaAddDTO addDTO) {
    SysSmsArea sysSmsArea = areaConvert.toEntity(addDTO);
    Assert.isTrue(this.save(sysSmsArea), "新增区号配置失败!");
  }

  @Override
  public void editSmsArea(SmsAreaEditDTO editDTO) {
    SysSmsArea sysSmsArea = areaConvert.toEntity(editDTO);
    Assert.isTrue(this.updateById(sysSmsArea), "更新区号配置失败!");
  }

  @Override
  public void deleteAreaById(Long id) {
    Assert.isTrue(this.removeById(id), "删除区号配置失败!");
  }

  @Override
  public void changeStatus(Long id, Integer status) {
    SysSmsArea area = Assert.notNull(this.getById(id), "区号不存在");
    area.setStatus(status);
    Assert.isTrue(this.updateById(area), "修改状态失败");
  }

  @Override
  public void setDefaultStatus(Long id, Integer status) {
    SysSmsArea area = Assert.notNull(this.getById(id), "区号不存在");
    this.lambdaUpdate().set(SysSmsArea::getIsDefault,0).eq(SysSmsArea::getIsDefault,1).update();
    area.setIsDefault(status);
    Assert.isTrue(this.updateById(area), "修改状态失败");
  }
}
