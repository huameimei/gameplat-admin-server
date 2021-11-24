package com.gameplat.admin.controller.open.activity;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityLobby;
import com.gameplat.admin.model.dto.ActivityLobbyAddDTO;
import com.gameplat.admin.model.dto.ActivityLobbyDTO;
import com.gameplat.admin.model.vo.ActivityLobbyVO;
import com.gameplat.admin.service.ActivityLobbyService;
import com.gameplat.common.context.GlobalContextHolder;
import com.gameplat.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * 用户活动大厅
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
    public void add(@RequestBody ActivityLobbyAddDTO activityLobbyAddDTO, BindingResult results) {
        if (results.hasErrors()) {
            throw new ServiceException(results.getFieldError().getDefaultMessage());
        }
        activityLobbyAddDTO.setCreateBy(GlobalContextHolder.getContext().getUsername());
        activityLobbyService.add(activityLobbyAddDTO);
    }


}
