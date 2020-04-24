package com.atxca.sportshall.Dao;

import com.atxca.sportshall.PO.VenueOrderPO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 王志鹏
 * @title: VenueOrderDao
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/27 10:04
 */
public interface VenueOrderDao extends JpaRepository<VenueOrderPO, Integer> {
    VenueOrderPO queryById(int id);

    VenueOrderPO queryByReserveTimeAndAndPid(String reserveTime, int pid);
}
