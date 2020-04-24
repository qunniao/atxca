package com.atxca.sportshall.Dao;

import com.atxca.sportshall.PO.Vhouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 王志鹏
 * @title: VhouseDao
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/13 9:38
 */


public interface VhouseDao extends JpaRepository<Vhouse,Integer>, JpaSpecificationExecutor<Vhouse> {

    Vhouse queryById(int id);
}
