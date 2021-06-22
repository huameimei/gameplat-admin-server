package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.dao.MemberInfoMapper;
import com.gameplat.admin.model.entity.MemberInfo;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.admin.utils.TreeUtils;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;

/**
 * @author Lenovo
 */
@Service
@RequiredArgsConstructor
public class MemberInfoServiceImpl extends ServiceImpl<MemberInfoMapper, MemberInfo>  implements MemberInfoService{

  private final MemberInfoMapper memberInfoMapper;

  /**
   * 根据指定的id，获取分类的全部属性。
   *
   * @param id 分类id
   * @return 分类的实体对象
   * @throws IllegalArgumentException 如果id不是正数
   */
  @Override
  public MemberInfo findById(Long id) {
    TreeUtils.checkNotNegative(id, "id");
    return memberInfoMapper.selectAttributes(id);
  }

  /**
   * 获取所有分类的数量。
   *
   * @return 数量
   */
  @Override
  public int count() {
    return this.lambdaQuery().count() - 1;
  }

  /**
   * 获取某一级分类的数量，参数从1开始，表示第一级分类（根分类的子类）。
   *
   * @param layer 层级（从1开始）
   * @return 数量
   * @throws IllegalArgumentException 如果layer不是正数
   */
  @Override
  public int countOfLayer(int layer) {
    TreeUtils.checkPositive(layer, "layer");
    return memberInfoMapper.selectCountByLayer(layer);
  }

  /**
   * 新增一个分类，其ID属性将自动生成或计算，并返回。 新增分类的继承关系由parent属性指定，parent为0表示该分类为一级分类。
   *
   * @param memberInfo 分类实体对象
   * @param parent 上级分类id
   * @throws IllegalArgumentException 如果parent所指定的分类不存在、category为null或category中存在属性为null
   */
  @Override
  public Long add(MemberInfo memberInfo, Long parent) {
    TreeUtils.checkNotNegative(parent, "parent");
    if (parent > 0 && memberInfoMapper.contains(parent) == null) {
      throw new IllegalArgumentException("指定的上级分类不存在");
    }
    try {
      memberInfoMapper.insert(memberInfo);
      memberInfoMapper.insertPath(memberInfo.getId(), parent);
      memberInfoMapper.insertNode(memberInfo.getId());
    } catch (PersistenceException ex) {
      throw new IllegalArgumentException(ex);
    }
    return memberInfo.getId();
  }

  /**
   * 该方法仅更新分类的属性，不修改继承关系，若要移动节点请使用 <code>Category.moveTo()</code>和<code>Category.moveTreeTo()</code>
   *
   * @param memberInfo 新的对象
   */
  @Override
  public void update(MemberInfo memberInfo) {
    try {
      TreeUtils.checkEffective(memberInfoMapper.updateById(memberInfo));
    } catch (PersistenceException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  /**
   * 删除一个分类，原来在该分类下的子分类将被移动到该分类的父分类中， 如果此分类是一级分类，则删除后子分类将全部成为一级分类。
   *
   * <p>顶级分类不可删除。
   *
   * @param id 要删除的分类的id
   * @throws IllegalArgumentException 如果指定id的分类不存在
   */
  @Override
  public void delete(Long id) {
    TreeUtils.checkPositive(id, "id");
    if (memberInfoMapper.contains(id) == null) {
      throw new IllegalArgumentException("指定的分类不存在");
    }
    Long parent = memberInfoMapper.selectAncestor(id, 1);
    if (parent == null) {
      parent = 0L;
    }
    findById(id).moveSubTree(parent);
    deleteBoth(id);
  }

  /**
   * 删除一个分类及其所有的下级分类。
   *
   * <p>顶级分类不可删除。
   *
   * @param id 要删除的分类的id
   * @throws IllegalArgumentException 如果指定id的分类不存在
   */
  @Override
  public void deleteTree(Long id) {
    TreeUtils.checkPositive(id, "id");
    if (memberInfoMapper.contains(id) == null) {
      throw new IllegalArgumentException("指定的分类不存在");
    }
    deleteBoth(id);
    for (Long des : memberInfoMapper.selectDescendantId(id)) {
      deleteBoth(des);
    }
  }

  /**
   * 删除一个分类，两个表中的相关记录都删除
   *
   * @param id 分类id
   */
  @Override
  public void deleteBoth(Long id) {
    TreeUtils.checkEffective(memberInfoMapper.deleteById(id));
    memberInfoMapper.deletePath(id);
  }
}
