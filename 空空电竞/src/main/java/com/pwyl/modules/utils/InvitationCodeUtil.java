package com.pwyl.modules.utils;

import com.pwyl.modules.app.entity.UserEntity;
import com.pwyl.modules.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 邀请码生成解密工具类
 * @author fang
 * @date 2020/7/8
 */
@Component
public class InvitationCodeUtil {


    /** 自定义进制（选择你想要的进制数，不能重复且最好不要0、1这些容易混淆的字符） */
    private static final char[] r=new char[]{ 'M', 'J', 'U', 'D', 'Z', 'X', '9', 'C', '7', 'P','E', '8', '6', 'B', 'G', 'H', 'S', '2', '5',  'F', 'R', '4','Q', 'W', 'K', '3', 'V', 'Y', 'T', 'N'};

    /** 定义一个字符用来补全邀请码长度（该字符前面是计算出来的邀请码，后面是用来补全用的） */
    private static final char b='A';

    /** 进制长度 */
    private static final int binLen=r.length;

    /** 邀请码长度 */
    private static final int s=6;


    /** 补位字符串 */
    private static final String e="KSLFXFR";

    private static UserService userService;

    @Autowired
    public void setUserService(UserService userService){
        InvitationCodeUtil.userService=userService;
    }

    /**
     * 根据ID生成六位随机码
     * @param
     * @return 随机码
     */
    public static String toSerialCode() {
        int t = (int) ((Math.random() * 9 + 1) * 100000);
        String code=String.valueOf(t);
        UserEntity userEntity = userService.queryByInvitationCode(code);
        if(userEntity==null){
            return code;
        }else{
            return toSerialCode();
        }
    }

    /**
     * 根据随机码生成ID
     * @param code 随机码
     * @return ID
     */
    public static long codeToId(String code) {
        char[] chs;
        chs = code.toCharArray();
        long res=0L;
        for(int i=0; i < chs.length; i++) {
            int ind=0;
            for(int j=0; j < binLen; j++) {
                if(chs[i] == r[j]) {
                    ind=j;
                    break;
                }
            }
            if(chs[i] == b) {
                break;
            }
            if(i > 0) {
                res=res * binLen + ind;
            } else {
                res=ind;
            }
        }
        return res;
    }






}