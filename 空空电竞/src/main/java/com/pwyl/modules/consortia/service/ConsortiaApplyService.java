package com.pwyl.modules.consortia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.consortia.entity.ConsortiaApply;

public interface ConsortiaApplyService extends IService<ConsortiaApply> {


    Result insertConsortiaApply(ConsortiaApply consortiaApply);

    Result auditConsortiaApply(String ids,Integer status,String content);

    Result selectConsortiaApplyList(Integer page,Integer limit,Integer status,Long consortiaId,Integer classify);

    Result selectConsortiaApplyByUserId(Integer page,Integer limit,Integer status,Long userId);

    Result selectOldConsortiaApplyByUserId(Long userId,Integer classify);

}
