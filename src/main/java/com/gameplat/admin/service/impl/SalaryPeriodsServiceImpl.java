package com.gameplat.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.TrueFalse;
import com.gameplat.admin.convert.SalaryPeriodsConvert;
import com.gameplat.admin.mapper.SalaryGrantMapper;
import com.gameplat.admin.mapper.SalaryPeriodsMapper;
import com.gameplat.admin.mapper.SysDictDataMapper;
import com.gameplat.admin.model.dto.SalaryConfigDTO;
import com.gameplat.admin.model.dto.SalaryPeriodsDTO;
import com.gameplat.admin.model.vo.SalaryConfigVO;
import com.gameplat.admin.model.vo.SalaryPeriodsVO;
import com.gameplat.admin.service.SalaryPeriodsService;
import com.gameplat.model.entity.proxy.DivideDetail;
import com.gameplat.model.entity.proxy.SalaryConfig;
import com.gameplat.model.entity.proxy.SalaryGrant;
import com.gameplat.model.entity.proxy.SalaryPeriods;
import com.gameplat.model.entity.sys.SysDictData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
@SuppressWarnings("all")
public class SalaryPeriodsServiceImpl
        extends ServiceImpl<SalaryPeriodsMapper, SalaryPeriods>
        implements SalaryPeriodsService {

    @Autowired private SalaryPeriodsMapper salaryPeriodsMapper;
    @Autowired private SalaryPeriodsConvert salaryPeriodsConvert;
    @Autowired private SysDictDataMapper sysDictDataMapper;
    @Autowired private SalaryGrantMapper salaryGrantMapper;

    @Override
    public IPage<SalaryPeriodsVO> queryPage(PageDTO<SalaryPeriods> page, SalaryPeriodsDTO dto) {
        QueryWrapper<SalaryPeriods> queryWrapper = new QueryWrapper();
        queryWrapper
                .eq(ObjectUtils.isNotNull(dto.getStatus()), "status", dto.getStatus());
        queryWrapper.orderByDesc("create_time");
        IPage<SalaryPeriodsVO> pageResult = salaryPeriodsMapper.selectPage(page, queryWrapper)
                .convert(salaryPeriodsConvert::toVo);
        List<SysDictData> liveGameTypeList = sysDictDataMapper.findDataByType("LIVE_GAME_TYPE", "1");
        Map<String, List<SysDictData>> map = liveGameTypeList.stream().collect(Collectors.groupingBy(SysDictData::getDictValue));
        for (SalaryPeriodsVO vo : pageResult.getRecords()) {
            String gameType = vo.getGameType();
            String gameTypeName = "";
            if (StrUtil.isNotBlank(gameType)){
                String[] split = gameType.split(",");
                vo.setGameCheckList(split);
                for (String s : split) {
                    String dictLabel = map.get(s).get(0).getDictLabel();
                    gameTypeName += (dictLabel+",");
                }
                if (StrUtil.isNotBlank(gameTypeName)) {
                    gameTypeName = gameTypeName.endsWith(",") ? gameTypeName.substring(0,gameTypeName.length()-1) : gameTypeName;
                    vo.setGameTypeName(gameTypeName);
                }
            }

            if (StrUtil.isNotBlank(vo.getAgentLevel())) {
                String[] split = vo.getAgentLevel().split(",");
                vo.setAgentCheckList(Arrays.stream(split).mapToInt(Integer::valueOf).toArray());
                String agentLevelLebel = "";
                for (String s : split) {
                    agentLevelLebel += (s+"级,");
                }
                if (StrUtil.isNotBlank(agentLevelLebel)) {
                    agentLevelLebel = agentLevelLebel.endsWith(",") ? agentLevelLebel.substring(0,agentLevelLebel.length()-1) : agentLevelLebel;
                    vo.setAgentLevelLebel(agentLevelLebel);
                }
            }
        }
        return pageResult;
    }

    @Override
    public void add(SalaryPeriodsDTO dto) {
        SalaryPeriods saveObj = salaryPeriodsConvert.toEntity(dto);
        Assert.isTrue(this.save(saveObj),"添加工资期数失败！");
    }

    @Override
    public void edit(SalaryPeriodsDTO dto) {
        Assert.isTrue(dto.getId() != null, "主键ID参数缺失！");
        SalaryPeriods editObj = salaryPeriodsConvert.toEntity(dto);
        Assert.isTrue(this.updateById(editObj),"编辑工资期数失败！");
    }

    @Override
    public void delete(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "参数为空！");
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            QueryWrapper<SalaryGrant> deleteDetailWrapper = new QueryWrapper();
            deleteDetailWrapper.eq("periods_id", Long.valueOf(id));
            int delete = salaryGrantMapper.delete(deleteDetailWrapper);
            Assert.isTrue(this.removeById(id), ("删除失败！"));
        }
    }
}
