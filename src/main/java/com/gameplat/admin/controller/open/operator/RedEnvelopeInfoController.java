package com.gameplat.admin.controller.open.operator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.RedEnvelopeConfigDTO;
import com.gameplat.admin.model.dto.UserRedEnvelopeDTO;
import com.gameplat.admin.model.vo.UserRedEnvelopeVO;
import com.gameplat.admin.service.RedEnvelopeConfigService;
import com.gameplat.admin.service.UserRedEnvelopeService;
import com.gameplat.model.entity.recharge.RedEnvelopeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/operator/red")
public class RedEnvelopeInfoController {

    @Autowired
    private UserRedEnvelopeService userRedEnvelopeService;

    @Autowired
    private RedEnvelopeConfigService redEnvelopeService;

    /**
     * 新增红包配置
     */
    @PostMapping("redAdd")
    @PreAuthorize("hasAuthority('operator:red:add')")
    public Object redAdd(@RequestBody RedEnvelopeConfigDTO dto) {
        return redEnvelopeService.redAdd(dto);
    }

    /**
     * 红包配置列表
     */
    @GetMapping("/redList")
    @PreAuthorize("hasAuthority('operator:red:view')")
    public Object redList(PageDTO<RedEnvelopeConfig> page, RedEnvelopeConfigDTO dto) {
        return redEnvelopeService.redList(page, dto);
    }

    /**
     * 红包设置
     */
    @PostMapping("/redEdit")
    @PreAuthorize("hasAuthority('operator:red:edit')")
    public Object redEdit(@RequestBody RedEnvelopeConfigDTO dto) {
        return redEnvelopeService.redEdit(dto);
    }

    /**
     * 红包删除
     */
    @PostMapping("/redDelete")
    @PreAuthorize("hasAuthority('operator:red:remove')")
    public Object redDelete(@RequestBody List<Integer> ids) {
        return redEnvelopeService.redDelete(ids);
    }

    /**
     * 红包记录
     */
    @GetMapping("/recordList")
    @PreAuthorize("hasAuthority('operator:red:view')")
    public IPage<UserRedEnvelopeVO> recordList(UserRedEnvelopeDTO dto) {
        return userRedEnvelopeService.recordList(dto);
    }

    /**
     * 红包回收
     */
    @PostMapping("/redRecycle")
    @PreAuthorize("hasAuthority('operator:red:reply')")
    public Object redRecycle() {
        return null;
    }
}
