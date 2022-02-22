package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.MemberBackup;
import com.gameplat.admin.model.vo.MemberBackupVO;
import java.util.List;

public interface MemberBackupService extends IService<MemberBackup> {

  IPage<MemberBackupVO> queryPage(PageDTO<MemberBackup> page, Integer type);

  List<MemberBackup> getBySerialNo(String serialNo);

  List<String> getContent(String serialNo,String endTime,String startTime);
}
