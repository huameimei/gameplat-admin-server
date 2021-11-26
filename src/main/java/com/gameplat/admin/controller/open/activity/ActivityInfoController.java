package com.gameplat.admin.controller.open.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityInfo;
import com.gameplat.admin.model.dto.ActivityInfoAddDTO;
import com.gameplat.admin.model.dto.ActivityInfoDTO;
import com.gameplat.admin.model.vo.ActivityInfoVO;
import com.gameplat.admin.service.ActivityInfoService;
import com.gameplat.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动管理
 *
 * @author kenvin
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/activity/info")
@Api(tags = "活动管理")
public class ActivityInfoController {

    @Autowired
    private ActivityInfoService activityInfoService;

    /**
     * 活动列表
     */
    @ApiOperation(value = "活动列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('activity:info:list')")
    public IPage<ActivityInfoVO> list(PageDTO<ActivityInfo> page, ActivityInfoDTO activityInfoDTO) {
        return activityInfoService.list(page, activityInfoDTO);
    }

    @ApiOperation(value = "查询活动详情")
    @GetMapping("/detail")
    public ActivityInfoVO detail(Long id) {
        return activityInfoService.detail(id);
    }

    @ApiOperation(value = "新增活动")
    @PostMapping("/add")
    public void add(@RequestBody ActivityInfoAddDTO activityInfoAddDTO,
                    @RequestHeader(value = "country", required = false, defaultValue = "zh-CN") String country) {
        if (StringUtils.isNotNull(activityInfoAddDTO.getActivityLobbyId())) {
            activityInfoService.checkActivityLobbyId(activityInfoAddDTO.getActivityLobbyId(), activityInfoAddDTO.getId());
        }
        activityInfoService.add(activityInfoAddDTO, country);
    }

    @ApiOperation(value = "获取关联了活动规则的全部活动信息")
    @GetMapping("/getAllSysActivityWithRule")
    public List<ActivityInfoVO> getAllSysActivityWithRule() {
        List<ActivityInfoVO> activityList = activityInfoService.getAllSysActivityWithRule();
        if (CollectionUtils.isEmpty(activityList)) {
            return new ArrayList<>();
        }
        ArrayList<ActivityInfoVO> result = new ArrayList<>();
        long nowTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        activityList.forEach(o -> {
//            if (o.getValidStatus() == 1) {
//                result.add(o);
//            } else if (o.getValidStatus() == 2) {
//                String beginDate = o.getBeginTime().concat(" 00:00:00");
//                String endDate = o.getEndTime().concat(" 23:59:59");
//                try {
//                    if (nowTime > sdf.parse(beginDate).getTime() && nowTime < sdf.parse(endDate).getTime()) {
//                        result.add(o);
//                    }
//                } catch (ParseException e) {
//                    log.info("获取关联了活动规则的全部活动信息,时间转换报错，原因{}", e);
//                }
//            }
//        });
        return result;
    }

}
