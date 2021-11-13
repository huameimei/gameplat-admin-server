package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.MemberTree;

import java.util.List;

public interface MemberTreeMapper extends BaseMapper<MemberTree> {

  /**
   * 获取某个节点所有子节点，不包括本身
   *
   * @param ancestor 祖先节点
   * @return 子节点id
   */
  List<MemberTree> getAllDescendant(Long ancestor);

  /**
   * 查找某节点下的所有直属子节点<br>
   * 该方法仅返回直属子节点，如果需要获取全部子节点，则应该使用getAllDescendant方法
   *
   * @param ancestor 分类id
   * @return 子类id数组
   */
  List<MemberTree> getDescendant(Long ancestor);

  /**
   * 查询某个节点的第N级父节点
   *
   * @param id 节点id
   * @param n 距离（第N级）
   * @return 父节点id，如果不存在则返回null
   */
  MemberTree getAnyAncestor(Long id, int n);

  /**
   * 查询某个节点的所有父节点
   *
   * @param id 节点id
   * @return 所有父节点
   */
  List<MemberTree> getAllAncestor(Long id);

  /**
   * 查询某节点到它某个祖先节点的距离
   *
   * @param ancestor 父节点id
   * @param id 节点id
   * @return 距离,如果ancestor并不是其祖先节点则返回null
   */
  Integer getDistance(Long ancestor, Long id);

  /**
   * 复制父节点的路径结构,并修改descendant和distance
   *
   * @param id 节点id
   * @param ancestor 父节点id
   */
  void add(Long id, Long ancestor);

  /**
   * 在关系表中插入对自身的连接
   *
   * @param id 节点id
   */
  void addNode(Long id);

  /**
   * 从树中删除某节点的路径。
   *
   * @param id 节点id
   */
  void delete(Long id);
}
