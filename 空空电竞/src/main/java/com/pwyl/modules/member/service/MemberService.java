package com.pwyl.modules.member.service;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.member.entity.Member;


public interface MemberService {

    Result insertMember(Member member);

    Result updateMember(Member member);

    Result deleteMemberById(Long memberId);

    Result selectMemberList();

    Result selectMemberPage(Integer page,Integer limit);

}
