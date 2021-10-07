package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.PayTypeConvert;
import com.gameplat.admin.dao.PayTypeMapper;
import com.gameplat.admin.model.dto.PayTypeAddDTO;
import com.gameplat.admin.model.dto.PayTypeEditDTO;
import com.gameplat.admin.model.entity.PayType;
import com.gameplat.admin.model.vo.PayTypeVO;
import com.gameplat.admin.service.PayTypeService;
import com.gameplat.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class PayTypeServiceImpl extends ServiceImpl<PayTypeMapper, PayType>
        implements PayTypeService {

    @Autowired
    private PayTypeConvert payTypeConvert;

    @Autowired
    private PayTypeMapper payTypeMapper;

    @Override
    public List<PayTypeVO> queryList() {
        return this.list().stream().map(e -> payTypeConvert.toVo(e)).collect(Collectors.toList());
    }

    @Override
    public void save(PayTypeAddDTO dto) throws ServiceException {
        LambdaQueryWrapper<PayType> query = Wrappers.lambdaQuery();
        query.eq(PayType::getCode, dto.getCode());
        if (this.count(query) > 0) {
            throw new ServiceException("支付方式已存在");
        }
        dto.setIsSysCode(0);
        if (!this.save(payTypeConvert.toEntity(dto))) {
            throw new ServiceException("添加失败!");
        }
    }

    @Override
    public void update(PayTypeEditDTO dto) {
        PayType payType = null;
        if (1 == dto.getIsSysCode()) {
            payType = new PayType();
            payType.setId(dto.getId());
            payType.setName(dto.getName());
            payType.setSort(dto.getSort());
            payType.setRechargeDesc(dto.getRechargeDesc());
        } else {
            payType = payTypeConvert.toEntity(dto);
            payType.setIsSysCode(0);
        }
        if (!this.updateById(payType)) {
            throw new ServiceException("更新失败!");
        }
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        if (null == status) {
            throw new ServiceException("状态不能为空!");
        }
        LambdaUpdateWrapper<PayType> update = Wrappers.lambdaUpdate();
        update.set(PayType::getStatus, status);
        update.eq(PayType::getId, id);
        this.update(update);
    }

    @Override
    public void delete(Long id) {
        PayType payType = payTypeMapper.selectById(id);
        if (1 == payType.getIsSysCode()) {
            throw new ServiceException("系统支付编码无法删除!");
        }
        payType.deleteById();
    }

    @Override
    public IPage<PayType> queryPage(Page<PayType> page) {
        LambdaQueryWrapper<PayType> query = Wrappers.lambdaQuery();
        query.orderByAsc(PayType::getSort);
        return this.page(page);
    }
}
