package com.gameplat.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberInfoConvert;
import com.gameplat.admin.dao.MemberInfoMapper;
import com.gameplat.admin.dao.MemberTreeMapper;
import com.gameplat.admin.model.dto.MemberInfoAddDTO;
import com.gameplat.admin.model.dto.MemberInfoEditDTO;
import com.gameplat.admin.model.dto.MemberInfoQueryDTO;
import com.gameplat.admin.model.entity.MemberInfo;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.service.MemberInfoService;
import com.gameplat.admin.utils.TreeUtils;
import com.gameplat.common.exception.ServiceException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** @author Lenovo */
@Service
public class MemberInfoServiceImpl extends ServiceImpl<MemberInfoMapper, MemberInfo>
    implements MemberInfoService {

  @Autowired private MemberInfoMapper memberInfoMapper;

  @Autowired private MemberTreeMapper memberTreeMapper;

  @Autowired private MemberInfoConvert memberInfoConvert;

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
    return memberInfoMapper.selectById(id);
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
      memberTreeMapper.insertPath(memberInfo.getId(), parent);
      memberTreeMapper.insertNode(memberInfo.getId());
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
    Long parent = memberTreeMapper.selectAncestor(id, 1);
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
    for (Long des : memberTreeMapper.selectDescendantId(id)) {
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
    memberTreeMapper.deletePath(id);
  }

  @Override
  public IPage<MemberInfoVO> queryPage(
      Page<MemberInfo> page, MemberInfoQueryDTO memberInfoQueryDto) {
    LambdaQueryWrapper<MemberInfo> query = Wrappers.lambdaQuery();
    // 检测当前是否可以进行模糊查询
    // 查询代理线处理
    // 多账号处理
    handleMultiAccount(memberInfoQueryDto);
    // 当前用户可查询层级过滤
    //    memberInfoQueryDto.setLevelList(UserLevelUtils.getAdminUserLevels(null));
    // 设置会员是否在线  处理数据查询后上线的情况

    return null;
  }

  private void handleMultiAccount(MemberInfoQueryDTO queryDTO) {
    if (queryDTO != null) {
      if (CollectionUtils.isEmpty(queryDTO.getAccountList())
          && StrUtil.isNotBlank(queryDTO.getAccount())) {
        List<String> accountList =
            Arrays.stream(
                    queryDTO
                        .getAccount()
                        .replace(StrUtil.SPACE, StrUtil.EMPTY)
                        .replace("，", StrUtil.COMMA)
                        .split(StrUtil.COMMA))
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(accountList)) {
          if (accountList.size() > 500) {
            throw new ServiceException("账号超限，最多支持500个账号查询");
          }
          if (accountList.size() == 1) {
            queryDTO.setAccountList(null);
            queryDTO.setAccount(accountList.get(0));
          } else {
            queryDTO.setAccountList(accountList);
            queryDTO.setAccount(null);
          }
        }
      }
    }
  }

  @Override
  public void save(MemberInfoAddDTO memberInfoAddDto) throws ServiceException {
    // 判断账号是否已存在
    if (this.lambdaQuery().eq(MemberInfo::getAccount, memberInfoAddDto.getAccount()).count() > 0) {
      throw new ServiceException("该账户已经存在，请确认后再添加");
    }
    // 默认添加到根下面
    this.add(
        memberInfoConvert.toEntity(memberInfoAddDto),
        Optional.ofNullable(memberInfoAddDto.getParentId()).orElse(0L));
  }

  @Override
  public void update(MemberInfoEditDTO memberInfoEditDto) {
    MemberInfo memberInfo = this.findById(memberInfoEditDto.getId());
    if (memberInfo == null) {
      throw new ServiceException("该账户已经不存在，请确认后再修改");
    }
    this.update(memberInfoConvert.toEntity(memberInfoEditDto));
  }
}
