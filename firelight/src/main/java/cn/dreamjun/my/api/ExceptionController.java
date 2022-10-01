package cn.dreamjun.my.api;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.common.exception.NotLoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Classname ExceptionController
 * @Description TODO
 * @Date 2022/9/27 16:07
 * @Created by 翊
 */
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public R handlerNotLoginException(NotLoginException e) {
        e.printStackTrace();
        return R.NOT_LOGIN();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R handlerException(Exception e) {
        e.printStackTrace();
        return R.FAILED("服务器繁忙");
    }
}
