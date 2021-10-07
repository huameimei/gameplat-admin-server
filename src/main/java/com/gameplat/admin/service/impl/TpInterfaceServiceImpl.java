package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.TpInterfaceConvert;
import com.gameplat.admin.dao.TpInterfaceMapper;
import com.gameplat.admin.model.entity.TpInterface;
import com.gameplat.admin.model.vo.TpInterfaceVO;
import com.gameplat.admin.service.TpInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class TpInterfaceServiceImpl extends ServiceImpl<TpInterfaceMapper, TpInterface>
        implements TpInterfaceService {

    @Autowired
    private TpInterfaceConvert tpInterfaceConvert;

    @Autowired
    private TpInterfaceMapper tpInterfaceMapper;

    @Override
    public List<TpInterfaceVO> queryAll() {
        return this.list().stream().map(e -> tpInterfaceConvert.toVo(e)).collect(Collectors.toList());
    }

    @Override
    public TpInterfaceVO queryTpInterface(String interfaceCode) {
        LambdaQueryWrapper<TpInterface> query = Wrappers.lambdaQuery();
        query.eq(TpInterface::getCode, interfaceCode);
        return tpInterfaceConvert.toVo(this.getOne(query));
    }
}
