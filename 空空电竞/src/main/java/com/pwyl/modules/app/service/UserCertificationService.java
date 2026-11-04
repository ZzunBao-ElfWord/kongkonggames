package com.pwyl.modules.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserCertification;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestAttribute;

public interface UserCertificationService extends IService<UserCertification> {

    Result insert(UserCertification userCertification);

    Result updateUserCertification(UserCertification userCertifications);

    Result isInsert(Long userId);

    Result queryCertification(Long page, Long limit, String status, String name);

    Result queryUserCertification(IPage iPage, String name,String phone);

    Result auditorUserCertification(Long status, Long userId, String remek);

    Result queryInsert(Long userId);
}
