package com.pwyl.modules.app.service;


import com.pwyl.common.utils.Result;

public interface IAppleService {

        Result getAppleUserInfo(String identityToken) throws Exception;

}
