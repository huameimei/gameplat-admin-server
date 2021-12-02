package com.gameplat.admin.controller.open.activity;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityLobby;
import com.gameplat.admin.model.dto.ActivityLobbyAddDTO;
import com.gameplat.admin.model.dto.ActivityLobbyDTO;
import com.gameplat.admin.model.dto.ActivityLobbyUpdateDTO;
import com.gameplat.admin.model.dto.ActivityLobbyUpdateStatusDTO;
import com.gameplat.admin.model.vo.ActivityLobbyVO;
import com.gameplat.admin.service.ActivityLobbyService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户活动大厅
 *
 * @author kenvin
 */
@Api(tags = "活动大厅管理")
@Slf4j
@RestController
@RequestMapping("/api/admin/activity/lobby")
public class ActivityLobbyController {

    @Autowired
    private ActivityLobbyService activityLobbyService;

    /**
     * 活动大厅列表
     */
    @ApiOperation(value = "列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('activity:lobby:list')")
    public IPage<ActivityLobbyVO> list(PageDTO<ActivityLobby> page, ActivityLobbyDTO activityLobbyDTO) {
        return activityLobbyService.findActivityLobbyList(page, activityLobbyDTO);
    }

    /**
     * 新增
     *
     * @param activityLobbyAddDTO
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('activity:lobby:add')")
    public void add(@RequestBody ActivityLobbyAddDTO activityLobbyAddDTO) {
        if (StringUtils.isNull(activityLobbyAddDTO.getStatisDate())) {
            throw new ServiceException("请选择统计日期");
        }
        if (activityLobbyAddDTO.getApplyWay() == 2 && activityLobbyAddDTO.getNextDayApply() == 0) {
            throw new ServiceException("自动申请的活动必须勾选隔天申请");
        }
        activityLobbyService.add(activityLobbyAddDTO);
    }

    /**
     * 修改
     *
     * @param activityLobbyUpdateDTO
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('activity:lobby:edit')")
    public void update(@RequestBody ActivityLobbyUpdateDTO activityLobbyUpdateDTO) {
        if (activityLobbyUpdateDTO.getId() == null || activityLobbyUpdateDTO.getId() == 0) {
            throw new ServiceException("id不能为空");
        }
        if (StringUtils.isNull(activityLobbyUpdateDTO.getStatisDate())) {
            throw new ServiceException("请选择统计日期");
        }
        if (activityLobbyUpdateDTO.getApplyWay() == 2 && activityLobbyUpdateDTO.getNextDayApply() == 0) {
            throw new ServiceException("自动申请的活动必须勾选隔天申请");
        }
        activityLobbyService.update(activityLobbyUpdateDTO);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('activity:lobby:remove')")
    public void remove(@RequestBody String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException("ids不能为空");
        }
        activityLobbyService.remove(ids);
    }


    /**
     * 更新状态
     *
     * @param activityLobbyUpdateStatusDTO
     */
    @PutMapping("/updateStatus")
    @PreAuthorize("hasAuthority('activity:lobby:edit')")
    public void updateStatus(@RequestBody ActivityLobbyUpdateStatusDTO activityLobbyUpdateStatusDTO) {
        if (activityLobbyUpdateStatusDTO.getId() == null || activityLobbyUpdateStatusDTO.getId() == 0) {
            throw new ServiceException("id不能为空");
        }
        activityLobbyService.updateStatus(activityLobbyUpdateStatusDTO);
    }

    /**
     * 查询未绑定的活动大厅列表
     */
    @ApiOperation(value = "查询未绑定的活动大厅列表")
    @GetMapping("/findUnboundLobbyList")
    @PreAuthorize("hasAuthority('activity:lobby:list')")
    public List<ActivityLobbyVO> findUnboundLobbyList() {
        return activityLobbyService.findUnboundLobbyList();
    }

}
