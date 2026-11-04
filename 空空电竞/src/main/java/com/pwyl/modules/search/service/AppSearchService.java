package com.pwyl.modules.search.service;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.search.entity.Search;
import org.springframework.web.bind.annotation.RequestAttribute;

/**
 * app搜索
 */
public interface AppSearchService {

    Result insetAppSearch(String searchName, Long userId);

    Result  selectAppSearchNum(Long userId);

    Result deleteAppSearch( Long userId);
}
