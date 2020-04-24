package com.atxca.sportshall.Controller;

import com.atxca.Util.PO.Result;
import com.atxca.Util.ResultUtil;
import com.atxca.core.page.Paging;
import com.atxca.order.Dao.OrderDao;
import com.atxca.order.PO.OrderPO;
import com.atxca.sportshall.Dao.VenueDao;
import com.atxca.sportshall.Dao.VenueOrderDao;
import com.atxca.sportshall.Dao.VhouseDao;
import com.atxca.sportshall.PO.PeriodTimePO;
import com.atxca.sportshall.PO.VenueOrderPO;
import com.atxca.sportshall.PO.VenuePO;
import com.atxca.sportshall.PO.Vhouse;
import com.atxca.sportshall.Service.SportshallService;
import com.atxca.sportshall.VO.VenueVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 王志鹏
 * @title: sportController
 * @projectName atxca
 * @description: TODO
 * @date 2019/4/19 12:00
 */

@Api(tags = "场地API")
@RestController
@RequestMapping(value = "/sport")
public class sportController {

    @Autowired
    private SportshallService sportshallService;

    @Autowired
    private VhouseDao vhouseDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private VenueOrderDao venueOrderDao;

    @Autowired
    private VenueDao venueDao;

    @ApiOperation(value = "查询所有场地信息", notes = "支持条件查询:主键,名称,类别,组别")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true),
            @ApiImplicitParam(name = "pageSize", value = "容量", required = true),
            @ApiImplicitParam(name = "type", value = "场地类型1篮球馆、2羽毛球馆、3网球馆、4足球馆、5笼室足球馆", required = false),
            @ApiImplicitParam(name = "vid", value = "场地主键", required = false),
            @ApiImplicitParam(name = "vName", value = "场地名称", required = false),
            @ApiImplicitParam(name = "groups", value = "组别", required = false),
    })
    @RequestMapping(value = "/queryVenueForBack", method = RequestMethod.POST)
    public Result<Page<VenuePO>> queryVenueForBack(Paging paging, @ApiIgnore("venuePO") VenueVO venueVO) {

        return ResultUtil.success(sportshallService.queryVenueForBack(paging, venueVO));
    }


    @ApiOperation(value = "查询所有场馆信息", notes = "无")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true),
            @ApiImplicitParam(name = "pageSize", value = "容量", required = true),
            @ApiImplicitParam(name = "type", value = "分类的id", required = true)
    })
    @RequestMapping(value = "/queryVhouseALL", method = RequestMethod.POST)
    public Result<List<Vhouse>> queryVhouseALL(Paging page, Vhouse vhouse) {
        System.out.println(vhouse.getType());
        Pageable pageable = PageRequest.of(page.getPageNum() - 1, page.getPageSize(), Sort.Direction.ASC, "id");
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (vhouse.getType() != 0) {
                    predicates.add(criteriaBuilder.equal(root.get("type"), vhouse.getType()));
                }

                Predicate[] p = new Predicate[predicates.size()];
                return criteriaBuilder.and(predicates.toArray(p));
            }
        };
        return ResultUtil.success(vhouseDao.findAll(specification, pageable));
    }

    @ApiOperation(value = "查询所有场地信息", notes = "支持条件查询:主键,名称,类别,组别")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true),
            @ApiImplicitParam(name = "pageSize", value = "容量", required = true),
            @ApiImplicitParam(name = "type", value = "场地类型1篮球馆、2羽毛球馆、3网球馆、4足球馆、5笼室足球馆", required = false),
            @ApiImplicitParam(name = "vid", value = "场地主键", required = false),
            @ApiImplicitParam(name = "vName", value = "场地名称", required = false),
            @ApiImplicitParam(name = "groups", value = "组别", required = false),
    })
    @RequestMapping(value = "/queryVenueALL", method = RequestMethod.POST)
    public Result<Page<VenuePO>> queryVenueALL(Paging paging, @ApiIgnore("venuePO") VenuePO venuePO) {

        return ResultUtil.success(sportshallService.queryVenueALL(paging, venuePO));
    }

    @ApiOperation(value = "查询全部时间和价格信息", notes = "支持条件查询:主键,价格,时间段,类型")
    @RequestMapping(value = "/queryPeriodTimeALL", method = RequestMethod.POST)
    public Result<List<PeriodTimePO>> queryPeriodTimeALL(PeriodTimePO periodTimePO) {

        return ResultUtil.success(sportshallService.queryPeriodTimeALL(periodTimePO));
    }

    @ApiOperation(value = "添加场地详细信息", notes = "无")
    @RequestMapping(value = "/addPeriodTime", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vid", value = "外键", required = true),
            @ApiImplicitParam(name = "betTime", value = "时段", required = true),
            @ApiImplicitParam(name = "price", value = "周一到周五价格", required = true),
            @ApiImplicitParam(name = "price", value = "周六日价格", required = true),
            @ApiImplicitParam(name = "type", value = "0可预约,1待审核,2已预约,3关闭,4正在使用", required = true)
    })
    public Result<PeriodTimePO> addPeriodTime(PeriodTimePO PeriodTimePO) {

        return ResultUtil.success(sportshallService.addPeriodTime(PeriodTimePO));
    }

    @ApiOperation(value = "添加场地", notes = "无")
    @RequestMapping(value = "/addVenue", method = RequestMethod.POST)
    public Result<VenuePO> addVenue(VenuePO venuePO) {
        venuePO.setStatus(1);
        return ResultUtil.success(sportshallService.addVenue(venuePO));
    }

    @ApiOperation(value = "修改场地", notes = "无")
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/updateVenue", method = RequestMethod.POST)
    public Result<VenuePO> updateVenue(VenuePO venuePO) {
        VenuePO venuePO1 = venueDao.findById(venuePO.getVid()).get();

        venuePO1.setStatus(venuePO.getStatus());
        venuePO1.setVName(venuePO.getVName());
        venuePO1.setType(venuePO.getType());
        venuePO1.setGroups(venuePO.getGroups());
        venueDao.save(venuePO1);

        return ResultUtil.success(true);
    }

    @ApiOperation(value = "添加场馆", notes = "无")
    @RequestMapping(value = "/addVhouse", method = RequestMethod.POST)
    public Result<Boolean> addVhouse(Vhouse vhouse) {

        return ResultUtil.success(sportshallService.addVhouse(vhouse));
    }

    @ApiOperation(value = "添加/修改指定日期场馆状态", notes = "无")
    @RequestMapping(value = "/addVenueOrder", method = RequestMethod.POST)
    public Result<Boolean> addVenueOrder(VenueOrderPO venueOrderPO) {

        return ResultUtil.success(sportshallService.addVenueOrder(venueOrderPO));
    }

    @ApiOperation(value = "更新状态", notes = "无")
    @RequestMapping(value = "/updateVenueType", method = RequestMethod.POST)
    public Result<PeriodTimePO> updateVenueType(@RequestParam("pid") int pid, @RequestParam("type") int type) {

        return ResultUtil.success(sportshallService.updatePeriodTimeType(pid, type));
    }

    @ApiOperation(value = "更新时间价格状态", notes = "参数参考PeriodTimePO")
    @RequestMapping(value = "/updatePeriodTime", method = RequestMethod.POST)
    public Result<PeriodTimePO> updatePeriodTime(PeriodTimePO periodTimePO) {

        String orderId = periodTimePO.getOrderId();
        if (orderId != null && !orderId.equals("")) {
            OrderPO orderPO = orderDao.queryByOrderId(orderId);
            orderDao.save(orderPO);
        }


        return ResultUtil.success(sportshallService.updatePeriodTime(periodTimePO));
    }

    @ApiOperation(value = "删除 时间场地价格", notes = "无")
    @ApiImplicitParam(name = "pid", value = "主键", required = true)
    @RequestMapping(value = "/deletePeriodTime", method = RequestMethod.POST)
    public Result<PeriodTimePO> deletePeriodTime(@RequestParam("pid") int pid) {

        return ResultUtil.success(sportshallService.deletePeriodTime(pid));
    }

    @ApiOperation(value = "删除场地", notes = "无")
    @RequestMapping(value = "/deleteVenue", method = RequestMethod.POST)
    @ApiImplicitParam(name = "vid", value = "主键", required = true)
    public Result<PeriodTimePO> deleteVenue(@RequestParam int vid) {

        return ResultUtil.success(sportshallService.deleteVenue(vid));
    }

    @ApiOperation(value = "查询指定日期场地状态数据", notes = "无")
    @RequestMapping(value = "/queryByReserveTimeAndAndPid", method = RequestMethod.POST)
    @ApiImplicitParam(name = "vid", value = "主键", required = true)
    public Result<VenueOrderPO> queryByReserveTimeAndAndPid(VenueOrderPO venueOrderPO) {

        VenueOrderPO venueOrderPO1 = venueOrderDao.queryByReserveTimeAndAndPid(venueOrderPO.getReserveTime(), venueOrderPO.getPid());


        return ResultUtil.success(venueOrderPO1);
    }


}
