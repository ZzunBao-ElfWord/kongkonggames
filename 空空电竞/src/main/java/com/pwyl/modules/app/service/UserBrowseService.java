package com.pwyl.modules.app.service;

import com.pwyl.common.utils.Result;
import org.springframework.web.bind.annotation.RequestAttribute;

public interface UserBrowseService {
    Result selectMyVisitor(Long userId, Long page, Long limit);

    Result selectMyBrowse(Long userId, Long page, Long limit);

    Result selectAmount(Long userId);

    Result addAmount(Long userId, Long byBrowseId, Long takingId);

    Result addVisitor(Long userId, Long byBrowseId);

    /**
     * 删除我的访客
     *
     * @param id
     * @return
     */
    Result deleteMyVisitor(Long id);

    /**
     * 删除浏览足迹
     *
     * @param id
     * @return
     */
    Result deleteMyBrowse(Long id);
}
