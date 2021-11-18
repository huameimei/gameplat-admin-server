package com.gameplat.admin.controller.open.notice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.PushMessage;
import com.gameplat.admin.model.dto.PushMessageAddDTO;
import com.gameplat.admin.model.dto.PushMessageDTO;
import com.gameplat.admin.model.dto.PushMessageRemoveDTO;
import com.gameplat.admin.model.vo.PushMessageVO;
import com.gameplat.admin.service.PushMessageService;
import com.gameplat.common.context.GlobalContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author kenvin
 * @description 个人消息
 * @date 2021/11/16
 */

@Slf4j
@RestController
@RequestMapping("/api/admin/notice/pushmessage")
public class OpenPushMessageController {

    @Autowired
    private PushMessageService pushMessageService;

    /**
     * 查询
     *
     * @param page
     * @param pushMessageDTO
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('notice:pushmessage:list')")
    public IPage<PushMessageVO> findPushMessageList(PageDTO<PushMessage> page, PushMessageDTO pushMessageDTO) {
        return pushMessageService.findPushMessageList(page, pushMessageDTO);
    }

    /**
     * 新增
     *
     * @param pushMessageAddDTO
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('notice:pushmessage:add')")
    public void add(@RequestBody PushMessageAddDTO pushMessageAddDTO) {
        pushMessageAddDTO.setCreateBy(GlobalContextHolder.getContext().getUsername());
        pushMessageService.insertPushMessage(pushMessageAddDTO);
    }

    /**
     * 单个删除
     *
     * @param id
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('notice:pushmessage:remove')")
    public void remove(@PathVariable("id") Long id) {
        pushMessageService.deletePushMessage(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @DeleteMapping("/deleteBatch")
    @PreAuthorize("hasAuthority('notice:pushmessage:remove')")
    public void remove(@RequestBody Long[] ids) {
        pushMessageService.deleteBatchPushMessage(ids);
    }

    /**
     * 按条件删除
     *
     * @param pushMessageRemoveDTO
     */
    @DeleteMapping("/deleteByCondition")
    @PreAuthorize("hasAuthority('notice:pushmessage:remove')")
    public void removeByCondition(@RequestBody PushMessageRemoveDTO pushMessageRemoveDTO) {
        pushMessageService.deleteByCondition(pushMessageRemoveDTO);
    }


}
