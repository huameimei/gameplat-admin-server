package com.gameplat.admin.controller.open.live;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.enums.LiveRebatePeriodStatus;
import com.gameplat.admin.enums.LiveRebateReportStatus;
import com.gameplat.admin.model.domain.LiveRebateDetail;
import com.gameplat.admin.model.domain.LiveRebatePeriod;
import com.gameplat.admin.model.dto.LiveRebatePeriodQueryDTO;
import com.gameplat.admin.model.dto.OperLiveRebatePeriodDTO;
import com.gameplat.admin.model.vo.LiveRebatePeriodVO;
import com.gameplat.admin.service.LiveRebateDetailService;
import com.gameplat.admin.service.LiveRebatePeriodService;
import com.gameplat.admin.service.LiveRebateReportService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.redis.api.RedisService;
import com.gameplat.redis.exception.RedisOpsResultIsNullException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/live/liveRebatePeriod")
public class LiveRebatePeriodController {

  private static final String LIVE_REBATE_PAY_REDIS_LOCK = "live_rebate_pay_redis_lock";
  private static final String LIVE_REBATE_RUNNING_TASK_NAME = "live_rebate_running_task_name";

  @Autowired
  private LiveRebatePeriodService liveRebatePeriodService;

  @Autowired
  private LiveRebateDetailService liveRebateDetailService;

  @Autowired
  private LiveRebateReportService liveRebateReportService;

  @Autowired
  private RedisService redisService;

  @GetMapping(value = "queryAll")
  public IPage<LiveRebatePeriodVO> queryLiveRebatePeriod(Page<LiveRebatePeriod> page, LiveRebatePeriodQueryDTO dto) {
    return liveRebatePeriodService.queryLiveRebatePeriod(page,dto);
  }


  @PostMapping(value = "add")
  public void add(@RequestBody OperLiveRebatePeriodDTO dto) {
    liveRebatePeriodService.addLiveRebatePeriod(dto);
  }

  @PutMapping(value = "update")
  public void update(@RequestBody OperLiveRebatePeriodDTO dto){
    liveRebatePeriodService.updateLiveRebatePeriod(dto);
  }

  @PostMapping(value = "delete")
  public void delete(@RequestBody OperLiveRebatePeriodDTO dto){
    liveRebatePeriodService.deleteLiveRebatePeriod(dto.getId(), dto.getOnly());
  }


  @PostMapping(value = "settle")
  public void settle(@RequestBody OperLiveRebatePeriodDTO dto){
    // 正在发放、回收，不允许进行结算操作
    try {
      redisService.getStringOps().setEx(LIVE_REBATE_PAY_REDIS_LOCK, 0, 3600, TimeUnit.SECONDS);
      LiveRebatePeriod liveRebatePeriod = liveRebatePeriodService.getById(dto.getId());
      if (liveRebatePeriod == null) {
        redisService.getKeyOps().delete(LIVE_REBATE_PAY_REDIS_LOCK);
        throw new ServiceException("真人返水期号不存在");
      }
      if (liveRebatePeriod.getStatus() != LiveRebatePeriodStatus.UNSETTLED.getValue()) {
        redisService.getKeyOps().delete(LIVE_REBATE_PAY_REDIS_LOCK);
        throw new ServiceException("期号状态不是未结算,不能进入结算操作");
      }
      try{
        liveRebatePeriodService.settle(dto.getId());
      } finally {
        redisService.getKeyOps().delete(LIVE_REBATE_PAY_REDIS_LOCK);
      }
    } catch (RedisOpsResultIsNullException e) {
      String taskName = (String) redisService.getStringOps().get(LIVE_REBATE_RUNNING_TASK_NAME);
      throw new ServiceException(String.format("正在执行[%s]，请稍后重试！", taskName));
    }
  }


  @PostMapping(value = "batchAccept")
  public void accept(@RequestBody OperLiveRebatePeriodDTO dto){
    LiveRebatePeriod rebatePeriod = liveRebatePeriodService.getById(dto.getId());
    if (rebatePeriod == null) {
      throw new ServiceException("返水期号不存在");
    }
    if (rebatePeriod.getStatus() != LiveRebatePeriodStatus.SETTLED.getValue()) {
      throw new ServiceException("期号状态不是结算状态,不能进入派发操作");
    }
    String taskName = String.format("发放 %s", dto.getName());
    asyncAndSingleTask(taskName, () -> {
      List<LiveRebateDetail> liveRebateDetailList = liveRebateDetailService
          .liveRebateDetailByStatus(dto.getId(), LiveRebateReportStatus.UNACCEPTED.getValue());
      String statTime = "";
      for (LiveRebateDetail liveRebateDetail : liveRebateDetailList) {
        try {
          if (StringUtils.isEmpty(statTime)) {
            statTime = liveRebateDetail.getStatTime();
          }
          liveRebateReportService.accept(dto.getId(), liveRebateDetail.getMemberId(),
              liveRebateDetail.getRealRebateMoney(),
              liveRebateDetail.getPeriodName() + "-真人返水");
        } catch (Exception e) {
          log.error("派发异常: " + JSONUtil.toJsonStr(liveRebateDetail));
          throw new RuntimeException("派发异常", e);
        }
      }

      //更新状态
      LiveRebatePeriod liveRebatePeriod = new LiveRebatePeriod();
      liveRebatePeriod.setStatus(LiveRebatePeriodStatus.ACCEPTED.getValue());
      LambdaUpdateWrapper<LiveRebatePeriod> updateWrapper = Wrappers.lambdaUpdate();
      updateWrapper.eq(LiveRebatePeriod::getId,dto.getId());
      if(!liveRebatePeriodService.update(liveRebatePeriod,updateWrapper)){
        throw new ServiceException("更新真人期数配置失败！");
      }
      // TODO 添加真人返水每日统计
      //this.userBusDayReportManager.userBusDayReportQueue(BusReportType.LIVE_REBATE.getValue(), statTime);
    });
  }

  //回收
  @RequestMapping(value = "rollBack", method = RequestMethod.POST)
  //@Log(type = LogType.LIVEREBATE, content = "'真人返水回收:期数:'+#name")
  public void rollBack(@RequestBody OperLiveRebatePeriodDTO dto){
    LiveRebatePeriod rebatePeriod = liveRebatePeriodService.getById(dto.getId());
    if (rebatePeriod == null) {
      throw new ServiceException("返水期号不存在");
    }
    if (rebatePeriod.getStatus() != LiveRebatePeriodStatus.ACCEPTED.getValue()) {
      throw new ServiceException("期号状态不是派发状态,不能进入回收操作");
    }
    String taskName = String.format("回收 %s", dto.getName());
    asyncAndSingleTask(taskName, () -> {
      String statTime = "";
      List<LiveRebateDetail> liveRebateDetailList = liveRebateDetailService
          .liveRebateDetailByStatus(dto.getId(), LiveRebateReportStatus.ACCEPTED.getValue());
      for (LiveRebateDetail liveRebateDetail : liveRebateDetailList) {
        try {
          if (StringUtils.isEmpty(statTime)) {
            statTime = liveRebateDetail.getStatTime();
          }
          liveRebateReportService
              .rollBack(liveRebateDetail.getMemberId(), liveRebateDetail.getPeriodId(),
                  liveRebateDetail.getPeriodName(), liveRebateDetail.getRealRebateMoney(),
                  liveRebateDetail.getRemark());
        } catch (Exception e) {
          log.error("回收异常: " + JSONUtil.toJsonStr(liveRebateDetail));
          throw new RuntimeException("回收异常", e);
        }
      }

      //更新状态
      LiveRebatePeriod liveRebatePeriod = new LiveRebatePeriod();
      liveRebatePeriod.setStatus(LiveRebatePeriodStatus.ROLLBACKED.getValue());
      LambdaUpdateWrapper<LiveRebatePeriod> updateWrapper = Wrappers.lambdaUpdate();
      updateWrapper.eq(LiveRebatePeriod::getId,dto.getId());
      liveRebatePeriodService.update(liveRebatePeriod,updateWrapper);

      // TODO 添加真人返水每日统计
      //this.userBusDayReportManager.userBusDayReportQueue(BusReportType.LIVE_REBATE.getValue(), statTime);
    });
  }


  private void asyncAndSingleTask(String taskName, Runnable task) throws ServiceException {
    try {
      redisService.getStringOps().setEx(LIVE_REBATE_PAY_REDIS_LOCK, 0, 3600,TimeUnit.SECONDS);
      redisService.getStringOps().set(LIVE_REBATE_RUNNING_TASK_NAME, taskName);
      new Thread(() -> {
        try {
          task.run();
        } catch (Throwable e) {
          e.printStackTrace();
        } finally {
          redisService.getKeyOps().delete(LIVE_REBATE_PAY_REDIS_LOCK);
        }
      }).start();
    } catch (RedisOpsResultIsNullException e) {
      String name = (String) redisService.getStringOps().get(LIVE_REBATE_RUNNING_TASK_NAME);
      throw new ServiceException(String.format("正在执行[%s]，请稍后重试！", name));
    }
  }

}
