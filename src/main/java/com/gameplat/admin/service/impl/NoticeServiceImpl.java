package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.gameplat.admin.convert.NoticeConvert;
import com.gameplat.admin.mapper.NoticeMapper;
import com.gameplat.admin.model.domain.Notice;
import com.gameplat.admin.model.dto.NoticeAddDTO;
import com.gameplat.admin.model.dto.NoticeEditDTO;
import com.gameplat.admin.model.dto.NoticeQueryDTO;
import com.gameplat.admin.model.dto.NoticeUpdateStatusDTO;
import com.gameplat.admin.model.vo.NoticeVO;
import com.gameplat.admin.service.NoticeService;
import com.gameplat.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author lily
 * @description 公告信息业务处理层
 * @date 2021/11/16
 */

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired private NoticeConvert noticeConvert;

    @Override
    public IPage<NoticeVO> selectNoticeList(IPage<Notice> page, NoticeQueryDTO noticeQueryDTO) {
        LambdaQueryChainWrapper<Notice> queryWrapper =
                this.lambdaQuery()
                    .eq(ObjectUtils.isNotEmpty(noticeQueryDTO.getNoticeType()), Notice::getNoticeType, noticeQueryDTO.getNoticeType())
                    .eq(ObjectUtils.isNotEmpty(noticeQueryDTO.getStatus()), Notice::getStatus, noticeQueryDTO.getStatus())
                 .eq(ObjectUtils.isNotEmpty(noticeQueryDTO.getNoticeTitle()), Notice::getNoticeTitle, noticeQueryDTO.getNoticeTitle());

        return queryWrapper.page(page).convert(noticeConvert::toVo);
    }

    @Override
    public void updateNotice(NoticeEditDTO noticeEditDTO) {
        if (ObjectUtils.isEmpty(noticeEditDTO.getId())){
            throw new ServiceException("id不能为空!");
        }
        Notice notice = noticeConvert.toEntity(noticeEditDTO);
        if (!this.updateById(notice)){
            throw new ServiceException("更新公告信息失败!");
        }
    }

    @Override
    public void deleteNotice(Integer id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new ServiceException("id不能为空!");
        }
        if (!this.removeById(id)){
            throw new ServiceException("删除公告信息失败！");
        }
    }

    @Override
    public void disableStatus(NoticeUpdateStatusDTO noticeUpdateStatusDTO) {
        if (ObjectUtils.isEmpty(noticeUpdateStatusDTO.getId())) {
            throw new ServiceException("id不能为空!");
        }
        Notice notice = noticeConvert.toEntity(noticeUpdateStatusDTO);
        if (!this.updateById(notice)){
            throw new ServiceException("禁用失败！");
        }

    }

    @Override
    public void enableStatus(NoticeUpdateStatusDTO noticeUpdateStatusDTO) {
        if (ObjectUtils.isEmpty(noticeUpdateStatusDTO.getId())) {
            throw new ServiceException("id不能为空!");
        }
        Notice notice = noticeConvert.toEntity(noticeUpdateStatusDTO);
        if (!this.updateById(notice)){
            throw new ServiceException("启用失败！");
        }
    }

    @Override
    public void insertNotice(NoticeAddDTO noticeAddDTO) {
        Notice notice = noticeConvert.toEntity(noticeAddDTO);
        if (!this.save(notice)) {
            throw new ServiceException("添加公告信息失败！");
        }

    }

}
