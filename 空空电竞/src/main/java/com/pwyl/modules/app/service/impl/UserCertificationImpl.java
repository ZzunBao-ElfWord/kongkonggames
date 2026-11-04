package com.pwyl.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwyl.common.utils.PageUtils;
import com.pwyl.common.utils.Result;
import com.pwyl.modules.app.dao.UserCertificationDao;
import com.pwyl.modules.app.dao.UserDao;
import com.pwyl.modules.app.entity.UserCertification;
import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserCertificationService;
import com.pwyl.modules.app.utils.VerifyIdCardUtils;
import com.pwyl.modules.common.service.CommonInfoService;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class UserCertificationImpl extends ServiceImpl<UserCertificationDao, UserCertification> implements UserCertificationService {

    private UserDao userDao;
    private CommonInfoService commonInfoService;

    @Override
    public Result insert(UserCertification userCertifications) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String value1 = commonInfoService.findOne(729).getValue();
        boolean flag=false;
        if("是".equals(value1) || StringUtils.isNotEmpty(userCertifications.getIdNumber())){
            //查询身份证是否被绑定
            UserCertification userCertification = baseMapper.selectOne(new QueryWrapper<UserCertification>().eq("id_number", userCertifications.getIdNumber()).eq("status", 1));
            if (userCertification != null) {
                return Result.error("身份证号已经被绑定");
            }
            String value = commonInfoService.findOne(260).getValue();
            if("是".equals(value)){
                //判断身份证是否合法
                String result = VerifyIdCardUtils.verifyIdCard(userCertifications.getIdNumber(), userCertifications.getName());
                if(!"ok".equals(result)){
                    return Result.error("您填写的信息有误，请检查后重试！");
                }
                flag=true;
            }
        }
        //查询是否实名过
        UserCertification userCertification1 = baseMapper.selectOne(new QueryWrapper<UserCertification>().eq("user_id", userCertifications.getUserId()));
        if (userCertification1 == null) {
            //没有实名过
            userCertifications.setCreateTime(simpleDateFormat.format(new Date()));
            if("是".equals(value1) || flag){
                if(flag){
                    Integer age = getAgeByCertId(userCertifications.getIdNumber());
                    UserEntity userEntity = userDao.selectById(userCertifications.getUserId());
                    userEntity.setIsAuthentication(1);
                    userEntity.setAge(age);
                    userDao.updateById(userEntity);
                    userCertifications.setStatus((long) 1);
                }else{
                    userCertifications.setStatus((long) 0);
                }
            }else{
                if(StringUtils.isNotEmpty(userCertifications.getIdNumber())){
                    userCertifications.setStatus((long) 0);
                }else{
                    userCertifications.setStatus((long) 1);
                }
                UserEntity userEntity = userDao.selectById(userCertifications.getUserId());
                userEntity.setIsAuthentication(1);
                userDao.updateById(userEntity);
            }
            userCertifications.setUpdateTime(simpleDateFormat.format(new Date()));
            baseMapper.insert(userCertifications);

        } else {
            //实名过
            userCertification1.setName(userCertifications.getName());
            userCertification1.setIdNumber(userCertifications.getIdNumber());
            userCertification1.setFront(userCertifications.getFront());
            userCertification1.setBack(userCertifications.getBack());
            if("是".equals(value1)){
                if(flag){
                    Integer age = getAgeByCertId(userCertifications.getIdNumber());
                    UserEntity userEntity = userDao.selectById(userCertifications.getUserId());
                    userEntity.setIsAuthentication(1);
                    userEntity.setAge(age);
                    userDao.updateById(userEntity);
                    userCertifications.setStatus((long) 1);
                }else{
                    userCertifications.setStatus((long) 0);
                }
            }else{
                if(StringUtils.isNotEmpty(userCertifications.getIdNumber())){
                    userCertifications.setStatus((long) 0);
                }else{
                    userCertifications.setStatus((long) 1);
                }
                UserEntity userEntity = userDao.selectById(userCertifications.getUserId());
                userEntity.setIsAuthentication(1);
                userDao.updateById(userEntity);
            }
            userCertification1.setUpdateTime(simpleDateFormat.format(new Date()));
            userCertification1.setServiceType(userCertifications.getServiceType());
            userCertification1.setDetails(userCertifications.getDetails());
            userCertification1.setSkillImg(userCertifications.getSkillImg());
            userCertification1.setLifePhoto(userCertifications.getLifePhoto());
            baseMapper.update(userCertification1, new QueryWrapper<UserCertification>().eq("user_id", userCertifications.getUserId()));
        }
        return Result.success();
    }

    @Override
    public Result updateUserCertification(UserCertification userCertifications) {
        //查询身份证是否被绑定
        UserCertification userCertification = baseMapper.selectOne(new QueryWrapper<UserCertification>().eq("id_number", userCertifications.getIdNumber()).eq("status", 1));
        if (userCertification != null) {
            return Result.error("身份证号已经被绑定");
        }
        boolean flag=false;
        String value = commonInfoService.findOne(260).getValue();
        if("是".equals(value)){
            //判断身份证是否合法
            String result = VerifyIdCardUtils.verifyIdCard(userCertifications.getIdNumber(), userCertifications.getName());
            if(!"ok".equals(result)){
                return Result.error("您填写的信息有误，请检查后重试！");
            }
            flag=true;
        }
        if(flag){
            userCertifications.setStatus((long) 1);
        }else{
            userCertifications.setStatus((long) 0);
        }
        baseMapper.update(userCertifications, new QueryWrapper<UserCertification>().eq("user_id", userCertifications.getUserId()));
        return Result.success();
    }


    @Override
    public Result isInsert(Long userId) {
        //UserCertification userCertification = baseMapper.selectOne(new QueryWrapper<UserCertification>().eq("user_id", userId).eq("status", 1));
        UserEntity userEntity = userDao.selectById(userId);
        if (userEntity.getIsAuthentication() != null && userEntity.getIsAuthentication()==1) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    public static Integer getAgeByCertId(String certId) {
        String birthday = "";
        if (certId.length() == 18) {
            birthday = certId.substring(6, 10) + "/"
                    + certId.substring(10, 12) + "/"
                    + certId.substring(12, 14);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date now = new Date();
        Date birth = new Date();
        try {
            birth = sdf.parse(birthday);
        } catch (ParseException e) {
        }
        long intervalMilli = now.getTime() - birth.getTime();
        int age = (int) (intervalMilli/(24 * 60 * 60 * 1000))/365;
        return age;
    }




    @Override
    public Result queryCertification(Long page, Long limit, String status, String name) {

        if (page == null || limit == null) {
            return Result.error("分页参数为空");
        } else {
            IPage<UserCertification> iPage = new Page(page, limit);
            String nname = null;
            if (name != null && !(name.equals(""))) {
                nname = "%" + name + "%";
            }
            IPage iPage1 = baseMapper.queryCertification(iPage, status, nname);
            List<UserCertification> userCertifications = iPage1.getRecords();
            for (UserCertification userCertification : userCertifications) {
                if (userCertification != null) {
                    //关联用户
                    userCertification.setUserEntity(userDao.selectById(userCertification.getUserId()));
                }
            }
            return Result.success().put("data", new PageUtils(iPage1));
        }
    }

    @Override
    public Result queryUserCertification(IPage iPage, String name,String phone) {
        String nname = null;
        if (name != null && !(name.equals(""))) {
            nname = "%" + name + "%";
        }
        String nphone=null;
        if(phone != null && !(phone.equals(""))){
            nphone = "%" + phone + "%";
        }

        return Result.success().put("data", baseMapper.queryUserCertification(iPage, nname,nphone));
    }

    @Override
    public Result auditorUserCertification(Long status, Long userId, String remek) {
        UserCertification userCertification = baseMapper.selectOne(new QueryWrapper<UserCertification>().eq("user_id", userId));
        if (userCertification != null) {
            if(status==1){
                UserEntity user=new UserEntity();
                user.setUserId(userId);
                user.setIsAuthentication(1);
                if(StringUtils.isNotEmpty(userCertification.getIdNumber())){
                    Integer age = getAgeByCertId(userCertification.getIdNumber());
                    user.setAge(age);
                }
                userDao.updateById(user);
            }
            userCertification.setStatus(status);
            userCertification.setRemek(remek);
            baseMapper.update(userCertification, new QueryWrapper<UserCertification>().eq("user_id", userId));
            return Result.success();
        } else {
            return Result.error("要审核的信息不存在！");
        }

    }

    @Override
    public Result queryInsert(Long userId) {
        return Result.success().put("data", baseMapper.selectOne(new QueryWrapper<UserCertification>().eq("user_id", userId)));
    }


}
