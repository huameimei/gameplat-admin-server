package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysEmailQueryDTO;
import com.gameplat.admin.model.entity.SysEmail;
import com.gameplat.admin.model.vo.SysEmailVO;

/**
 * @author Lenovo
 */
public interface SysEmailService extends IService<SysEmail> {

    IPage<SysEmailVO> queryPage(Page<SysEmail> page, SysEmailQueryDTO sysEmailQueryDto);
}
