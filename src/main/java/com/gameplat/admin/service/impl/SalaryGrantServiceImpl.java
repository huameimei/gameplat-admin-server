package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SalaryGrantMapper;
import com.gameplat.admin.model.dto.SalaryGrantDTO;
import com.gameplat.admin.model.vo.SalaryGrantVO;
import com.gameplat.admin.service.SalaryGrantService;
import com.gameplat.model.entity.proxy.SalaryGrant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SalaryGrantServiceImpl
        extends ServiceImpl<SalaryGrantMapper, SalaryGrant>
        implements SalaryGrantService {
    @Override
    public IPage<SalaryGrantVO> queryPage(PageDTO<SalaryGrant> page, SalaryGrantDTO dto) {
        return null;
    }
}
