package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberWealRewordConvert;
import com.gameplat.admin.mapper.MemberWealRewordMapper;
import com.gameplat.admin.model.domain.MemberWealReword;
import com.gameplat.admin.model.dto.MemberWealRewordAddDTO;
import com.gameplat.admin.model.dto.MemberWealRewordDTO;
import com.gameplat.admin.model.vo.MemberWealRewordVO;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.MemberWealRewordService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.redis.redisson.DistributedLocker;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author lily
 * @description vip福利记录业务处理层
 * @date 2021/11/23
 */

@Service
@RequiredArgsConstructor
public class MemberWealRewordServiceImpl extends ServiceImpl<MemberWealRewordMapper, MemberWealReword> implements MemberWealRewordService {

    @Autowired private MemberWealRewordConvert rewordConvert;

    @Autowired private MemberWealRewordMapper rewordMapper;

    @Autowired private MemberService memberService;

    @Autowired
    private DistributedLocker distributedLocker;

    /**
     * 分页获取VIP福利记录列表
     */
    @Override
    public IPage<MemberWealRewordVO> findWealRewordList(IPage<MemberWealReword> page, MemberWealRewordDTO queryDTO) {
                return this.lambdaQuery()
                        .eq(ObjectUtils.isNotEmpty(queryDTO.getUserId()), MemberWealReword::getUserId, queryDTO.getUserId())
                        .eq(ObjectUtils.isNotEmpty(queryDTO.getSerialNumber()), MemberWealReword::getSerialNumber, queryDTO.getSerialNumber())
                        .like(ObjectUtils.isNotEmpty(queryDTO.getUserName()), MemberWealReword::getUserName, queryDTO.getUserName())
                        .eq(ObjectUtils.isNotEmpty(queryDTO.getStatus()), MemberWealReword::getStatus, queryDTO.getStatus())
                        .eq(ObjectUtils.isNotEmpty(queryDTO.getType()), MemberWealReword::getType, queryDTO.getType())
                        .ge(ObjectUtils.isNotEmpty(queryDTO.getStartTime()), MemberWealReword::getCreateTime, queryDTO.getStartTime())
                        .le(ObjectUtils.isNotEmpty(queryDTO.getEndTime()), MemberWealReword::getCreateTime, queryDTO.getEndTime())
                        .orderByDesc(MemberWealReword::getCreateTime)
                        .page(page)
                        .convert(rewordConvert::toVo);
    }

    /**
     * 不分页获取VIP福利记录列表
     */
    @Override
    public List<MemberWealReword> findList(MemberWealRewordDTO queryDTO){
        return this.lambdaQuery()
                .eq(ObjectUtils.isNotEmpty(queryDTO.getUserId()), MemberWealReword::getUserId, queryDTO.getUserId())
                .eq(ObjectUtils.isNotEmpty(queryDTO.getSerialNumber()), MemberWealReword::getSerialNumber, queryDTO.getSerialNumber())
                .like(ObjectUtils.isNotEmpty(queryDTO.getUserName()), MemberWealReword::getUserName, queryDTO.getUserName())
                .eq(ObjectUtils.isNotEmpty(queryDTO.getStatus()), MemberWealReword::getStatus, queryDTO.getStatus())
                .eq(ObjectUtils.isNotEmpty(queryDTO.getType()), MemberWealReword::getType, queryDTO.getType())
                .ge(ObjectUtils.isNotEmpty(queryDTO.getStartTime()), MemberWealReword::getCreateTime, queryDTO.getStartTime())
                .le(ObjectUtils.isNotEmpty(queryDTO.getEndTime()), MemberWealReword::getCreateTime, queryDTO.getEndTime())
                .orderByDesc(MemberWealReword::getCreateTime)
                .list();
    }

    @Override
    public void updateWealRecord(MemberWealReword entity) {
        if (!this.updateById(entity)){
            throw new ServiceException("修改福利记录失败");
        }
    }

    /**
     * 导出VIP福利记录列表
     */
    @Override
    public void exportWealReword(MemberWealRewordDTO queryDTO, HttpServletResponse response) {
        try {
            List<MemberWealReword> list = this.lambdaQuery()
                    .eq(ObjectUtils.isNotEmpty(queryDTO.getUserName()), MemberWealReword::getUserName, queryDTO.getUserName())
                    .eq(ObjectUtils.isNotEmpty(queryDTO.getStatus()), MemberWealReword::getStatus, queryDTO.getStatus())
                    .eq(ObjectUtils.isNotEmpty(queryDTO.getType()), MemberWealReword::getType, queryDTO.getType())
                    .ge(ObjectUtils.isNotEmpty(queryDTO.getStartTime()), MemberWealReword::getCreateTime, queryDTO.getStartTime())
                    .le(ObjectUtils.isNotEmpty(queryDTO.getEndTime()), MemberWealReword::getCreateTime, queryDTO.getEndTime())
                    .list();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=myExcel.xls");
            @Cleanup OutputStream ouputStream = null;
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("VIP福利记录导出","VIP福利记录"),
                    MemberWealReword.class, list );

            ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
        } catch (IOException e) {
            throw new ServiceException("导出失败:"+e);
        }

    }

    @Override
    public void insert(MemberWealRewordAddDTO dto) {
        MemberWealReword memberWealReword = rewordConvert.toEntity(dto);
        if (!this.save(memberWealReword)){
            throw new ServiceException("新增福利记录失败！");
        }
    }

//    @Override
//    public void check(MemberWealRewordCheckDTO dto, HttpServletRequest request) {
//        //获取奖励记录
//        MemberWealReword wealReword = rewordMapper.selectById(dto.getId());
//        if (wealReword.getStatus() != 0){
//            throw new ServiceException("已审核过!");
//        }
//
//        //获取奖励用户
//        MemberInfoVO member = memberService.getInfo(wealReword.getUserId());
//        //福利状态
//        Integer status = dto.getStatus();
//        //'已完成' 状态  表示审核通过
//        if (status == 2) {
//            // 账户资金锁
//            String lockKey = MessageFormat.format(MemberServiceKeyConstant.MEMBER_FINANCIAL_LOCK, member.getAccount());
//            try {
//                // 获取资金锁（等待8秒，租期120秒）
//                boolean flag = distributedLocker.tryLock(lockKey, TimeUnit.SECONDS, 8, 120);
//                if (!flag) {
//                    throw new ServiceException("资金锁未释放！");
//                }
//                // 给用户增加真币资产
//
//                //通知 发个人消息
//
//        } else if (status == 3) {//'已失效' 状态 表示审核不通过
//                wealReword.setRemark(dto.getRemark());
//        }
//    }


}
