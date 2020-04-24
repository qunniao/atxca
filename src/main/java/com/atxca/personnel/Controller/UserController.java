package com.atxca.personnel.Controller;

import com.atxca.Util.PO.Result;
import com.atxca.Util.ResultUtil;
import com.atxca.Util.SCException;
import com.atxca.personnel.Dao.UserManageDao;
import com.atxca.personnel.PO.UserManagePO;
import com.atxca.personnel.PO.UserPO;
import com.atxca.personnel.Service.WxloginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 王志鹏
 * @title: UserController
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/11 16:51
 */

@Api(tags = "微信登录")
@RestController
@RequestMapping(value = "/wxlog")
public class UserController {

    @Autowired
    private WxloginService wxloginService;

    @Autowired
    private UserManageDao userManageDao;


    @ApiOperation(value = "微信登录,通过code进行登录,返回Userd对象")
    @ApiImplicitParam(name = "code", value = "微信登录时获取的code", required = true)
    @RequestMapping(value = "/loginUserForWX", method = RequestMethod.POST)
    public Result<UserPO> loginUserForWX(@RequestParam String code) throws SCException {
        System.out.println(code);
        return ResultUtil.success(wxloginService.loginUserForWX(code));
    }

    @ApiOperation(value = "更新用户名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "微信openid", required = true),
            @ApiImplicitParam(name = "name", value = "名称", required = true),
    })
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public Result<UserPO> updateUser(UserPO userPO) throws SCException {

        return ResultUtil.success(wxloginService.updateUser(userPO));
    }

    @ApiOperation(value = "管理用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true),
            @ApiImplicitParam(name = "userPassWord", value = "密码", required = true),
    })
    @RequestMapping(value = "/loginUserManage", method = RequestMethod.POST)
    public Result<UserManagePO> loginUserManage(UserManagePO userManagePO) throws SCException {

        UserManagePO DBuserPO = userManageDao.findById(1).get();

        Md5Hash md5Hash = new Md5Hash(userManagePO.getUserPassWord());
        if (!(DBuserPO.getUserPassWord()).equals(md5Hash.toString())) {
            throw new SCException(11111, "用户账号或密码错误");
        }

        return ResultUtil.success(DBuserPO);
    }

    @ApiOperation(value = "管理用户修改")
    @ApiImplicitParam(name = "userPassWord", value = "密码", required = true)
    @RequestMapping(value = "/updateUserManage", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateUserManage(UserManagePO userManagePO) throws SCException {

        UserManagePO DBuserPO = userManageDao.getOne(1);
        Md5Hash md5Hash = new Md5Hash(userManagePO.getUserPassWord());
        DBuserPO.setUserPassWord(md5Hash.toString());
        userManageDao.save(DBuserPO);
        return ResultUtil.success(true);
    }

}
