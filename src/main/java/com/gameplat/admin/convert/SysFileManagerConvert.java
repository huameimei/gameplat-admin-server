package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.SysFileManagerVO;
import com.gameplat.model.entity.sys.SysFileManager;
import org.mapstruct.Mapper;

/**
 * @author aBen
 * @date 2022/4/10 0:34
 * @desc
 */
@Mapper(componentModel = "spring")
public interface SysFileManagerConvert {

  SysFileManagerVO toVo(SysFileManager entity);
}
