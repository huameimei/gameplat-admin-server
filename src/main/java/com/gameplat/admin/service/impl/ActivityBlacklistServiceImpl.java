package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityBlacklistConvert;
import com.gameplat.admin.enums.ActivityBlacklistEnum;
import com.gameplat.admin.mapper.ActivityBlacklistMapper;
import com.gameplat.admin.model.domain.ActivityBlacklist;
import com.gameplat.admin.model.dto.ActivityBlacklistAddDTO;
import com.gameplat.admin.model.dto.ActivityBlacklistDTO;
import com.gameplat.admin.model.vo.ActivityBlacklistVO;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.service.ActivityBlacklistService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 活动黑名单业务处理
 *
 * @author kenvin
 */
@Service
public class ActivityBlacklistServiceImpl extends ServiceImpl<ActivityBlacklistMapper, ActivityBlacklist>
        implements ActivityBlacklistService {

    @Autowired
    private ActivityBlacklistConvert activityBlacklistConvert;

    @Autowired
    private MemberService memberService;

    @Override
    public IPage<ActivityBlacklistVO> list(PageDTO<ActivityBlacklist> page, ActivityBlacklistDTO activityBlacklistDTO) {
        return this.lambdaQuery()
                .like(StringUtils.isNotBlank(activityBlacklistDTO.getLimitedContent())
                        , ActivityBlacklist::getLimitedContent, activityBlacklistDTO.getLimitedContent())
                .eq(activityBlacklistDTO.getLimitedType() != null && activityBlacklistDTO.getLimitedType() != 0
                        , ActivityBlacklist::getLimitedType, activityBlacklistDTO.getLimitedType())
                .eq(activityBlacklistDTO.getActivityId() != null && activityBlacklistDTO.getActivityId() != 0
                        , ActivityBlacklist::getActivityId, activityBlacklistDTO.getActivityId())
                .page(page).convert(activityBlacklistConvert::toVo);
    }

    @Override
    public boolean add(ActivityBlacklistAddDTO activityBlacklistAddDTO) {
        if (activityBlacklistAddDTO.getLimitedType() == ActivityBlacklistEnum.MEMBER.getValue()) {
            MemberInfoVO memberInfo = memberService.getMemberInfo(activityBlacklistAddDTO.getLimitedContent());
            if (StringUtils.isNull(memberInfo)) {
                throw new ServiceException("限制内容框中输入的会员账号不存在");
            }
        } else if (activityBlacklistAddDTO.getLimitedType() == ActivityBlacklistEnum.IP.getValue()) {
            if (!isIp(activityBlacklistAddDTO.getLimitedContent())) {
                throw new ServiceException("限制内容框中输入的IP地址不是一个有效的IP地址");
            }
        }
        ActivityBlacklist activityBlacklist = activityBlacklistConvert.toEntity(activityBlacklistAddDTO);
        return this.save(activityBlacklist);
    }

    @Override
    public void remove(String ids) {
        String[] idArr = ids.split(",");
        List<Long> idList = new ArrayList<>();
        for (String idStr : idArr) {
            idList.add(Long.parseLong(idStr));
        }
        this.removeByIds(idList);
    }

    public boolean isIp(String ip) {//判断是否是一个IP
        boolean b = false;
        ip = this.removeBlankSpace(ip);
        if (ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String s[] = ip.split("\\.");
            if (Integer.parseInt(s[0]) < 255) {
                if (Integer.parseInt(s[1]) < 255) {
                    if (Integer.parseInt(s[2]) < 255) {
                        if (Integer.parseInt(s[3]) < 255) {
                            b = true;
                        }
                    }
                }
            }
        }
        return b;
    }

    public String removeBlankSpace(String ip) {//去掉IP字符串前后所有的空格
        while (ip.startsWith(" ")) {
            ip = ip.substring(1, ip.length()).trim();
        }
        while (ip.endsWith(" ")) {
            ip = ip.substring(0, ip.length() - 1).trim();
        }
        return ip;
    }
}
