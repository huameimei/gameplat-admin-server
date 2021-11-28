package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityInfo;
import com.gameplat.admin.model.dto.ActivityInfoAddDTO;
import com.gameplat.admin.model.dto.ActivityInfoDTO;
import com.gameplat.admin.model.vo.ActivityInfoVO;

import java.util.List;

/**
 * 活动业务类
 */
public interface ActivityInfoService {


    /**
     * 列表查询
     *
     * @param page
     * @param activityInfoDTO
     * @return
     */
    IPage<ActivityInfoVO> list(PageDTO<ActivityInfo> page, ActivityInfoDTO activityInfoDTO);

    /**
     * 查询活动详情
     *
     * @param id
     * @return
     */
    ActivityInfoVO detail(Long id);

    /**
     * 新增活动
     *
     * @param activityInfoAddDTO
     * @param country
     * @return
     */
    void add(ActivityInfoAddDTO activityInfoAddDTO, String country);

    /**
     * 检查活动是否满足条件
     *
     * @param activityLobbyId
     * @param id
     */
    void checkActivityLobbyId(Long activityLobbyId, Integer id);

    /**
     * 保存活动信息
     *
     * @param activityInfo
     * @return
     */
    boolean saveActivityInfo(ActivityInfo activityInfo);

    /**
     * 查询关联规则的活动
     *
     * @return
     */
    List<ActivityInfoVO> getAllSysActivityWithRule();

    /**
     * 通过条件查询列表数据
     *
     * @param activityInfo
     * @return
     */
    List<ActivityInfo> list(ActivityInfo activityInfo);

    /**
     * 通过活动类型查询活动列表
     *
     * @param id
     * @return
     */
    List<ActivityInfo> findByTypeId(Long id);
}
