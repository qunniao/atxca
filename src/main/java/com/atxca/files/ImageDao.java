package com.atxca.files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 王志鹏
 * @title: ImageDao
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/11 17:29
 */
public interface ImageDao extends JpaRepository<ImagePO,Integer> {

}
