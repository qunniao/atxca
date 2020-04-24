package com.atxca.sportshall.Dao;

import com.atxca.sportshall.PO.VenuePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 王志鹏
 * @title: VenueDao
 * @projectName atxca
 * @description: TODO
 * @date 2019/4/19 11:02
 */
public interface VenueDao extends JpaRepository<VenuePO, Integer>, JpaSpecificationExecutor<VenuePO> {

    List<VenuePO> queryByType(int type);

    @Modifying
    @Query(value="update atxca.venues v set v.url =?1 where v.type=?2", nativeQuery = true)
    int updateVenueUrl(String url,Integer type);


}
