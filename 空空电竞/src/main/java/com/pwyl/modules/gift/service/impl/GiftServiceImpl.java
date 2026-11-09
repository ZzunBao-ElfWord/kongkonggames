package com.pwyl.modules.gift.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.gift.dao.GiftDao;
import com.pwyl.modules.gift.entity.Gift;
import com.pwyl.modules.gift.service.GiftService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiftServiceImpl extends ServiceImpl<GiftDao, Gift> implements GiftService {

    @Override
    public List<Gift> selectGiftList(Integer status,String giftName){
        List<Gift> gifts = baseMapper.selectList(new QueryWrapper<Gift>().
                eq(status != null && status != 0, "status", status).
                eq(StringUtils.isNotEmpty(giftName), "gift_name", giftName).orderByAsc("sort"));
        return gifts;
    }

    @Override
    public Result selectGiftPage(Integer page,Integer limit,Integer status,String giftName){
        Page<Gift> pages=new Page<>(page,limit);
        IPage<Gift> gifts = baseMapper.selectPage(pages,new QueryWrapper<Gift>().
                eq(status != null && status != 0, "status", status).
                eq(StringUtils.isNotEmpty(giftName), "gift_name", giftName).orderByAsc("sort"));
        return Result.success().put("data",new PageUtils(gifts));
    }






}
