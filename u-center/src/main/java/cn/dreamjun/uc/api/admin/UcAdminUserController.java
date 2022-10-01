package cn.dreamjun.uc.api.admin;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.uc.service.IUserService;
import cn.dreamjun.uc.vo.RegisterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author 翊
 * @since 2022-09-09
 */

/**
 * - 获取用户列表 list（get）
 * - 重置用户密码 reset_pwd（put）
 * - 删除用户 （delete）
 * - 禁止用户 disable（put）
 * - 取消禁止 enable（put）
 */
@Api(tags = "管理员-用户管理接口")
@RestController
@PreAuthorize("@permission.adminPermission()")
public class UcAdminUserController {

    @Autowired
    IUserService userService;

    @ApiOperation(value = "获取用户列表", notes = "管理员-用户管理，获取用户列表 / 刷新列表，可携带查询条件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页页码，从 1 开始"),
            @ApiImplicitParam(name = "phone", value = "用户注册号码（非必须）"),
            @ApiImplicitParam(name = "email", value = "用户注册邮箱（非必须）"),
            @ApiImplicitParam(name = "name", value = "用户名（非必须）"),
            @ApiImplicitParam(name = "id", value = "用户 ID（非必须）"),
            @ApiImplicitParam(name = "status", value = "用户状态（非必须：0表示禁用；1表示正常）")
    })
    @GetMapping("/admin/user/list/{page}")
    public R listUser(@PathVariable(value = "page", required = true) int page,
                      @RequestParam(value = "phone", required = false) String phone,
                      @RequestParam(value = "email", required = false) String email,
                      @RequestParam(value = "name", required = false) String userName,
                      @RequestParam(value = "id", required = false) String userId,
                      @RequestParam(value = "status", required = false) String status) {
        return userService.listUser(page, phone, email, userName, userId, status);
    }

    @ApiOperation(value = "禁用用户", notes = "管理员-用户管理，禁用用户")
    @ApiImplicitParam(name = "userId", value = "用户 ID，路径参数")
    @PutMapping("/admin/user/disable/{userId}")
    public R disableUser(@PathVariable("userId") String userId) {
        return userService.disableUser(userId);
    }

    @ApiOperation(value = "重置用户信息", notes = "管理员-用户管理，重置用户信息（email / name / password）")
    @PutMapping("/admin/user/reset/{userId}")
    public R resetPassword(@PathVariable("userId") String userId,
                           @RequestBody RegisterVo registerVo) {
        return userService.resetPasswordByUid(userId, registerVo);
    }

    @ApiOperation(value = "初始化超级管理员帐户", notes = "超级管理员初始化使用")
    @PostMapping("/admin/user/init")
    public R initAdminAccount(@RequestBody RegisterVo registerVo) {
        return userService.initAdminAccount(registerVo);
    }

    @ApiOperation("根据用户名称模糊查询指定的用户")
    @GetMapping("/admin/user/keyword")
    @ApiImplicitParam(name = "keyword", value = "用户名称关键字")
    public R listUserByKeyword(@RequestParam("keyword") String keyword) {
        return userService.listUserByKeyword(keyword);
    }
}

