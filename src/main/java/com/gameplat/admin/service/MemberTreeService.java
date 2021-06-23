package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.entity.MemberTree;

public interface MemberTreeService extends IService<MemberTree> {

  /**
   * 获取某一级分类的数量，参数从1开始，表示第一级分类（根分类的子类）。
   *
   * @param layer 层级（从1开始）
   * @return 数量
   * @throws IllegalArgumentException 如果layer不是正数
   */
  int countOfLayer(int layer) ;

}
