package com.pwyl.modules.consortia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.DateUtils;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.consortia.dao.ConsortiaApplyDao;
import com.pwyl.modules.consortia.entity.Consortia;
import com.pwyl.modules.consortia.entity.ConsortiaApply;
import com.pwyl.modules.consortia.service.ConsortiaApplyService;
import com.pwyl.modules.consortia.service.ConsortiaService;
import com.pwyl.modules.message.entity.MessageInfo;
import com.pwyl.modules.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ConsortiaApplyServiceImpl extends ServiceImpl<ConsortiaApplyDao, ConsortiaApply> implements ConsortiaApplyService {

    @Autowired
    private ConsortiaService consortiaService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @Override
    public Result insertConsortiaApply(ConsortiaApply consortiaApply){
        Consortia consortia;
        if(consortiaApply.getClassify()==1){
            consortia = consortiaService.selectConsortiaByNo(consortiaApply.getConsortiaNo());
            if(consortia==null){
                return Result.error("您输入的公会号码有误，请核对后再次提交！");
            }
        }else{
            consortia = consortiaService.getById(consortiaApply.getConsortiaId());
        }

        consortiaApply.setConsortiaId(consortia.getConsortiaId());
        consortiaApply.setStatus(1);
        consortiaApply.setCreateTime(DateUtils.format(new Date()));
        baseMapper.insert(consortiaApply);
        UserEntity userEntity = userService.selectUserById(consortia.getUserId());
        MessageInfo messageInfo = new MessageInfo();
        if(consortiaApply.getClassify()==1){
            messageInfo.setContent("新的成员申请加入公会，请尽快审核！");
            messageInfo.setTitle("公会申请待审核");
        }else{
            messageInfo.setContent("成员申请退出公会，请尽快审核！");
            messageInfo.setTitle("退会申请待审核");
        }
        messageInfo.setState(String.valueOf(5));
        messageInfo.setUserName(userEntity.getUserName());
        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
        messageInfo.setCreateAt(DateUtils.format(new Date()));
        messageInfo.setIsSee("0");
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @Override
    public Result auditConsortiaApply(String ids,Integer status,String content){
        for(String idStr:ids.split(",")){
            Long id=Long.parseLong(idStr);
            ConsortiaApply consortiaApply = baseMapper.selectById(id);
            if(consortiaApply.getStatus()==1){
                MessageInfo messageInfo = new MessageInfo();
                UserEntity user = userService.selectUserById(consortiaApply.getUserId());
                if(status==1){
                    consortiaApply.setStatus(2);
                    if(consortiaApply.getClassify()==1){
                        //加入公会
                        UserEntity userEntity = userService.selectUserById(consortiaApply.getUserId());
                        userEntity.setConsortiaId(consortiaApply.getConsortiaId());
                        userService.updateById(userEntity);

                        messageInfo.setContent("恭喜您，您已加入公会！");
                        messageInfo.setTitle("加入公会成功");

                    }else{
                        //退出公会
                        userService.updateUserConsortiaIsNullByUserId(consortiaApply.getUserId());

                        messageInfo.setContent("您成功已退出公会！");
                        messageInfo.setTitle("退出公会成功");
                    }
                }else{
                    consortiaApply.setStatus(3);
                    consortiaApply.setContent(content);
                    if(consortiaApply.getClassify()==1){
                        messageInfo.setContent("您申请加入公会被拒绝，请前往公会申请中查看拒绝原因！");
                        messageInfo.setTitle("加入公会申请失败");
                    }else{
                        messageInfo.setContent("您申请的退出公会被拒绝，请前往退出申请中查看原因！");
                        messageInfo.setTitle("退出公会失败");
                    }
                   
                }
                messageInfo.setState(String.valueOf(5));
                messageInfo.setUserName(user.getUserName());
                messageInfo.setUserId(String.valueOf(user.getUserId()));
                messageInfo.setCreateAt(DateUtils.format(new Date()));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
                baseMapper.updateById(consortiaApply);
            }
        }
        return Result.success();
    }

    @Override
    public Result selectConsortiaApplyList(Integer page,Integer limit,Integer status,Long consortiaId,Integer classify){
        IPage<ConsortiaApply> consortiaApplyIPage = baseMapper.selectConsortiaApplyList(new Page<>(page, limit), consortiaId, status,classify);
        return Result.success().put("data",new PageUtils(consortiaApplyIPage));
    }

    @Override
    public Result selectConsortiaApplyByUserId(Integer page,Integer limit,Integer status,Long userId){
        return Result.success().put("data",new PageUtils(baseMapper.selectPage(new Page<ConsortiaApply>(page,limit),
                new QueryWrapper<ConsortiaApply>().
                        eq(status!=null && status!=0,"status",status)
                        .eq(userId!=null,"user_id",userId))));
    }

    @Override
    public Result selectOldConsortiaApplyByUserId(Long userId,Integer classify){
        return Result.success().put("data",baseMapper.selectOne(
                new QueryWrapper<ConsortiaApply>()
                        .eq("user_id",userId)
                        .eq("classify",classify).orderByDesc("create_time").last(" limit 1")));
    }


}
