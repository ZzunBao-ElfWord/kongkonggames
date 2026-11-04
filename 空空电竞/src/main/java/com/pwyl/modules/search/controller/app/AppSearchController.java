package com.pwyl.modules.search.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.annotation.Login;
import com.pwyl.modules.app.entity.App;
import com.pwyl.modules.search.entity.Search;
import com.pwyl.modules.search.service.AppSearchService;
import com.pwyl.modules.search.service.SearchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 搜索记录
 *
 * @author liyuan
 * @since 2021-07-17
 */
@RestController
@RequestMapping("app/Search")
@AllArgsConstructor
@Slf4j
public class AppSearchController {

    private AppSearchService appSearchService;
    /**
     * 查询搜索记录
     */
    @CrossOrigin
    @Login
    @RequestMapping(value = "/selectAppSearchNum", method = RequestMethod.GET)
    public Result selectAppSearchNum(@RequestAttribute Long userId) {
        return appSearchService.selectAppSearchNum(userId);
    }

    /**
     * 删除用户的搜索记录
     */
    @Login
    @RequestMapping(value = "/deleteAppSearch", method = RequestMethod.GET)
    public Result deleteAppSearch(@RequestAttribute Long userId) {
        return appSearchService.deleteAppSearch(userId);
    }
}
