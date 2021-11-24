package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityLobbyConvert;
import com.gameplat.admin.mapper.ActivityLobbyDao;
import com.gameplat.admin.model.domain.ActivityLobby;
import com.gameplat.admin.model.dto.ActivityLobbyAddDTO;
import com.gameplat.admin.model.dto.ActivityLobbyDTO;
import com.gameplat.admin.model.vo.ActivityLobbyVO;
import com.gameplat.admin.service.ActivityLobbyService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 活动大厅业务
 */
@Service
public class ActivityLobbyServiceImpl extends ServiceImpl<ActivityLobbyDao, ActivityLobby>
        implements ActivityLobbyService {

    @Autowired
    private ActivityLobbyConvert activityLobbyConvert;


    @Override
    public IPage<ActivityLobbyVO> findActivityLobbyList(PageDTO<ActivityLobby> page, ActivityLobbyDTO activityLobbyDTO) {
        LambdaQueryChainWrapper<ActivityLobby> queryWrapper = this.lambdaQuery();
        queryWrapper.like(StringUtils.isNotBlank(activityLobbyDTO.getTitle()), ActivityLobby::getTitle, activityLobbyDTO.getTitle())
                .eq(activityLobbyDTO.getStatus() != null && activityLobbyDTO.getStatus() != 0, ActivityLobby::getStatus, activityLobbyDTO.getStatus());


        return queryWrapper.page(page).convert(activityLobbyConvert::toVo);
    }

    @Override
    public void add(ActivityLobbyAddDTO activityLobbyAddDTO) {
        ActivityLobby activityLobby = activityLobbyConvert.toEntity(activityLobbyAddDTO);

        if (!this.save(activityLobby)) {
            throw new ServiceException("保存活动大厅异常");
        }
    }
}
