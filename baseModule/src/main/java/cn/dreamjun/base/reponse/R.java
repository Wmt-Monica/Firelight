package cn.dreamjun.base.reponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname Response
 * @Description TODO
 * @Date 2022/9/9 9:09
 * @Created by 翊
 */
@Data
@ApiModel("返回对象")
public class R {

    public static final int CODE_SUCCESS = 20000;
    public static final int CODE_FAILED = 40000;
    public static final int CODE_NOT_LOGIN = 40001;
    public static final int CODE_NO_PERMISSION = 40002;

    //状态码
    @ApiModelProperty("是否成功：状态码，请参考状态码表")
    private int code;

    //描述
    @ApiModelProperty("描述信息")
    private String msg;
    //数据
    @ApiModelProperty("数据")
    private Object data;

    public R setData(Object data) {
        this.data = data;
        return this;
    }

    //提供一些静态的方法，可以快速地创建返回对象
    public static R SUCCESS(String msg) {
        R r = new R();
        r.code = CODE_SUCCESS;
        r.msg = msg;
        return r;
    }

    public static R SUCCESS(String msg, Object data) {
        R success = SUCCESS(msg);
        success.data = data;
        return success;
    }


    public static R SERVER_BUSY() {
        return FAILED("服务器繁忙，请稍后重试.");
    }

    public static R NOT_LOGIN() {
        R failed = FAILED("账号未登录.");
        failed.code = CODE_NOT_LOGIN;
        return failed;
    }

    public static R NO_PERMISSION() {
        R failed = FAILED("无权限访问");
        failed.code = CODE_NO_PERMISSION;
        return failed;
    }


    public static R FAILED(String msg) {
        R r = new R();
        r.code = CODE_FAILED;
        r.msg = msg;
        return r;
    }

    public static R FAILED(String msg, Object data) {
        R fail = FAILED(msg);
        fail.data = data;
        return fail;
    }

}
