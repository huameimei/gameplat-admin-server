package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysFileManagerConvert;
import com.gameplat.admin.mapper.SysFileManagerMapper;
import com.gameplat.admin.model.dto.SysFileManagerQueryDTO;
import com.gameplat.admin.model.vo.SysFileManagerVO;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.OssService;
import com.gameplat.admin.service.SysFileManagerService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.compent.oss.config.FileConfig;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.model.entity.sys.SysFileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author aBen
 * @date 2022/4/9 23:51
 * @desc
 */
@Service
public class SysFileManagerServiceImpl extends ServiceImpl<SysFileManagerMapper, SysFileManager>
        implements SysFileManagerService {

  @Autowired
  private SysFileManagerConvert sysFileManagerConvert;

  @Autowired
  private ConfigService configService;

  @Autowired
  private OssService ossService;


  @Override
  public IPage<SysFileManagerVO> list(PageDTO<SysFileManager> page, SysFileManagerQueryDTO queryDTO) {
    return this.lambdaQuery()
            .eq(StringUtils.isNotEmpty(queryDTO.getOldFileName()), SysFileManager::getOldFileName, queryDTO.getOldFileName())
            .eq(StringUtils.isNotEmpty(queryDTO.getStoreFileName()), SysFileManager::getStoreFileName, queryDTO.getStoreFileName())
            .eq(StringUtils.isNotEmpty(queryDTO.getCreateBy()), SysFileManager::getCreateBy, queryDTO.getCreateBy())
            .eq(StringUtils.isNotEmpty(queryDTO.getFileType()), SysFileManager::getFileType, queryDTO.getFileType())
            .ge(StringUtils.isNotEmpty(queryDTO.getStartTime()), SysFileManager::getCreateTime, queryDTO.getStartTime())
            .le(StringUtils.isNotEmpty(queryDTO.getEndTime()), SysFileManager::getCreateTime, queryDTO.getEndTime())
            .page(page).convert(sysFileManagerConvert::toVo);
  }

  @Override
  public String upload(MultipartFile file) {
    // 上传图片
    FileConfig fileConfig = configService.getDefaultConfig(DictTypeEnum.FILE_CONFIG, FileConfig.class);
    if (StringUtils.isNull(fileConfig)) {
      throw new ServiceException("未找到文件配置");
    }

    return ossService.upload(file);
  }
}
