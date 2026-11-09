package com.pwyl.modules.consortia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.consortia.entity.Consortia;

public interface ConsortiaService extends IService<Consortia> {

    Consortia selectConsortiaByUserId(Long userId);

    Consortia selectConsortiaByNo(String consortiaNo);

    Result insertConsortia(Consortia consortia);

    Result updateConsortia(Consortia consortia);

    Result deleteConsortia(Long consortiaId);

    Result selectConsortiaList(Integer page,Integer limit,String consortiaName,String userName,String phone);

}
