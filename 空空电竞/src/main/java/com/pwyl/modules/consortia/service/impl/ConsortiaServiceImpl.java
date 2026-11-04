package com.pwyl.modules.consortia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.DateUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserService;
import com.pwyl.modules.consortia.dao.ConsortiaDao;
import com.pwyl.modules.consortia.entity.Consortia;
import com.pwyl.modules.consortia.service.ConsortiaService;
import com.pwyl.modules.message.entity.MessageInfo;
import com.pwyl.modules.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ConsortiaServiceImpl extends ServiceImpl<ConsortiaDao, Consortia> implements ConsortiaService {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @Override
    public Consortia selectConsortiaByUserId(Long userId){
        return baseMapper.selectOne(new QueryWrapper<Consortia>().eq("user_id",userId));
    }

    @Override
    public Consortia selectConsortiaByNo(String consortiaNo){
        return baseMapper.selectOne(new QueryWrapper<Consortia>().eq("consortia_no",consortiaNo));
    }

    @Override
    public Result insertConsortia(Consortia consortia){
        Consortia consortia2 = selectConsortiaByNo(consortia.getConsortiaNo());
        if(consortia2!=null){
            return Result.error("该公会编号已经存在了！");
        }
        UserEntity userEntity = userService.selectUserById(consortia.getUserId());
        if(userEntity.getConsortiaId()!=null){
            Consortia consortia1 = baseMapper.selectById(userEntity.getConsortiaId());
            if(consortia1!=null){
                return Result.error("用户已经有公会了！");
            }
        }
        consortia.setCreateTime(DateUtils.format(new Date()));
        baseMapper.insertConsortia(consortia);
        userEntity.setConsortiaId(consortia.getConsortiaId());
        userService.updateById(userEntity);
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setContent("恭喜您，您的公会创建成功！");
        messageInfo.setTitle("公会创建成功");
        messageInfo.setState(String.valueOf(5));
        messageInfo.setUserName(userEntity.getUserName());
        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
        messageInfo.setCreateAt(DateUtils.format(new Date()));
        messageInfo.setIsSee("0");
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @Override
    public Result updateConsortia(Consortia consortia){
        Consortia consortia2 = selectConsortiaByNo(consortia.getConsortiaNo());
        if(consortia2!=null && !consortia2.getConsortiaId().equals(consortia.getConsortiaId())){
            return Result.error("该公会编号已经存在了！");
        }
        if(consortia.getUserId()!=null){
            Consortia oldConsortia = baseMapper.selectById(consortia.getConsortiaId());
            if(!consortia.getUserId().equals(oldConsortia.getUserId())){
                UserEntity userEntity = userService.selectUserById(consortia.getUserId());
                if(userEntity.getConsortiaId()!=null){
                    Consortia consortia1 = baseMapper.selectById(userEntity.getConsortiaId());
                    if(consortia1!=null){
                        return Result.error("用户已经有公会了！");
                    }
                }
                userEntity.setConsortiaId(consortia.getConsortiaId());
                userService.updateById(userEntity);
            }
        }
        baseMapper.updateById(consortia);
        return Result.success();
    }

    @Override
    public Result deleteConsortia(Long consortiaId){
        baseMapper.deleteById(consortiaId);
        userService.updateUserConsortiaIsNullByConsortiaId(consortiaId);
        return Result.success();
    }

    @Override
    public Result selectConsortiaList(Integer page,Integer limit,String consortiaName,String userName,String phone){
        return Result.success().put("data",baseMapper.selectConsortiaList(new Page<>(page,limit),consortiaName,userName,phone));
    }




}
