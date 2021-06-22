package com.gameplat.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.entity.SysDictData;
import java.util.List;
import org.springframework.stereotype.Service;

public interface SysDictDataService extends IService<SysDictData> {

  List<SysDictData> selectDictDataListByType(String dictType);

  <T> T getDictData(String dictType, Class<T> t);

  SysDictData getDictValue(String dictType, String dictLabel);
}
