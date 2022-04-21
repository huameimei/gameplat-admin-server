package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.AgentDomainConvert;
import com.gameplat.admin.convert.GameBarConvert;
import com.gameplat.admin.mapper.AgentDomainMapper;
import com.gameplat.admin.model.dto.AgentDomainDTO;
import com.gameplat.admin.model.dto.ChatNoticeQueryDTO;
import com.gameplat.admin.model.vo.AgentDomainVO;
import com.gameplat.admin.model.vo.ChatNoticeVO;
import com.gameplat.admin.service.AgentDomainService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.chart.ChatNotice;
import com.gameplat.model.entity.spread.AgentDomain;
import com.gameplat.model.entity.spread.SpreadUnion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 域名推广配置 服务实现层
 *
 * @author three
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class AgentDomainServiceImpl extends ServiceImpl<AgentDomainMapper, AgentDomain>  implements AgentDomainService {

  @Autowired private AgentDomainConvert convert;

  /**
   * 代理域名列表
   */
  @Override
  @SentinelResource(value = "agentDomainList")
  public IPage<AgentDomainVO>  agentDomainList(PageDTO<AgentDomain> page,AgentDomainDTO domainDTO) {
    return  this.lambdaQuery()
            .eq(ObjectUtils.isNotEmpty(domainDTO.getType()),AgentDomain::getType,domainDTO.getType())
            .eq(ObjectUtils.isNotEmpty(domainDTO.getStatus()),AgentDomain::getStatus,domainDTO.getStatus())
            .like(ObjectUtils.isNotEmpty(domainDTO.getDomain()),AgentDomain::getDomain,domainDTO.getDomain())
            .orderByDesc(AgentDomain::getCreateTime)
            .page(page)
            .convert(convert::toVo);
  }

  /**
   * 创建代理域名
   */
  @Override
  @SentinelResource(value = "createAgentDomain")
  public void createAgentDomain(AgentDomainDTO domainDTO) {
    List<AgentDomain> list = this.lambdaQuery()
            .eq(AgentDomain::getPromoteProtocol, domainDTO.getPromoteProtocol())
            .eq(AgentDomain::getDomain, domainDTO.getDomain())
            .list();
    if (list.size() > 1) throw new ServiceException("此域名已添加过了!");
    this.save(convert.toDto(domainDTO));
  }

  /**
   * 删除代理域名
   */
  @Override
  @SentinelResource(value = "deleteAgentDomain")
  public void deleteAgentDomain(Integer id) {
    this.removeById(id);
  }

  /**
   * 修改代理域名
   */
  @Override
  @SentinelResource(value = "updateAgentDomain")
  public void updateAgentDomain(AgentDomainDTO domainDTO) {
    this.lambdaUpdate()
            .set(AgentDomain::getPromoteProtocol,domainDTO.getDomain())
            .set(AgentDomain::getDomain,domainDTO.getDomain())
            .set(AgentDomain::getType,domainDTO.getType())
            .set(AgentDomain::getStatus,domainDTO.getStatus())
            .set(AgentDomain::getRemark,domainDTO.getRemark())
            .set(AgentDomain::getUpdateBy,domainDTO.getUpdateBy())
            .set(AgentDomain::getUpdateTime,domainDTO.getUpdateTime())
            .update();
  }

}
