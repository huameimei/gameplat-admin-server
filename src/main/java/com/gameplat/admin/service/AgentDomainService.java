package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.AgentDomainDTO;
import com.gameplat.admin.model.vo.AgentDomainVO;
import com.gameplat.model.entity.spread.AgentDomain;



public interface AgentDomainService extends IService<AgentDomain> {

   /**
    * 获取代理域名列表
    */
   IPage<AgentDomainVO> agentDomainList(PageDTO<AgentDomain> page, AgentDomainDTO domainDTO);

   /**
    * 创建域名
    */
   void createAgentDomain(AgentDomainDTO domainDTO);

   /**
    * 删除代理域名
    */
   void deleteAgentDomain(Integer id);

   /**
    * 修改代理域名
    */
   void updateAgentDomain(AgentDomainDTO domainDTO);
}
