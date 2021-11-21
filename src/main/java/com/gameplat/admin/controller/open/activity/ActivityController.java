//package com.gameplat.admin.controller.open.activity;
//
//import com.alibaba.fastjson.JSONObject;
//import com.live.cloud.backend.model.activity.dto.QualificationAddDTO;
//import com.live.cloud.backend.service.activity.QualificationService;
//import com.live.cloud.common.aop.AutoIdempotent;
//import com.live.cloud.common.base.AjaxResult;
//import com.live.cloud.common.context.DyDataSourceContextHolder;
//import com.live.cloud.common.exception.service.ServiceException;
//import com.live.cloud.common.util.StringUtils;
//import com.live.cloud.log.annotation.LogAnnotation;
//import com.live.cloud.web.core.BaseController;
//import com.live.cloud.web.core.RetUtils;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.web.bind.annotation.*;
//
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
///**
// * @Author: whh
// * @Date: 2020/8/20 10:20
// * @Description: 资格管理 处理用户等操作
// */
//@Slf4j
//@RestController
//@RequestMapping("/api/open/activity-server/activity")
//@Api(tags = "资格管理")
//public class ActivityController {
//
//    private final QualificationService qualificationService;
//
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    /**
//     * 申请方式 为手动 用户自己点击
//     * 用户点击参加活动
//     *
//     * @param userId     用户id
//     * @param activityId 活动id
//     */
//    @ApiOperation(value = "用户点击参加活动")
//    @GetMapping("/userClickAdv.json")
//    @LogAnnotation(module = "activity-center", recordRequestParam = false, desc = "用户点击参加活动")
//    public AjaxResult userClickAdv(@RequestParam("userId") Long userId, @RequestParam("activityId") Long activityId) {
//        if (StringUtils.isNull(userId)) {
//            //用户ID不能为空
//            return RetUtils.error("E001");
//        }
//        if (StringUtils.isNull(activityId)) {
//            //活动ID不能为空
//            return RetUtils.error("E002");
//        }
//        String dBSuffix = DyDataSourceContextHolder.getDBSuffix();
//        //幂等
//        Boolean result = redisTemplate.opsForValue().setIfAbsent(dBSuffix + ":idempotent" + ":" + userId + ":" + activityId, "0", 5000, TimeUnit.MILLISECONDS);
//        if (Boolean.FALSE.equals(result)) {
//            //请求正在处理中，请勿重复操作！
//            return RetUtils.error("A059");
//        }
//
//        List<Long> userIdList = new ArrayList<>();
//        userIdList.add(userId);
//        return qualificationService.userClickAdv(userIdList, activityId);
//    }
//
//    /**
//     * 资格检测
//     */
//    @ApiOperation(value = "资格检测")
//    @GetMapping("/deal_qualification.json")
//    @LogAnnotation(module = "activity-center", recordRequestParam = false, desc = "资格检测")
//    public JSONObject checkQualification(@RequestParam("userName") String userName, @RequestParam("activityId") Long activityId, @RequestParam("countDate") String countDate) {
//        return qualificationService.checkQualification(userName, activityId, countDate);
//    }
//
//    @ApiOperation(value = "添加用户资格")
//    @PostMapping("/add_qualification.json")
//    @LogAnnotation(module = "activity-center", recordRequestParam = false, desc = "添加用户资格")
//    @AutoIdempotent(expir = 3000, argIndex = {0})
//    public AjaxResult addQualification(@RequestBody QualificationAddDTO qualificationAddDTO) throws ParseException {
//        try {
//            return qualificationService.addQualification(qualificationAddDTO);
//        } catch (ServiceException e) {
//            return RetUtils.error(e.getMessage());
//        }
//    }
//
//    /**
//     * 测试点击参与
//     *
//     * @param userId     用户id
//     * @param activityId 活动id
//     */
//    @ApiOperation(value = "测试点击参与")
//    @GetMapping("/testUserClickAdv.json")
//    @LogAnnotation(module = "activity-center", recordRequestParam = false, desc = "测试点击参与")
//    @AutoIdempotent(expir = 5000, argIndex = {0, 1})
//    public AjaxResult testUserClickAdv(@RequestParam("userId") Long userId,
//                                       @RequestParam("activityId") Long activityId,
//                                       @RequestParam("countDate") String countDate) {
//        if (StringUtils.isNull(userId)) {
//            //用户ID不能为空
//            return RetUtils.error("用户ID不能为空");
//        }
//        if (StringUtils.isNull(activityId)) {
//            //活动ID不能为空
//            return RetUtils.error("活动ID不能为空");
//        }
//        if (StringUtils.isEmpty(countDate)) {
//            //申请时间不能为空
//            return RetUtils.error("申请时间不能为空");
//        }
//        List<Long> userIdList = new ArrayList<>();
//        userIdList.add(userId);
//        return qualificationService.testUserClickAdv(userIdList, activityId, countDate);
//    }
//
////    @RequiredPermission("activity:qualification:checkUser")
////    @ApiOperation(value = "检测用户是否存在")
////    @PostMapping("/check_user.json")
////    @LogAnnotation(module = "activity-center", recordRequestParam = false, desc = "检测用户是否存在")
////    public AjaxResult checkUser(@RequestBody String userName) {
////        return qualificationService.checkUser(userName);
////    }
//
//
////    @PostMapping("/test.json")
////    public void test() {
////        List<MemberActivityLobby> list = activityLobbyDao.findAllValid(LocalDate.now().toString());
////        List<Long> ids = new ArrayList<>();
////        for (MemberActivityLobby memberActivityLobby : list) {
////            ids.add(memberActivityLobby.getId());
////        }
////        // 根据活动id查询对应的优惠列表
////        List<MemberLobbyDiscount> lobbyDiscounts = memberLobbyDiscountDao.findLobbyDiscountListByLobbyIds(ids);
////        activityComponent.checkAndCumulative(list, lobbyDiscounts, null, false);
////    }
//}
