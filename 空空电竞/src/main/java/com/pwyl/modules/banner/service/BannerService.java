package com.pwyl.modules.banner.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.banner.entity.Banner;
import com.pwyl.modules.taking.response.OrderTakingResponse;
import io.swagger.annotations.ApiParam;

import java.util.List;

public interface BannerService extends IService<Banner> {

    List<Banner> selectBannerList(Integer classify);

    List<Banner> selectBannerLists(Integer classify);

    int saveBody(String image, String url, Integer sort);

    Banner selectBannerById(Long id);

    Result updateBannerStateById(Long id);

    int updateBannerById(Banner banner);

    int insertBanner(Banner banner);

    PageUtils selectPage(Integer page, Integer limit, Integer classify, Integer state);

}
