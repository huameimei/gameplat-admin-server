package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.List;

@Data
public class TpMerchantPayTypeVO extends Model<TpMerchantPayTypeVO> {

    private Long id;

    private String name;

    private String parameters;

    private String payTypes;

    private String tpInterfaceCode;

    private List<TpPayTypeVO> tpPayTypeVOList;

    private TpInterfaceVO tpInterfaceVO;
}
