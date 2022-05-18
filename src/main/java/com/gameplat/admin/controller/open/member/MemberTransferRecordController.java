package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.vo.MemberTransferRecordVO;
import com.gameplat.admin.service.MemberTransferRecordService;
import com.gameplat.model.entity.member.MemberTransferRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "会员转代理记录")
@RestController
@RequestMapping("/api/admin/member/transfer/record")
public class MemberTransferRecordController {

  @Autowired private MemberTransferRecordService memberTransferRecordService;

  @GetMapping("/list")
  public IPage<MemberTransferRecordVO> list(PageDTO<MemberTransferRecord> page, Integer type) {
    return memberTransferRecordService.queryPage(page, type);
  }

  @Operation(summary = "获取转代理详情")
  @GetMapping("/getContent/{serialNo}")
  public List<String> getContent(@PathVariable String serialNo) {
    return memberTransferRecordService.getContent(serialNo, null, null);
  }
}
