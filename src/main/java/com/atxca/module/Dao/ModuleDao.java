package com.atxca.module.Dao;

import com.atxca.module.PO.ModulePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 王志鹏
 * @title: ModuleDao
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/22 14:41
 */

public interface ModuleDao extends JpaRepository<ModulePO,Integer> {

    @Query(value = "SELECT * FROM atxca.sys_module AS b ORDER BY b.fatherModuleId , b.level , b.sort ASC;", nativeQuery = true)
    List<ModulePO> queryAllBySrot();
}
