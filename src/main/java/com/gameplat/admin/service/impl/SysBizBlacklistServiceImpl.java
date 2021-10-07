package com.gameplat.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysBizBlacklistConvert;
import com.gameplat.admin.dao.SysBizBlacklistMapper;
import com.gameplat.admin.enums.BizBlackListTargetTypeEnum;
import com.gameplat.admin.enums.BizBlackListTypeEnum;
import com.gameplat.admin.enums.BizBlacklistStatusEnum;
import com.gameplat.admin.model.dto.SysBizBlackListQueryDTO;
import com.gameplat.admin.model.dto.SysBizBlacklistAddDTO;
import com.gameplat.admin.model.dto.SysBizBlacklistUpdateDTO;
import com.gameplat.admin.model.entity.MemberInfo;
import com.gameplat.admin.model.entity.SysBizBlacklist;
import com.gameplat.admin.model.vo.SysBizBlacklistVO;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.admin.service.SysBizBlacklistService;
import com.gameplat.common.exception.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@CacheConfig(cacheNames = "biz_blacklists")
public class SysBizBlacklistServiceImpl extends ServiceImpl<SysBizBlacklistMapper, SysBizBlacklist>
        implements SysBizBlacklistService {

    @Resource
    private SysBizBlacklistMapper sysBizBlacklistMapper;

    @Resource
    private SysBizBlacklistConvert sysBizBlacklistConvert;

    @Resource
    private MemberInfoService memberInfoService;

    @Override
    public IPage<SysBizBlacklistVO> queryPage(
            Page<SysBizBlacklist> page, SysBizBlackListQueryDTO queryDTO) {
        if (StringUtils.isNotBlank(queryDTO.getAccount())) {
            LambdaQueryWrapper<MemberInfo> memberInfoQuery = Wrappers.lambdaQuery();
            memberInfoQuery.eq(MemberInfo::getAccount, queryDTO.getAccount());
            MemberInfo memberInfo = memberInfoService.getOne(memberInfoQuery);
            if (memberInfo == null) {
                return new Page<>();
            }
            queryDTO.setAccountUserLevel(memberInfo.getUserLevel());
        } else {
            queryDTO.setAccount(null);
            queryDTO.setAccountUserLevel(null);
        }
        // 执行查询
        LambdaQueryWrapper<SysBizBlacklist> query = Wrappers.lambdaQuery();
        query
                .eq(SysBizBlacklist::getTarget, queryDTO.getUserLevel())
                .eq(SysBizBlacklist::getTargetType, BizBlackListTargetTypeEnum.LEVEL.getValue())
                .and(
                        wrapper ->
                                wrapper
                                        .eq(SysBizBlacklist::getTarget, queryDTO.getAccount())
                                        .eq(
                                                SysBizBlacklist::getTargetType,
                                                BizBlackListTargetTypeEnum.MEMBER.getValue())
                                        .or()
                                        .eq(SysBizBlacklist::getTarget, queryDTO.getAccountUserLevel())
                                        .eq(
                                                SysBizBlacklist::getTargetType,
                                                BizBlackListTargetTypeEnum.LEVEL.getValue()))
                .eq(SysBizBlacklist::getStatus, BizBlacklistStatusEnum.ENABLED.getValue());
        return this.page(page, query).convert(sysBizBlacklistConvert::toVo);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void save(SysBizBlacklistAddDTO sysBizBlacklistAddDTO) {
        validateBizBlacklist(sysBizBlacklistAddDTO);
        LambdaQueryWrapper<SysBizBlacklist> query = Wrappers.lambdaQuery();
        query.eq(SysBizBlacklist::getTarget, sysBizBlacklistAddDTO.getTarget());
        query.eq(SysBizBlacklist::getTargetType, sysBizBlacklistAddDTO.getTargetType());
        SysBizBlacklist exists = sysBizBlacklistMapper.selectOne(query);
        if (exists != null) {
            SysBizBlacklist update = new SysBizBlacklist();
            update.setId(exists.getId());
            if (sysBizBlacklistAddDTO.isReplaceExists()) {
                update.setTypes(sysBizBlacklistAddDTO.getTypes());
            } else {
                Set<String> types =
                        Stream.of(exists.getTypes().split(StrUtil.COMMA)).collect(Collectors.toSet());
                types.addAll(Arrays.asList(sysBizBlacklistAddDTO.getTypes().split(StrUtil.COMMA)));
                update.setTypes(StrUtil.join(StrUtil.COMMA, types));
            }
            update.setRemark(sysBizBlacklistAddDTO.getRemark());
            update.setStatus(sysBizBlacklistAddDTO.getStatus());
            sysBizBlacklistMapper.updateById(update);
        } else {
            SysBizBlacklist sysBizBlacklist = sysBizBlacklistConvert.toEntity(sysBizBlacklistAddDTO);
            sysBizBlacklistMapper.insert(sysBizBlacklist);
        }
    }

    @CacheEvict(allEntries = true)
    @Override
    public void delete(Long id) {
        Optional.ofNullable(id).orElseThrow(() -> new ServiceException("无效的ID"));
        if (sysBizBlacklistMapper.deleteById(id) == 0) {
            throw new ServiceException("无效的ID");
        }
    }

    @CacheEvict(allEntries = true)
    @Override
    public void update(SysBizBlacklistUpdateDTO sysBizBlacklistUpdateDTO) {
        validateBizBlacklist(sysBizBlacklistUpdateDTO);
        Optional.ofNullable(sysBizBlacklistUpdateDTO.getId())
                .map(sysBizBlacklistMapper::selectById)
                .orElseThrow(() -> new ServiceException("无效的ID"));
        SysBizBlacklist update = sysBizBlacklistConvert.toEntity(sysBizBlacklistUpdateDTO);
        sysBizBlacklistMapper.updateById(update);
    }

    private void validateBizBlacklist(SysBizBlacklistAddDTO bizBlacklist) throws ServiceException {
        Optional.ofNullable(BizBlackListTargetTypeEnum.matches(bizBlacklist.getTargetType()))
                .orElseThrow(() -> new ServiceException("无效的目标类型"));
        String[] types = bizBlacklist.getTypes().split(StrUtil.COMMA);
        for (String type : types) {
            Optional.ofNullable(BizBlackListTypeEnum.matches(type))
                    .orElseThrow(() -> new ServiceException("无效的业务类型"));
        }
        Optional.ofNullable(BizBlacklistStatusEnum.matches(bizBlacklist.getStatus()))
                .orElseThrow(() -> new ServiceException("无效的状态"));
    }

    private void validateBizBlacklist(SysBizBlacklistUpdateDTO bizBlacklist) throws ServiceException {
        if (bizBlacklist.getTypes() != null) {
            String[] types = bizBlacklist.getTypes().split(StrUtil.COMMA);
            for (String type : types) {
                Optional.ofNullable(BizBlackListTypeEnum.matches(type))
                        .orElseThrow(() -> new ServiceException("无效的业务类型"));
            }
        }
        if (bizBlacklist.getStatus() != null) {
            Optional.ofNullable(BizBlacklistStatusEnum.matches(bizBlacklist.getStatus()))
                    .orElseThrow(() -> new ServiceException("无效的状态"));
        }
    }

    private Set<String> combineAllTypes(SysBizBlacklist... bizBlacklists) {
        Set<String> types = new HashSet<>();
        Optional.ofNullable(bizBlacklists)
                .ifPresent(
                        bls ->
                                Stream.of(bls)
                                        .filter(Objects::nonNull)
                                        .forEach(
                                                bl -> types.addAll(Arrays.asList(bl.getTypes().split(StrUtil.COMMA)))));
        return types;
    }

    @Cacheable(key = "'member_' + #memberId")
    public Set<String> getBizBlacklistTypesByMemberId(Long memberId) throws ServiceException {
        return getBizBlacklistTypesByMember(
                Optional.ofNullable(memberId)
                        .map(memberInfoService::getById)
                        .orElseThrow(() -> new ServiceException("无效的用户ID")));
    }

    @Cacheable(key = "'member_' + #memberInfo.id")
    public Set<String> getBizBlacklistTypesByMember(MemberInfo memberInfo) throws ServiceException {
        return Optional.ofNullable(memberInfo)
                .map(
                        u -> {
                            // 会员黑名单
                            SysBizBlacklist userBizBlacklist =
                                    this.lambdaQuery()
                                            .eq(SysBizBlacklist::getTarget, memberInfo.getAccount())
                                            .eq(
                                                    SysBizBlacklist::getTargetType,
                                                    BizBlackListTargetTypeEnum.MEMBER.getValue())
                                            .eq(SysBizBlacklist::getStatus, BizBlacklistStatusEnum.ENABLED.getValue())
                                            .one();
                            // 层级黑名单
                            SysBizBlacklist userLevelBizBlacklist =
                                    this.lambdaQuery()
                                            .eq(SysBizBlacklist::getTarget, memberInfo.getUserLevel())
                                            .eq(
                                                    SysBizBlacklist::getTargetType,
                                                    BizBlackListTargetTypeEnum.LEVEL.getValue())
                                            .eq(SysBizBlacklist::getStatus, BizBlacklistStatusEnum.ENABLED.getValue())
                                            .one();
                            return combineAllTypes(userBizBlacklist, userLevelBizBlacklist);
                        })
                .orElseThrow(() -> new ServiceException("无效的用户信息"));
    }
}
