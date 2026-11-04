package com.pwyl.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.member.dao.MemberDao;
import com.pwyl.modules.member.entity.Member;
import com.pwyl.modules.member.service.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberDao, Member> implements MemberService {


    @Override
    public Result insertMember(Member member){
        baseMapper.insert(member);
        return Result.success();
    }

    @Override
    public Result updateMember(Member member){
        baseMapper.updateById(member);
        return Result.success();
    }

    @Override
    public Result deleteMemberById(Long memberId){
        baseMapper.deleteById(memberId);
        return Result.success();
    }

    @Override
    public Result selectMemberList(){
        return Result.success().put("data",baseMapper.selectList(new QueryWrapper<Member>().orderByAsc("sort")));
    }

    @Override
    public Result selectMemberPage(Integer page,Integer limit){
        IPage<Member> pages=new Page<>(page,limit);
        return Result.success().put("data",new PageUtils(baseMapper.selectPage(pages,new QueryWrapper<Member>().orderByAsc("sort"))));
    }



}







