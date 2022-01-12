package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysSmsAreaConvert;
import com.gameplat.admin.mapper.SysSmsAreaMapper;
import com.gameplat.admin.model.domain.SysSmsArea;
import com.gameplat.admin.model.dto.SmsAreaAddDTO;
import com.gameplat.admin.model.dto.SmsAreaEditDTO;
import com.gameplat.admin.model.dto.SmsAreaQueryDTO;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import com.gameplat.admin.service.SysSmsAreaService;
import com.gameplat.base.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysSmsAreaServiceImpl extends ServiceImpl<SysSmsAreaMapper, SysSmsArea>
    implements SysSmsAreaService {

    @Autowired
    private SysSmsAreaConvert areaConvert;
    @Autowired
    private SysSmsAreaMapper areaMapper;

    @Override
    public IPage<SysSmsAreaVO> findSmsAreaList(PageDTO<SysSmsArea> page, SmsAreaQueryDTO queryDTO) {
        return this.lambdaQuery()
                .eq(ObjectUtils.isNotEmpty(queryDTO.getCode()), SysSmsArea::getCode, queryDTO.getCode())
                .eq(ObjectUtils.isNotEmpty(queryDTO.getName()), SysSmsArea::getName, queryDTO.getName())
                .page(page)
                .convert(areaConvert::toVo);
    }

    @Override
    public void addSmsArea(SmsAreaAddDTO addDTO) {
        SysSmsArea sysSmsArea = areaConvert.toEntity(addDTO);
        if (areaMapper.insert(sysSmsArea) == 0) {
            throw new ServiceException("新增区号配置失败");
        }
    }

    @Override
    public void editSmsArea(SmsAreaEditDTO editDTO) {
        SysSmsArea sysSmsArea = areaConvert.toEntity(editDTO);
        if (areaMapper.updateById(sysSmsArea) == 0) {
            throw new ServiceException("更新区号配置失败!");
        }
    }

    @Override
    public void deleteAreaById(Long id) {
        if (areaMapper.deleteById(id) == 0) {
            throw new ServiceException("删除区号配置失败!");
        }
    }

    @Override
    public void changeStatus(Long id, Integer status) {
        SysSmsArea area = this.lambdaQuery()
                .eq(SysSmsArea::getId, id).one();
        if(ObjectUtils.isNull(area)) {
            throw new ServiceException("修改状态失败");
        }
        area.setStatus(status);
        if(areaMapper.updateById(area) == 0) {
            throw new ServiceException("修改状态失败");
        }
    }
}
