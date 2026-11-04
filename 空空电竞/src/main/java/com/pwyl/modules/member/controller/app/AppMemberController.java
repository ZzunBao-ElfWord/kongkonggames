package com.pwyl.modules.member.controller.app;

import com.pwyl.common.utils.Result;
import com.pwyl.modules.member.entity.Member;
import com.pwyl.modules.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "会员特权", tags = {"会员特权"})
@RequestMapping(value = "/app/member")
public class AppMemberController {


    @Autowired
    private MemberService memberService;

    @GetMapping("/selectMemberList")
    @ApiOperation("查询列表（不带分页）")
    public Result selectMemberList(){
        return memberService.selectMemberList();
    }


}
