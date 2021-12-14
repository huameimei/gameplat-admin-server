package com.gameplat.admin.controller.open.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityRedPacket;
import com.gameplat.admin.model.dto.ActivityRedPacketAddDTO;
import com.gameplat.admin.model.dto.ActivityRedPacketQueryDTO;
import com.gameplat.admin.model.dto.ActivityRedPacketUpdateDTO;
import com.gameplat.admin.model.vo.ActivityRedPacketVO;
import com.gameplat.admin.service.ActivityRedPacketService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 活动红包雨
 *
 * @author admin
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/activity/redpacket")
@Api(tags = "活动红包雨管理")
public class ActivityRedPacketController {

    @Autowired
    private ActivityRedPacketService activityRedPacketService;

    /**
     * 活动红包雨列表
     *
     * @param page
     * @param activityRedPacketQueryDTO
     * @return
     */
    @ApiOperation(value = "活动红包雨列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('activity:redpacket:list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "分页参数：当前页", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页条数"),
    })
    public IPage<ActivityRedPacketVO> redPacketList(@ApiIgnore PageDTO<ActivityRedPacket> page, ActivityRedPacketQueryDTO activityRedPacketQueryDTO) {
        return activityRedPacketService.redPacketList(page, activityRedPacketQueryDTO);
    }

    /**
     * 新增红包雨
     *
     * @param activityRedPacketAddDTO
     */
    @ApiOperation(value = "新增红包雨")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('activity:redpacket:add')")
    public void add(@RequestBody ActivityRedPacketAddDTO activityRedPacketAddDTO) {
        if (activityRedPacketAddDTO.getPacketType() == null || activityRedPacketAddDTO.getPacketType() == 0) {
            throw new ServiceException("红包类型必须");
        }
        if (activityRedPacketAddDTO.getBeginTime() == null || activityRedPacketAddDTO.getEndTime() == null) {
            throw new ServiceException("红包雨时间必填");
        }
        if (StringUtils.isBlank(activityRedPacketAddDTO.getWeekTime())) {
            throw new ServiceException("红包雨时间必填");
        }
        if (StringUtils.isBlank(activityRedPacketAddDTO.getDuration())) {
            throw new ServiceException("红包雨持续时长必填");
        }
        if (StringUtils.isBlank(activityRedPacketAddDTO.getRealTitle())) {
            throw new ServiceException("红包雨标题不能为空");
        }
        if (StringUtils.isBlank(activityRedPacketAddDTO.getRealLocation())) {
            throw new ServiceException("红包雨位置不能为空");
        }
        activityRedPacketService.add(activityRedPacketAddDTO);
    }

    /**
     * 修改红包雨
     *
     * @param activityRedPacketUpdateDTO
     */
    @ApiOperation(value = "修改红包雨")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('activity:redpacket:edit')")
    public void edit(@RequestBody ActivityRedPacketUpdateDTO activityRedPacketUpdateDTO) {
        if (activityRedPacketUpdateDTO.getPacketId() == null || activityRedPacketUpdateDTO.getPacketId() == 0) {
            throw new ServiceException("红包雨ID必须");
        }
        if (activityRedPacketUpdateDTO.getPacketType() == null || activityRedPacketUpdateDTO.getPacketType() == 0) {
            throw new ServiceException("红包类型必须");
        }
        if (activityRedPacketUpdateDTO.getBeginTime() == null || activityRedPacketUpdateDTO.getEndTime() == null) {
            throw new ServiceException("红包雨时间必填");
        }
        if (StringUtils.isBlank(activityRedPacketUpdateDTO.getWeekTime())) {
            throw new ServiceException("红包雨时间必填");
        }
        if (StringUtils.isBlank(activityRedPacketUpdateDTO.getDuration())) {
            throw new ServiceException("红包雨持续时长必填");
        }
        if (StringUtils.isBlank(activityRedPacketUpdateDTO.getRealTitle())) {
            throw new ServiceException("红包雨标题不能为空");
        }
        if (StringUtils.isBlank(activityRedPacketUpdateDTO.getRealLocation())) {
            throw new ServiceException("红包雨位置不能为空");
        }
        activityRedPacketService.edit(activityRedPacketUpdateDTO);
    }

    /**
     * 更新红包雨状态
     *
     * @param packetId
     */
    @ApiOperation(value = "更新红包雨状态")
    @PutMapping("/updateStatus")
    @PreAuthorize("hasAuthority('activity:redpacket:edit')")
    public void updateStatus(@RequestParam Long packetId) {
        activityRedPacketService.updateStatus(packetId);
    }

    /**
     * 更新红包雨状态
     *
     * @param ids
     */
    @ApiOperation(value = "更新红包雨状态")
    @PutMapping("/delete")
    @PreAuthorize("hasAuthority('activity:redpacket:remove')")
    public void delete(@RequestBody String ids) {
        activityRedPacketService.delete(ids);
    }


}
