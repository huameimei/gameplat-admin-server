package com.gameplat.admin.controller.open.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityBlacklist;
import com.gameplat.admin.model.dto.ActivityBlacklistAddDTO;
import com.gameplat.admin.model.dto.ActivityBlacklistDTO;
import com.gameplat.admin.model.vo.ActivityBlacklistVO;
import com.gameplat.admin.service.ActivityBlacklistService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 黑名单管理
 *
 * @author admin
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/activity/blacklist")
@Api(tags = "活动黑名单管理")
public class ActivityBlacklistController {

    @Autowired
    private ActivityBlacklistService activityBlacklistService;

    /**
     * 活动列表
     */
    @ApiOperation(value = "活动黑名单列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('activity:blacklist:list')")
    public IPage<ActivityBlacklistVO> list(PageDTO<ActivityBlacklist> page, ActivityBlacklistDTO activityBlacklistDTO) {
        return activityBlacklistService.list(page, activityBlacklistDTO);
    }

    @ApiOperation(value = "新增活动黑名单")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('activity:blacklist:add')")
    public void add(@RequestBody ActivityBlacklistAddDTO activityBlacklistAddDTO) {
        if (StringUtils.isNull(activityBlacklistAddDTO.getActivityId())) {
            throw new ServiceException("活动ID不能为空");
        }
        if (StringUtils.isEmpty(activityBlacklistAddDTO.getLimitedContent())) {
            throw new ServiceException("限制内容不能为空");
        }
        if (StringUtils.isNull(activityBlacklistAddDTO.getLimitedType())) {
            throw new ServiceException("限制类型不能为空");
        }
        activityBlacklistService.add(activityBlacklistAddDTO);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @ApiOperation(value = "删除活动黑名单")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('activity:blacklist:remove')")
    public void remove(@RequestBody String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException("ids不能为空");
        }
        activityBlacklistService.remove(ids);
    }


}
