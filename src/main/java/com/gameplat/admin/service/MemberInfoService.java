package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.MemberInfoAddDTO;
import com.gameplat.admin.model.dto.MemberInfoEditDTO;
import com.gameplat.admin.model.dto.MemberInfoQueryDTO;
import com.gameplat.admin.model.entity.MemberInfo;
import com.gameplat.admin.model.entity.SysUser;
import com.gameplat.admin.model.vo.MemberInfoVO;

public interface MemberInfoService extends IService<MemberInfo> {

  /**
   * 根据指定的id，获取分类的全部属性。
   *
   * @param id 分类id
   * @return 分类的实体对象
   * @throws IllegalArgumentException 如果id不是正数
   */
  MemberInfo findById(Long id);

  /**
   * 获取所有分类的数量。
   *
   * @return 数量
   */
  int count();

  /**
   * 新增一个分类，其ID属性将自动生成或计算，并返回。 新增分类的继承关系由parent属性指定，parent为0表示该分类为一级分类。
   *
   * @param memberInfo 分类实体对象
   * @param parent 上级分类id
   * @throws IllegalArgumentException 如果parent所指定的分类不存在、category为null或category中存在属性为null
   */
  Long add(MemberInfo memberInfo, Long parent);

  /**
   * 该方法仅更新分类的属性，不修改继承关系，若要移动节点请使用 <code>Category.moveTo()</code>和<code>Category.moveTreeTo()</code>
   *
   * @param memberInfo 新的对象
   */
  void update(MemberInfo memberInfo);

  /**
   * 删除一个分类，原来在该分类下的子分类将被移动到该分类的父分类中， 如果此分类是一级分类，则删除后子分类将全部成为一级分类。
   *
   * <p>顶级分类不可删除。
   *
   * @param id 要删除的分类的id
   * @throws IllegalArgumentException 如果指定id的分类不存在
   */
  void delete(Long id);

  /**
   * 删除一个分类及其所有的下级分类。
   *
   * <p>顶级分类不可删除。
   *
   * @param id 要删除的分类的id
   * @throws IllegalArgumentException 如果指定id的分类不存在
   */
  void deleteTree(Long id);

  /**
   * 删除一个分类，两个表中的相关记录都删除
   *
   * @param id 分类id
   */
  void deleteBoth(Long id);

  IPage<MemberInfoVO> queryPage(
      IPage<MemberInfo> page, MemberInfoQueryDTO memberInfoQueryDto, SysUser sysUser);

  void save(MemberInfoAddDTO memberInfoAddDto);

  void update(MemberInfoEditDTO memberInfoEditDto);
}
