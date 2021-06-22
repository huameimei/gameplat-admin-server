package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysSmsQueryDto;
import com.gameplat.admin.model.entity.SysSms;
import java.util.List;

/**
 * @author Lenovo
 */
public interface SysSmsService extends IService<SysSms> {

    List<SysSms> listByQueryDto(SysSmsQueryDto sysSmsQueryDto);
}
