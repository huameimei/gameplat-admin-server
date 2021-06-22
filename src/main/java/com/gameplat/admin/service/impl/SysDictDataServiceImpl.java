package com.gameplat.admin.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.dao.SysDictDataMapper;
import com.gameplat.admin.model.entity.SysDictData;
import com.gameplat.admin.service.SysDictDataService;
import java.util.List;
import jodd.util.StringUtil;
import org.springframework.stereotype.Service;

/**
 * @author Lenovo
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper,SysDictData> implements SysDictDataService {

  @Override
  public List<SysDictData> selectDictDataListByType(String dictType) {
    return this.lambdaQuery().eq(SysDictData::getDictType,dictType).list();
  }

  @Override
  public <T> T getDictData(String dictType, Class<T> t) {
    return (T) JSONUtil.toBean(JSONUtil.toJsonStr(this.selectDictDataListByType(dictType)), t);
  }

  @Override
  public SysDictData getDictValue(String dictType, String dictLabel) {
    return this.lambdaQuery().eq(SysDictData::getDictType,dictType)
        .eq(SysDictData::getDictLabel,dictLabel).one();
  }

}
