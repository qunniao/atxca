package com.atxca.personnel.Dao;

import com.atxca.personnel.PO.UserPO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 王志鹏
 * @title: UserDao
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/11 17:03
 */
public interface UserDao extends JpaRepository<UserPO, Integer> {
    UserPO queryByOpenid(String openid);
}
