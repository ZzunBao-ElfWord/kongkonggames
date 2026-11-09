package com.pwyl.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserVip;
import org.springframework.web.bind.annotation.RequestAttribute;

public interface UserVipService extends IService<UserVip> {

    UserVip selectUserVipByUserId(Long userId);

    Result isUserVip(Long userId);

}
