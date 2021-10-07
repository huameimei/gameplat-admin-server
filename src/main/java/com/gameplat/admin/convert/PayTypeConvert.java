package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.PayTypeAddDTO;
import com.gameplat.admin.model.dto.PayTypeEditDTO;
import com.gameplat.admin.model.dto.PayTypeQueryDTO;
import com.gameplat.admin.model.entity.PayType;
import com.gameplat.admin.model.vo.PayTypeVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PayTypeConvert {

    PayTypeVO toVo(PayType entity);

    PayType toEntity(PayTypeAddDTO payTypeAddDTO);

    PayType toEntity(PayTypeQueryDTO payTypeQueryDTO);

    PayType toEntity(PayTypeEditDTO payTypeEditDTO);
}
