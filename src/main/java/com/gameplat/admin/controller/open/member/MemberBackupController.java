package com.gameplat.admin.controller.open.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.MemberBackup;
import com.gameplat.admin.model.vo.MemberBackupVO;
import com.gameplat.admin.service.MemberBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/member/backup")
public class MemberBackupController {

  @Autowired private MemberBackupService memberBackupService;

  @GetMapping("/list")
  public IPage<MemberBackupVO> list(PageDTO<MemberBackup> page, Integer type) {
    return memberBackupService.queryPage(page, type);
  }

  @GetMapping("/getContent/{serialNo}")
  public List<String> getContent(@PathVariable String serialNo) {
    return memberBackupService.getContent(serialNo);
  }
}
