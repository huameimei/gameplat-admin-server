package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysSmsAreaMapper;
import com.gameplat.admin.model.domain.SysSmsArea;
import com.gameplat.admin.service.SysSmsAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysSmsAreaServiceImpl extends ServiceImpl<SysSmsAreaMapper, SysSmsArea>
    implements SysSmsAreaService {}
