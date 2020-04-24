package com.atxca.sportshall.Dao;

import com.atxca.sportshall.PO.PeriodTimePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 王志鹏
 * @title: PeriodTimeDao
 * @projectName atxca
 * @description: TODO
 * @date 2019/4/19 11:02
 */
public interface PeriodTimeDao extends JpaRepository<PeriodTimePO, Integer>, JpaSpecificationExecutor {



}
