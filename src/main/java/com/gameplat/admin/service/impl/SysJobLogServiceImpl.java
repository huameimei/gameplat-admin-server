package com.gameplat.admin.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysJobLogMapper;
import com.gameplat.admin.model.bean.SysJobLog;
import com.gameplat.admin.service.SysJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLog>
    implements SysJobLogService {

  @Autowired private SysJobLogMapper sysJobLogMapper;

  @Override
  public IPage<SysJobLog> queryPage(PageDTO<SysJobLog> page, SysJobLog dto) {
    LambdaQueryWrapper<SysJobLog> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(StrUtil.isNotBlank(dto.getStatus()), SysJobLog::getStatus, dto.getStatus());
    queryWrapper.like(
        StrUtil.isNotBlank(dto.getMethodName()), SysJobLog::getMethodName, dto.getMethodName());
    queryWrapper.orderByDesc(SysJobLog::getCreateTime);
    return sysJobLogMapper.selectPage(page, queryWrapper);
  }

  @Override
  public void addJobLog(SysJobLog jobLog) {
    sysJobLogMapper.insert(jobLog);
  }

  @Override
  public void deleteJobLogByIds(String ids) {
    String[] strings = Convert.toStrArray(ids);
    for (String id : strings) {
      sysJobLogMapper.deleteById(Long.valueOf(id));
    }
  }

  @Override
  public void cleanJobLog() {
    this.remove(new QueryWrapper<>());
  }
}
