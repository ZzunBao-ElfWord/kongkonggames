package com.pwyl.modules.search.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.search.entity.Search;

public interface SearchService extends IService<Search> {
    Result insertSearch(Search search);
    Result selectByUserId(Long userId);
    Result deleteById(Long id);
}
