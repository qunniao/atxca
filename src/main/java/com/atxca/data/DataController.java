package com.atxca.data;

import com.atxca.Util.ExcelUtil;
import com.atxca.Util.FastDFSClient;
import com.atxca.Util.PO.Result;
import com.atxca.Util.ResultUtil;
import com.atxca.Util.SCException;
import com.atxca.data.Dao.TypeDao;
import com.atxca.data.PO.DaochuPO;
import com.atxca.data.PO.TypePO;
import com.atxca.data.PO.Zpricure;
import com.atxca.order.Dao.OrderDao;
import com.atxca.order.PO.OrderPO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.atxca.Util.DateUtil.parseDate;

/**
 * @author 王志鹏
 * @title: DataController
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/14 9:19
 */

@Api(tags = "获取场馆及报表数据API")
@RestController
@RequestMapping(value = "/data")
public class DataController {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TypeDao typeDao;

    @Autowired
    private OrderDao orderDao;

    @ApiOperation(value = "获取场馆类型")
    @RequestMapping(value = "/queryTypePO", method = RequestMethod.POST)
    public Result<List<TypePO>> queryTypePO() throws SCException {

        return ResultUtil.success(typeDao.findAll());
    }

    @ApiOperation(value = "查询报表数据")
    @RequestMapping(value = "/daochu", method = RequestMethod.POST)
    public Result<List<DaochuPO>> daochu(@RequestParam String startTime, @RequestParam String endTime) throws SCException {
        String sql = "SELECT av.vName as vName,ah.name as name,count(*) as peoplenum,sum(ao.price) as price FROM atxca.venues AS av,atxca.periodtime AS ap,atxca.order AS ao,atxca.vhouse AS ah WHERE av.vid = ap.vid AND ap.pid = ao.pid AND ah.id = av.type AND ao.createTime between \"" + startTime + "\" and \"" + endTime + "\"group by av.vName , ah.name;";
        Query query = em.createNativeQuery(sql);
        query.unwrap(SQLQuery.class)
                .addScalar("vName", StandardBasicTypes.STRING)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("peoplenum", StandardBasicTypes.INTEGER)
                .addScalar("price", StandardBasicTypes.INTEGER)
                .setResultTransformer(Transformers.aliasToBean(DaochuPO.class));
        List<DaochuPO> list = query.getResultList();

        return ResultUtil.success(list);
    }

    /*
    @ApiOperation(value = "导出表数据")
    @RequestMapping(value = "/daochuExcel", method = RequestMethod.POST)
    public Result<String> daochuExcel(@RequestParam String startTime, @RequestParam String endTime) throws Exception {



        List<String> heads = new ArrayList<>();
        heads.add("场馆");
        heads.add("片场");
        heads.add("日期");
        heads.add("时间段");
        heads.add("价格");
        heads.add("状态");
        ByteArrayOutputStream baos = null;
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet("报表");
        ExcelUtil.headList(heads, sheet);

        Date start = parseDate(startTime, "yyyy-MM-dd");
        Date end = parseDate(endTime, "yyyy-MM-dd");
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(criteriaBuilder.between(root.get("reserveTime"), start, end));

                Predicate[] p = new Predicate[predicates.size()];
                return criteriaBuilder.and(predicates.toArray(p));
            }
        };
        List<OrderPO> list = orderDao.findAll(specification);
        double sumprice = 0;
        int cell = 1;
        for (OrderPO orderPO : list) {
            HSSFRow headerRow = sheet.createRow(cell);// 行
            int row = 0;
            List cloums = new ArrayList<>();

            TypePO typePO = typeDao.findById(orderPO.getVenuePO().getType()).get();
            cloums.add(typePO.getName());//场馆名
            cloums.add(orderPO.getVenuePO().getVName());//片场
            cloums.add(orderPO.getCreateTime());//日期
            cloums.add(orderPO.getBetTime());//时间段
            cloums.add(orderPO.getPrice() + "");//价格
            if (orderPO.getOpenid() == null) {
                cloums.add("现厂到场");
            } else {
                cloums.add("预约到场");
            }
            sumprice = sumprice + orderPO.getPrice();
            for (Object aaa : cloums) {
                HSSFCell cell0 = headerRow.createCell(row); // 列
                cell0.setCellValue(aaa.toString());
                row += 1;
            }
            cell += 1;
        }
        cell = cell + 1;
        HSSFRow headerRow = sheet.createRow(cell);// 行
        HSSFCell cell0 = headerRow.createCell(0); // 列
        cell0.setCellValue("总计");

        HSSFCell cell02 = headerRow.createCell(1); // 列
        cell02.setCellValue("订单数量:" + list.size());

        HSSFCell cell3 = headerRow.createCell(2); // 列
        cell3.setCellValue("总销售额" + sumprice);

        baos = new ByteArrayOutputStream();
        workbook.write(baos);

        FastDFSClient fastDFSClient = new FastDFSClient();
        String url = fastDFSClient.uploadFile(baos.toByteArray(), "xls");

        return ResultUtil.success("http://39.106.78.98/"+url);
    }
    */
//    @ApiOperation(value = "导出表数据")
//    @RequestMapping(value = "/daochuExcel", method = RequestMethod.POST)
//    public Result<String> daochuExcel(@RequestParam String startTime, @RequestParam String endTime) throws Exception {
//        String sql = "SELECT av.vName as vName,ah.name as name,count(*) as peoplenum,sum(ao.price) as price FROM atxca.venues AS av,atxca.periodtime AS ap,atxca.order AS ao,atxca.vhouse AS ah WHERE av.vid = ap.vid AND ap.pid = ao.pid AND ah.id = av.type AND ao.createTime between \"" + startTime + "\" and \"" + endTime + "\"group by av.vName , ah.name ; ";
//        Query query = em.createNativeQuery(sql);
//        query.unwrap(SQLQuery.class)
//                .addScalar("vName", StandardBasicTypes.STRING)
//                .addScalar("name", StandardBasicTypes.STRING)
//                .addScalar("peoplenum", StandardBasicTypes.INTEGER)
//                .addScalar("price", StandardBasicTypes.INTEGER)
//                .setResultTransformer(Transformers.aliasToBean(DaochuPO.class));
//        List<DaochuPO> list = query.getResultList();
//
//        List<String> heads = new ArrayList<>();
//        heads.add("场地名称");
//        heads.add("场馆名称");
//        heads.add("总预约成功次数");
//        heads.add("总金额");
//
//        ByteArrayOutputStream baos = null;
//        HSSFWorkbook workbook = new HSSFWorkbook();
//
//        HSSFSheet sheet = workbook.createSheet("报表");
//        ExcelUtil.headList(heads, sheet);
//        int cell = 1;
//        for (DaochuPO daochuPO : list) {
//            HSSFRow headerRow = sheet.createRow(cell);// 行
//            int row = 0;
//            List cloums = new ArrayList<>();
//            cloums.add(daochuPO.getVName());
//            cloums.add(daochuPO.getName());
//            cloums.add(daochuPO.getPeoplenum());
//            cloums.add(daochuPO.getPrice());
//
//            for (Object aaa : cloums) {
//                HSSFCell cell0 = headerRow.createCell(row); // 列
//                cell0.setCellValue(aaa.toString());
//                row += 1;
//            }
//            cell += 1;
//        }
//
//
//        baos = new ByteArrayOutputStream();
//        workbook.write(baos);
//        FastDFSClient fastDFSClient = new FastDFSClient();
//        String url = fastDFSClient.uploadFile(baos.toByteArray(), "xls");
//
//        return ResultUtil.success("http://39.106.78.98/"+url);
//    }


    @ApiOperation(value = "查询折线图数据")
    @RequestMapping(value = "/queryForPicture", method = RequestMethod.POST)
    public Result<List<Zpricure>> queryForPicture(@RequestParam String startTime, @RequestParam String endTime)throws Exception{
        String sql = "SELECT reserveTime, count(*) AS numers FROM atxca.order WHERE type = 4 AND createTime BETWEEN '2019-05-08 00:00:00' AND '2019-05-09 23:59:59' GROUP BY pid , reserveTime ; ";
        Query query = em.createNativeQuery(sql);

        query.unwrap(SQLQuery.class)
                .addScalar("reserveTime", StandardBasicTypes.STRING)
                .addScalar("numers", StandardBasicTypes.INTEGER)
                .setResultTransformer(Transformers.aliasToBean(Zpricure.class));

        List<Zpricure> sss = query.getResultList();
        return ResultUtil.success(sss);
    }

    public static void main(String[] args) {


    }
}
