package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.SysFileManagerQueryDTO;
import com.gameplat.admin.model.vo.SysFileManagerVO;
import com.gameplat.model.entity.sys.SysFileManager;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author aBen
 * @date 2022/4/9 23:51
 * @desc
 */
public interface SysFileManagerService {

  IPage<SysFileManagerVO> list(PageDTO<SysFileManager> page, SysFileManagerQueryDTO queryDTO);

  String upload(MultipartFile file);

}
