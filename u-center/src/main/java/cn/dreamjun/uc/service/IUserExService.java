package cn.dreamjun.uc.service;

import cn.dreamjun.base.reponse.R;

import javax.servlet.http.HttpServletRequest;

/**
 * @Classname IUserExService
 * @Description TODO
 * @Date 2022/9/10 10:54
 * @Created by 翊
 */
public interface IUserExService {
    R sendEmailCode(String verification, String emailAddress, Boolean mustRegister);
}
