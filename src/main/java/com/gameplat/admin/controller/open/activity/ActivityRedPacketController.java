package com.gameplat.admin.controller.open.activity;

import com.gameplat.admin.model.dto.ActivityRedPacketConfigDTO;
import com.gameplat.admin.model.vo.ActivityRedPacketConfigVO;
import com.gameplat.admin.service.ActivityRedPacketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 活动红包配置
 *
 * @author kenvin
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/activity/redpacket")
@Api(tags = "活动红包配置")
public class ActivityRedPacketController {

    @Autowired
    private ActivityRedPacketService activityRedPacketService;


    /**
     * 获取红包配置
     *
     * @return
     */
    @ApiOperation(value = "获取红包配置")
    @GetMapping("/getConfig")
    @PreAuthorize("hasAuthority('activity:redpacket:view')")
    public ActivityRedPacketConfigVO getConfig() {
        return activityRedPacketService.getConfig();
    }

    /**
     * 更新红包配置
     *
     * @return
     */
    @ApiOperation(value = "更新红包配置")
    @PutMapping("/updateConfig")
    @PreAuthorize("hasAuthority('activity:redpacket:view')")
    public void updateConfig(@RequestBody ActivityRedPacketConfigDTO activityRedPacketConfigDTO) {
        activityRedPacketService.updateConfig(activityRedPacketConfigDTO);
    }


//    /**
//     * 活动红包列表
//     *
//     * @param page
//     * @param activityRedPacketQueryDTO
//     * @return
//     */
//    @ApiOperation(value = "活动红包列表")
//    @GetMapping("/list")
//    @PreAuthorize("hasAuthority('activity:redpacket:list')")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "current", value = "分页参数：当前页", defaultValue = "1"),
//            @ApiImplicitParam(name = "size", value = "每页条数"),
//    })
//    public IPage<ActivityRedPacketVO> redPacketList(@ApiIgnore PageDTO<ActivityRedPacket> page, ActivityRedPacketQueryDTO activityRedPacketQueryDTO) {
//        return activityRedPacketService.redPacketList(page, activityRedPacketQueryDTO);
//    }
//
//    /**
//     * 新增红包
//     *
//     * @param activityRedPacketAddDTO
//     */
//    @ApiOperation(value = "新增红包")
//    @PostMapping("/add")
//    @PreAuthorize("hasAuthority('activity:redpacket:add')")
//    public void add(@RequestBody ActivityRedPacketAddDTO activityRedPacketAddDTO) {
//        if (activityRedPacketAddDTO.getPacketType() == null || activityRedPacketAddDTO.getPacketType() == 0) {
//            throw new ServiceException("红包类型必须");
//        }
//        if (activityRedPacketAddDTO.getBeginTime() == null || activityRedPacketAddDTO.getEndTime() == null) {
//            throw new ServiceException("红包时间必填");
//        }
//        if (StringUtils.isBlank(activityRedPacketAddDTO.getWeekTime())) {
//            throw new ServiceException("红包时间必填");
//        }
//        if (StringUtils.isBlank(activityRedPacketAddDTO.getDuration())) {
//            throw new ServiceException("红包持续时长必填");
//        }
//        if (StringUtils.isBlank(activityRedPacketAddDTO.getRealTitle())) {
//            throw new ServiceException("红包标题不能为空");
//        }
//        if (StringUtils.isBlank(activityRedPacketAddDTO.getRealLocation())) {
//            throw new ServiceException("红包位置不能为空");
//        }
//        activityRedPacketService.add(activityRedPacketAddDTO);
//    }
//
//    /**
//     * 修改红包
//     *
//     * @param activityRedPacketUpdateDTO
//     */
//    @ApiOperation(value = "修改红包")
//    @PutMapping("/update")
//    @PreAuthorize("hasAuthority('activity:redpacket:edit')")
//    public void edit(@RequestBody ActivityRedPacketUpdateDTO activityRedPacketUpdateDTO) {
//        if (activityRedPacketUpdateDTO.getPacketId() == null || activityRedPacketUpdateDTO.getPacketId() == 0) {
//            throw new ServiceException("红包ID必须");
//        }
//        if (activityRedPacketUpdateDTO.getPacketType() == null || activityRedPacketUpdateDTO.getPacketType() == 0) {
//            throw new ServiceException("红包类型必须");
//        }
//        if (activityRedPacketUpdateDTO.getBeginTime() == null || activityRedPacketUpdateDTO.getEndTime() == null) {
//            throw new ServiceException("红包时间必填");
//        }
//        if (StringUtils.isBlank(activityRedPacketUpdateDTO.getWeekTime())) {
//            throw new ServiceException("红包时间必填");
//        }
//        if (StringUtils.isBlank(activityRedPacketUpdateDTO.getDuration())) {
//            throw new ServiceException("红包持续时长必填");
//        }
//        if (StringUtils.isBlank(activityRedPacketUpdateDTO.getRealTitle())) {
//            throw new ServiceException("红包标题不能为空");
//        }
//        if (StringUtils.isBlank(activityRedPacketUpdateDTO.getRealLocation())) {
//            throw new ServiceException("红包位置不能为空");
//        }
//        activityRedPacketService.edit(activityRedPacketUpdateDTO);
//    }
//
//    /**
//     * 更新红包状态
//     *
//     * @param packetId
//     */
//    @ApiOperation(value = "更新红包状态")
//    @PutMapping("/updateStatus")
//    @PreAuthorize("hasAuthority('activity:redpacket:edit')")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "packetId", value = "红包ID"),
//    })
//    public void updateStatus(@RequestParam Long packetId) {
//        activityRedPacketService.updateStatus(packetId);
//    }
//
//    /**
//     * 更新红包状态
//     *
//     * @param ids
//     */
//    @ApiOperation(value = "更新红包状态")
//    @PutMapping("/delete")
//    @PreAuthorize("hasAuthority('activity:redpacket:remove')")
//    public void delete(@RequestBody String ids) {
//        activityRedPacketService.delete(ids);
//    }
//
//    /**
//     * 查询红包优惠列表
//     *
//     * @param activityRedPacketDiscountDTO
//     * @return
//     */
//    @ApiOperation(value = "查询红包优惠列表")
//    @GetMapping("/discountList")
//    @PreAuthorize("hasAuthority('activity:redpacket:list')")
//    public Object discountList(@RequestBody ActivityRedPacketDiscountDTO activityRedPacketDiscountDTO) {
//        if (activityRedPacketDiscountDTO.getId() == null || activityRedPacketDiscountDTO.getId() == 0) {
//            throw new ServiceException("红包id不能为空");
//        }
//        if (activityRedPacketDiscountDTO.getType() == null || activityRedPacketDiscountDTO.getType() == 0) {
//            throw new ServiceException("类型不能为空");
//        }
//        return activityRedPacketService.discountList(activityRedPacketDiscountDTO);
//    }


}
