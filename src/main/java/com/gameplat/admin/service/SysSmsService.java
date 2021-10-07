package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysSmsQueryDTO;
import com.gameplat.admin.model.entity.SysSms;

import java.util.List;

/**
 * @author Lenovo
 */
public interface SysSmsService extends IService<SysSms> {

    List<SysSms> listByQueryDto(SysSmsQueryDTO sysSmsQueryDto);
}
