package com.gameplat.admin.controller.open.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityDistribute;
import com.gameplat.admin.model.domain.ActivityInfo;
import com.gameplat.admin.model.dto.ActivityDistributeDTO;
import com.gameplat.admin.model.dto.ActivityInfoDTO;
import com.gameplat.admin.model.dto.ActivityQualificationUpdateStatusDTO;
import com.gameplat.admin.model.vo.ActivityDistributeVO;
import com.gameplat.admin.model.vo.ActivityInfoVO;
import com.gameplat.admin.service.ActivityDistributeService;
import com.gameplat.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author admin
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/activity/distribute")
@Api(tags = "活动分发管理")
public class ActivityDistributeController {

    @Autowired
    private ActivityDistributeService activityDistributeService;

    /**
     * 活动列表
     */
    @ApiOperation(value = "活动分发列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('activity:distribute:list')")
    public IPage<ActivityDistributeVO> list(PageDTO<ActivityDistribute> page, ActivityDistributeDTO activityDistributeDTO) {
        return activityDistributeService.list(page, activityDistributeDTO);
    }

    /**
     * 修改结算状态
     *
     * @param ids
     */
    @ApiOperation(value = "修改结算状态")
    @PutMapping("/updateStatus")
    @PreAuthorize("hasAuthority('activity:distribute:edit')")
    public void updateStatus(@RequestBody String ids) {
        activityDistributeService.updateStatus(ids);
    }


}
