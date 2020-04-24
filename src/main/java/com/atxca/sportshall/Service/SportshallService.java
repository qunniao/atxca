package com.atxca.sportshall.Service;

import com.atxca.core.page.Paging;
import com.atxca.sportshall.PO.PeriodTimePO;
import com.atxca.sportshall.PO.VenueOrderPO;
import com.atxca.sportshall.PO.VenuePO;
import com.atxca.sportshall.PO.Vhouse;
import com.atxca.sportshall.VO.VenueVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author 王志鹏
 * @title: SportshallService
 * @projectName atxca
 * @description: TODO
 * @date 2019/4/19 11:17
 */
public interface SportshallService {

    Page<VenuePO> queryVenueALL(Paging paging, VenuePO venuePO);

    Page<VenuePO> queryVenueForBack(Paging paging, VenueVO venueVO);

    List<PeriodTimePO> queryPeriodTimeALL(PeriodTimePO periodTimePO);

    List<Vhouse> queryVhouseALL(Vhouse vhouse);

    boolean addPeriodTime(PeriodTimePO PeriodTimePO);

    boolean addVenue(VenuePO venuePO);

    boolean addVhouse(Vhouse vhouse);

    //添加指定时间场地
    boolean addVenueOrder(VenueOrderPO venueOrderPO);

    boolean updatePeriodTimeType(int pid, int type);

    //更新详情数据
    boolean updatePeriodTime(PeriodTimePO periodTimePO);

    boolean deletePeriodTime(int pid);

    boolean deleteVenue(int vid);

}
