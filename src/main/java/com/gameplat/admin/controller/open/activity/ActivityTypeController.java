package com.gameplat.admin.controller.open.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.ActivityTypeAddDTO;
import com.gameplat.admin.model.dto.ActivityTypeQueryDTO;
import com.gameplat.admin.model.dto.ActivityTypeUpdateDTO;
import com.gameplat.admin.model.vo.ActivityTypeVO;
import com.gameplat.admin.model.vo.CodeDataVO;
import com.gameplat.admin.service.ActivityTypeService;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.model.entity.activity.ActivityType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

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
public class ActivityTypeController {

    @Autowired
    private ActivityTypeService activityTypeService;

    @Autowired
    private ConfigService configService;

    /**
     * 活动板块列表
     *
     * @param page
     * @param activityTypeQueryDTO
     * @return
     */
    @ApiOperation(value = "活动板块列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('activity:type:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "分页参数：当前页", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页条数"),
    })
    public IPage<ActivityTypeVO> list(
            @ApiIgnore PageDTO<ActivityType> page,
            ActivityTypeQueryDTO activityTypeQueryDTO) {
        if (StringUtils.isBlank(activityTypeQueryDTO.getLanguage())) {
            activityTypeQueryDTO.setLanguage(LocaleContextHolder.getLocale().toLanguageTag());
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
     */
    @ApiOperation(value = "新增活动板块")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('activity:type:add')")
    public void add(
            @Validated @RequestBody ActivityTypeAddDTO activityTypeAddDTO) {
        activityTypeAddDTO.setLanguage(LocaleContextHolder.getLocale().toLanguageTag());
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
     */
    @ApiOperation(value = "更新活动板块")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('activity:type:update')")
    public void update(
            @Validated @RequestBody ActivityTypeUpdateDTO activityTypeUpdateDTO) {
        activityTypeUpdateDTO.setLanguage(LocaleContextHolder.getLocale().toLanguageTag());
        if (activityTypeUpdateDTO.getFloatStatus() != null
                && activityTypeUpdateDTO.getFloatStatus() != BooleanEnum.NO.value()) {
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
     * @return
     */
    @ApiOperation(value = "类型编码列表")
    @GetMapping("/typeCodeList")
    @PreAuthorize("hasAuthority('activity:type:typeCodeList')")
    @ApiImplicitParams({@ApiImplicitParam(name = "language", value = "语言", required = true)})
    public List<CodeDataVO> typeCodeList(String language) {
        if (StringUtils.isBlank(language)) {
            language = LocaleContextHolder.getLocale().toLanguageTag();
        }
        if (StringUtils.isBlank(language)) {
            throw new ServiceException("语言language参数不能为空");
        }
        String activityTypeConfig = configService.getValue(DictDataEnum.ACTIVITY_TYPE_CONFIG);
        if (StringUtils.isEmpty(activityTypeConfig)) {
            throw new ServiceException("活动板块类型配置信息不存在");
        }

        JSONObject jsonObject = JSONObject.parseObject(activityTypeConfig);
        JSONArray jsonArray = jsonObject.getJSONArray(language);
        if (CollectionUtils.isEmpty(jsonArray)) {
            throw new ServiceException("语言【" + language + "】活动板块类型配置信息不存在");
        }
        List<CodeDataVO> codeDataVOList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            CodeDataVO codeDataVO = jsonArray.getObject(i, CodeDataVO.class);
            codeDataVOList.add(codeDataVO);
        }
        return codeDataVOList;
    }

    /**
     * 活动板块查询所有列表
     *
     * @param language
     * @return
     */
    @ApiOperation(value = "活动板块查询所有列表")
    @GetMapping("/listAll")
    @PreAuthorize("hasAuthority('activity:type:listAll')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "language", value = "语言"),
    })
    public List<ActivityTypeVO> listAll(
            @RequestParam(value = "language", required = false) String language) {
        if (StringUtils.isBlank(language)) {
            language = LocaleContextHolder.getLocale().toLanguageTag();
        }
        if (StringUtils.isBlank(language)) {
            throw new ServiceException("语言language参数必传");
        }
        return activityTypeService.listAll(language);
    }
}
