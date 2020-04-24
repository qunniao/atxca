package com.atxca.sportshall.Service.ServiceImpl;

import com.atxca.Util.BeanUtil;
import com.atxca.Util.DateUtil;
import com.atxca.Util.GeneralUtils;
import com.atxca.Util.RandomUtil;
import com.atxca.core.page.Paging;
import com.atxca.sportshall.Dao.PeriodTimeDao;
import com.atxca.sportshall.Dao.VenueDao;
import com.atxca.sportshall.Dao.VenueOrderDao;
import com.atxca.sportshall.Dao.VhouseDao;
import com.atxca.sportshall.PO.PeriodTimePO;
import com.atxca.sportshall.PO.VenueOrderPO;
import com.atxca.sportshall.PO.VenuePO;
import com.atxca.sportshall.PO.Vhouse;
import com.atxca.sportshall.Service.SportshallService;
import com.atxca.sportshall.VO.VenueVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 王志鹏
 * @title: sportshallServiceImpl
 * @projectName atxca
 * @description: TODO
 * @date 2019/4/19 11:17
 */
@Service
public class SportshallServiceImpl implements SportshallService {

    @Autowired
    private PeriodTimeDao periodTimeDao;

    @Autowired
    private VenueDao venueDao;

    @Autowired
    private VhouseDao vhouseDao;

    @Autowired
    private VenueOrderDao venueOrderDao;


    @Override
    public Page<VenuePO> queryVenueALL(Paging page, VenuePO venuePO) {
        Pageable pageable = PageRequest.of(page.getPageNum() - 1, page.getPageSize(), Sort.Direction.DESC, "vid");

        Specification querySpecifi = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();
                //名称
                if (null != venuePO.getVName()) {
                    predicates.add(criteriaBuilder.like(root.get("vName"), "%" + venuePO.getVName() + "%"));
                }

                if (venuePO.getVid() != 0) {
                    predicates.add(criteriaBuilder.equal(root.get("vid"), venuePO.getVid()));
                }
                if (GeneralUtils.notEmpty(venuePO.getPeriodTimePO()) && GeneralUtils.notEmpty(venuePO.getPeriodTimePO().getBetTime())) {
                    predicates.add(criteriaBuilder.equal(root.join("periodTimePOList").get("betTime"), venuePO.getPeriodTimePO().getBetTime()));
                }

                if (venuePO.getType() != 0) {
                    predicates.add(criteriaBuilder.equal(root.get("type"), venuePO.getType()));
                }

                if (venuePO.getGroups() != 0) {
                    predicates.add(criteriaBuilder.equal(root.get("groups"), venuePO.getGroups()));
                }

                criteriaQuery.groupBy(root.get("vid"));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };


        return venueDao.findAll(querySpecifi, pageable);
    }

    @Override
    public Page<VenuePO> queryVenueForBack(Paging page, VenueVO venueVO) {

        Pageable pageable = PageRequest.of(page.getPageNum() - 1, page.getPageSize(), Sort.Direction.ASC, "vid");

        Specification querySpecifi = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                //名称
                if (null != venueVO.getVName()) {
                    predicates.add(criteriaBuilder.like(root.get("vName"), "%" + venueVO.getVName() + "%"));
                }

                if (null != venueVO.getVNumber()) {
                    predicates.add(criteriaBuilder.equal(root.get("vNumber"), venueVO.getVNumber()));
                }

                if (venueVO.getVid() != 0) {
                    predicates.add(criteriaBuilder.equal(root.get("vid"), venueVO.getVid()));
                }
                if (GeneralUtils.notEmpty(venueVO.getPeriodTimePO()) && GeneralUtils.notEmpty(venueVO.getPeriodTimePO().getBetTime())) {
                    Join<VenuePO, PeriodTimePO> join = root.join("periodTimePOList", JoinType.INNER);
                    predicates.add(criteriaBuilder.equal(join.get("betTime").as(String.class), venueVO.getPeriodTimePO().getBetTime()));
                }

                if (venueVO.getStartPrice() != 0 && venueVO.getEndPrice() != 0) {
                    Join<VenuePO, PeriodTimePO> join = root.join("periodTimePOList", JoinType.INNER);

//                    int w  = DateUtil.getIntWeekOfDate(venueVO.getTimes());
                    predicates.add(criteriaBuilder.equal(join.get("betTime").as(String.class), venueVO.getPeriodTimePO().getBetTime()));
                }

                if (venueVO.getType() != 0) {
                    predicates.add(criteriaBuilder.equal(root.get("type"), venueVO.getType()));
                }

                if (venueVO.getGroups() != 0) {
                    predicates.add(criteriaBuilder.equal(root.get("groups"), venueVO.getGroups()));
                }

                criteriaQuery.groupBy(root.get("vid"));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

//        List<VenuePO> list = venueDao.findAll(querySpecifi, pageable).getContent();


        return venueDao.findAll(querySpecifi, pageable);
    }

    @Override
    public List<PeriodTimePO> queryPeriodTimeALL(PeriodTimePO periodTimePO) {

        Specification querySpecifi = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (GeneralUtils.notEmpty(periodTimePO.getBetTime())) {
                    predicates.add(criteriaBuilder.equal(root.get("betTime"), periodTimePO.getBetTime()));
                }

                if (periodTimePO.getPrice() != 0) {
                    predicates.add(criteriaBuilder.equal(root.get("price"), periodTimePO.getPrice()));
                }

                if (periodTimePO.getWeekPrice() != 0) {
                    predicates.add(criteriaBuilder.equal(root.get("weekPrice"), periodTimePO.getWeekPrice()));
                }



                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };


        return periodTimeDao.findAll(querySpecifi);
    }

    @Override
    public List<Vhouse> queryVhouseALL(Vhouse vhouse) {

        Specification querySpecifi = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (vhouse.getType() != 0) {
                    predicates.add(criteriaBuilder.equal(root.get("type"), vhouse.getType()));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };


        return vhouseDao.findAll(querySpecifi);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPeriodTime(PeriodTimePO PeriodTimePO) {

        periodTimeDao.save(PeriodTimePO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addVenue(VenuePO venuePO) {
        venuePO.setVNumber(RandomUtil.getShortUuid());
        venueDao.save(venuePO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addVhouse(Vhouse vhouse) {
        vhouseDao.save(vhouse);
        return true;
    }

    @Override
    public boolean addVenueOrder(VenueOrderPO venueOrderPO) {
        String reserveTime = venueOrderPO.getReserveTime();
        int pid = venueOrderPO.getPid();

        VenueOrderPO venueOrderPO1 = venueOrderDao.queryByReserveTimeAndAndPid(reserveTime, pid);

        if (venueOrderPO1 != null) {
            venueOrderPO1.setType(venueOrderPO.getType());
            venueOrderDao.save(venueOrderPO1);
        }else {
            VenueOrderPO venueOrderPO2 = new VenueOrderPO();
            venueOrderPO2.setPid(pid);
            venueOrderPO2.setReserveTime(reserveTime);
            venueOrderDao.save(venueOrderPO2);
        }
        return true;
    }

    @Override
    public boolean updatePeriodTimeType(int pid, int type) {

        PeriodTimePO periodTimePO = periodTimeDao.getOne(pid);
        periodTimeDao.save(periodTimePO);

        return true;
    }

    @Override
    public boolean updatePeriodTime(PeriodTimePO periodTime) {

        PeriodTimePO periodTimePO = periodTimeDao.getOne(periodTime.getPid());

        BeanUtil.copyProperties(periodTime, periodTimePO, Arrays.asList("vid"));
        periodTimeDao.save(periodTimePO);
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePeriodTime(int pid) {

        periodTimeDao.deleteById(Integer.valueOf(pid));
        return true;
    }

    @Override
    public boolean deleteVenue(int vid) {

        venueDao.deleteById(vid);
        return true;
    }

}
