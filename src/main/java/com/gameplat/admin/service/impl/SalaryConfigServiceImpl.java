package com.gameplat.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.TrueFalse;
import com.gameplat.admin.convert.SalaryConfigConvert;
import com.gameplat.admin.mapper.SalaryConfigMapper;
import com.gameplat.admin.mapper.SysDictDataMapper;
import com.gameplat.admin.model.dto.SalaryConfigDTO;
import com.gameplat.admin.model.vo.SalaryConfigVO;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.SalaryConfigService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.model.entity.proxy.SalaryConfig;
import com.gameplat.model.entity.sys.SysDictData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SalaryConfigServiceImpl extends ServiceImpl<SalaryConfigMapper, SalaryConfig>
    implements SalaryConfigService {

  @Autowired private SalaryConfigMapper salaryConfigMapper;

  @Autowired private SalaryConfigConvert salaryConfigConvert;

  @Autowired private MemberService memberService;

  @Autowired private SysDictDataMapper sysDictDataMapper;

  @Override
  public IPage<SalaryConfigVO> queryPage(PageDTO<SalaryConfig> page, SalaryConfigDTO dto) {
    QueryWrapper<SalaryConfig> queryWrapper = new QueryWrapper<>();
    queryWrapper
        .eq(StrUtil.isNotBlank(dto.getGameType()), "game_type", dto.getGameType())
        .eq(ObjectUtils.isNotNull(dto.getAgentLevel()), "agent_level", dto.getAgentLevel());
    queryWrapper.orderByAsc("settle_sort,agent_level");
    queryWrapper.orderByDesc("create_time");
    IPage<SalaryConfigVO> pageResult =
        salaryConfigMapper.selectPage(page, queryWrapper).convert(salaryConfigConvert::toVo);
    List<SysDictData> liveGameTypeList = sysDictDataMapper.findDataByType("LIVE_GAME_TYPE", "1");
    Map<String, List<SysDictData>> map =
        liveGameTypeList.stream().collect(Collectors.groupingBy(SysDictData::getDictValue));
    for (SalaryConfigVO vo : pageResult.getRecords()) {
      vo.setGameTypeName(map.get(vo.getGameType()).get(0).getDictLabel());
      vo.setIsDirectBol(EnableEnum.isEnabled(vo.getIsDirect()));
    }
    return pageResult;
  }

  @Override
  public Integer getMaxLevel() {
    return memberService.getMaxLevel();
  }

  @Override
  public void add(SalaryConfigDTO dto) {
    SalaryConfig saveObj = salaryConfigConvert.toEntity(dto);
    saveObj.setIsDirect(TrueFalse.valueOf(dto.getIsDirectBol()));
    Assert.isTrue(this.save(saveObj), "添加工资配置失败！");
  }

  @Override
  public void edit(SalaryConfigDTO dto) {
    Assert.isTrue(dto.getId() != null, "主键ID参数缺失！");
    SalaryConfig editObj = salaryConfigConvert.toEntity(dto);
    editObj.setIsDirect(
        dto.getIsDirectBol() ? TrueFalse.TRUE.getValue() : TrueFalse.FALSE.getValue());
    Assert.isTrue(this.updateById(editObj), "编辑工资配置失败！");
  }

  @Override
  public void delete(String ids) {
    Assert.isTrue(StrUtil.isNotBlank(ids), "参数为空！");
    String[] idArr = ids.split(",");
    for (String id : idArr) {
      Assert.isTrue(this.removeById(id), ("删除失败！"));
    }
  }
}
