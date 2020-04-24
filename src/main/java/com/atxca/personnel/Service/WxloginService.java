package com.atxca.personnel.Service;

import com.atxca.Util.SCException;
import com.atxca.personnel.PO.UserPO;

/**
 * @author 王志鹏
 * @title: WxloginService
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/11 16:53
 */
public interface WxloginService {

    UserPO loginUserForWX(String code) throws SCException;

    boolean updateUser(UserPO userPO);
}
