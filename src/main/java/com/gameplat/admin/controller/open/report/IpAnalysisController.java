package com.gameplat.admin.controller.open.report;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.mapper.MemberMapper;
import com.gameplat.admin.mapper.MemberWithdrawHistoryMapper;
import com.gameplat.admin.mapper.RechargeOrderHistoryMapper;
import com.gameplat.admin.model.dto.IpAnalysisDTO;
import com.gameplat.admin.model.vo.IpAnalysisVO;
import com.gameplat.base.common.exception.ServiceException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author lily
 * @description ip分析
 * @date 2022/1/18
 */
@Slf4j
@Api(tags = "IP分析报表")
@RestController
@RequestMapping("/api/admin/report/ip")
public class IpAnalysisController {

    @Autowired
    private RechargeOrderHistoryMapper rechargeOrderHistoryMapper;

    @Autowired
    private MemberWithdrawHistoryMapper memberWithdrawHistoryMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_PREFIX = "token:web:";

  @GetMapping(value = "/page")
  public IPage<IpAnalysisVO> page(PageDTO<IpAnalysisVO> page, IpAnalysisDTO dto) {

    IPage<IpAnalysisVO> pagelist = new PageDTO<>();
    List<IpAnalysisVO> list = new ArrayList<>();

    if (Objects.equals(dto.getType(), null)) {
      throw new ServiceException("分析类型不能为空");
    }
    if (dto.getType().equals(1)) {
      // 注册
      pagelist = memberMapper.page(page, dto);
      list = pagelist.getRecords();
    } else if (dto.getType().equals(2)) {
      // 登录
    } else if (dto.getType().equals(3)) {
      // 充值
      pagelist = rechargeOrderHistoryMapper.page(page, dto);
      list = pagelist.getRecords();
    } else if (dto.getType().equals(4)) {
      pagelist = memberWithdrawHistoryMapper.page(page, dto);
      list = pagelist.getRecords();
    }
    // 判断会员是否在线
    if (CollectionUtils.isNotEmpty(list)) {
      for (int i = 0; i < list.size(); i++) {
          IpAnalysisVO ipAnalysis = list.get(i);
        if (redisTemplate.hasKey(TOKEN_PREFIX + ipAnalysis.getAccount())) {
            ipAnalysis.setOffline(1);
            pagelist.getRecords().set(i, ipAnalysis);
        } else {
            ipAnalysis.setOffline(0);
            pagelist.getRecords().set(i, ipAnalysis);
        }
      }
    }
    return pagelist;
  }
}
