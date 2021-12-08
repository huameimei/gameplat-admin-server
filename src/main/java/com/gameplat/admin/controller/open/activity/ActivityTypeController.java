package com.gameplat.admin.controller.open.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityType;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.dto.ActivityTypeAddDTO;
import com.gameplat.admin.model.dto.ActivityTypeQueryDTO;
import com.gameplat.admin.model.dto.ActivityTypeUpdateDTO;
import com.gameplat.admin.model.vo.ActivityTypeVO;
import com.gameplat.admin.service.ActivityTypeService;
import com.gameplat.admin.service.SystemConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 活动板块、管理
 *
 * @author kevin
 * @since 2020-08-17
 */
@Slf4j
@Api(tags = "活动板块管理")
@RestController
@RequestMapping("/api/admin/activity/type")
@RequiredArgsConstructor
public class ActivityTypeController {

    @Autowired
    private ActivityTypeService activityTypeService;

    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * 活动板块列表
     *
     * @param page
     * @param activityTypeQueryDTO
     * @param country
     * @return
     */
    @ApiOperation(value = "活动板块列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('activity:type:list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "分页参数：当前页", defaultValue = "1", required = false),
            @ApiImplicitParam(name = "size", value = "每页条数", required = false),
    })
    public IPage<ActivityTypeVO> list(@ApiIgnore PageDTO<ActivityType> page, ActivityTypeQueryDTO activityTypeQueryDTO,
                                      @RequestHeader(value = "country", defaultValue = "zh-CN", required = false) String country) {
        if (StringUtils.isBlank(activityTypeQueryDTO.getLanguage())) {
            activityTypeQueryDTO.setLanguage(country);
        }
        if (StringUtils.isBlank(activityTypeQueryDTO.getLanguage())) {
            throw new ServiceException("语言language参数必传");
        }
        return activityTypeService.list(page, activityTypeQueryDTO);
    }


    /**
     * 新增活动板块
     *
     * @param activityTypeAddDTO
     * @param country
     */
    @ApiOperation(value = "新增活动板块")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('activity:type:add')")
    public void add(@RequestBody ActivityTypeAddDTO activityTypeAddDTO,
                    @RequestHeader(value = "country", defaultValue = "zh-CN", required = false) String country) {
        if (StringUtils.isBlank(activityTypeAddDTO.getLanguage())) {
            activityTypeAddDTO.setLanguage(country);
        }
        if (StringUtils.isBlank(activityTypeAddDTO.getTypeName())) {
            throw new ServiceException("活动板块名称不能为空");
        }
        if (activityTypeAddDTO.getSort() == null || activityTypeAddDTO.getSort() == 0) {
            throw new ServiceException("排序不能为空");
        }
        if (activityTypeAddDTO.getFloatStatus() != null && activityTypeAddDTO.getFloatStatus() != 0) {
            if (StringUtils.isBlank(activityTypeAddDTO.getFloatLogo())) {
                throw new ServiceException("开启浮窗开关，浮窗图片不能为空");
            }
        }
        if (StringUtils.isBlank(activityTypeAddDTO.getLanguage())) {
            throw new ServiceException("语言不能为空");
        }
        activityTypeService.add(activityTypeAddDTO);
    }

    /**
     * 更新活动板块
     *
     * @param activityTypeUpdateDTO
     * @param country
     */
    @ApiOperation(value = "更新活动板块")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('activity:type:edit')")
    public void update(@RequestBody ActivityTypeUpdateDTO activityTypeUpdateDTO,
                       @RequestHeader(value = "country", defaultValue = "zh-CN", required = false) String country) {
        if (activityTypeUpdateDTO.getId() == null || activityTypeUpdateDTO.getId() == 0) {
            throw new ServiceException("更新活动板块，ID不能为空");
        }
        if (StringUtils.isBlank(activityTypeUpdateDTO.getLanguage())) {
            activityTypeUpdateDTO.setLanguage(country);
        }
        if (activityTypeUpdateDTO.getSort() == null || activityTypeUpdateDTO.getSort() == 0) {
            throw new ServiceException("排序不能为空");
        }
        if (activityTypeUpdateDTO.getFloatStatus() != null && activityTypeUpdateDTO.getFloatStatus() != 0) {
            if (StringUtils.isBlank(activityTypeUpdateDTO.getFloatLogo())) {
                throw new ServiceException("开启浮窗开关，浮窗图片不能为空");
            }
        }
        if (StringUtils.isBlank(activityTypeUpdateDTO.getLanguage())) {
            throw new ServiceException("语言language不能为空");
        }
        activityTypeService.update(activityTypeUpdateDTO);
    }

    /**
     * 删除活动板块
     *
     * @param ids
     */
    @ApiOperation(value = "删除活动板块")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('activity:type:remove')")
    public void remove(@RequestBody String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException("ids不能为空");
        }
        activityTypeService.remove(ids);
    }

    /**
     * 类型编码列表
     *
     * @param language
     * @param country
     * @return
     */
    @ApiOperation(value = "类型编码列表")
    @GetMapping("/typeCodeList")
    @PreAuthorize("hasAuthority('activity:type:list')")
    @ApiImplicitParams({@ApiImplicitParam(name = "language", value = "语言", required = true)})
    public JSONArray typeCodeList(String language, @RequestHeader(value = "country", defaultValue = "zh-CN", required = false) String country) {
        if (StringUtils.isBlank(language)) {
            language = country;
        }
        if (StringUtils.isBlank(language)) {
            throw new ServiceException("语言language参数不能为空");
        }
        SysDictData sysDictData = systemConfigService.findActivityTypeCodeList(language);
        //mock数据
        String jsonStr = "{\"zh-CN\":[{\"sport\":\"体育\"},{\"lottery\":\"彩票\"},{\"chess\":\"棋牌\"},{\"real\":\"真人\"},{\"egame\":\"电子\"},{\"esport\":\"电竞\"},{\"hunter\":\"捕鱼\"},{\"asport\":\"动物竞技\"},{\"other\":\"其它\"}],\"en-US\":[{\"sport\":\"Sports\"},{\"lottery\":\"Lottery\"},{\"chess\":\"Cards\"},{\"real\":\"Live\"},{\"egame\":\"Slot\"},{\"esport\":\"Esports\"},{\"hunter\":\"Animal\"},{\"asport\":\"Fishing\"},{\"other\":\"Other\"}]}";
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        return jsonObject.getJSONArray(language);
    }

}
