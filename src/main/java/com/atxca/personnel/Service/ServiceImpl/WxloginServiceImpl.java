package com.atxca.personnel.Service.ServiceImpl;

import com.alibaba.fastjson.JSONObject;
import com.atxca.Util.DateUtil;
import com.atxca.Util.HttpUtil;
import com.atxca.Util.SCException;
import com.atxca.personnel.Dao.UserDao;
import com.atxca.personnel.PO.UserPO;
import com.atxca.personnel.Service.WxloginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Date;

/**
 * @author 王志鹏
 * @title: WxloginServiceImpl
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/11 16:54
 */
@Service
public class WxloginServiceImpl implements WxloginService {

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserPO loginUserForWX(String code) throws SCException {
        System.out.println(code);
        String getOpendid = getOpendid(code);
        UserPO userPO = userDao.queryByOpenid(getOpendid);
        if (userPO == null) {
            userPO=userDao.queryByOpenid(addUser(getOpendid).getOpenid());
        }
        return userPO;
    }

    public UserPO addUser(String opendid) {
        UserPO userPO = new UserPO();
        userPO.setOpenid(opendid);
        userPO.setCreateTime(DateUtil.getDateToTimestamp(new Date()));
        userDao.save(userPO);
        return userPO;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(UserPO userPO) {
        UserPO userPO1 = userDao.queryByOpenid(userPO.getOpenid());
        userPO1.setName(userPO.getName());
        userDao.save(userPO1);
        return true;
    }

    private String getOpendid(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        Appid=wxed2bf9c3470a1021
//        AppSecret=e5f9fea8b3b79a03a7990819b5e08ef0

        params.add("appid", "wx92ab15fb58a05321");
        params.add("secret", "60f66820e5c72cd407476b490ef746a2");
        params.add("js_code", code);
        params.add("grant_type", "authorization_code");

        String res = HttpUtil.sendPostRequest("https://api.weixin.qq.com/sns/jscode2session", params);
        JSONObject responseSTR = JSONObject.parseObject(res); //将字符串{“id”：1}

        System.out.println(res);

        Object err = responseSTR.get("errcode");

        if (err != null) {
            throw new SCException(100109, "微信获取session_key失败");
        }

        String session_key = responseSTR.get("session_key").toString();
        String openid = responseSTR.get("openid").toString();


        return openid;
    }
}
