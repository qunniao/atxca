package com.atxca.data.Dao;

import com.atxca.data.PO.DaochuPO;
import com.atxca.order.Dao.OrderDao;
import com.atxca.order.PO.OrderPO;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.atxca.Util.DateUtil.parseDate;

/**
 * @author 王志鹏
 * @title: TypeDaoTest
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/14 14:23
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TypeDaoTest {


    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OrderDao orderDao;


    @Test
    @Transactional(rollbackFor = Exception.class)
    public void afda() throws Exception {

    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void queryBdaochu() throws IOException {
        String sql = "SELECT reserveTime, count(*) AS pid FROM atxca.order WHERE type = 4 AND createTime BETWEEN '2019-05-08 00:00:00' AND '2019-05-09 23:59:59' GROUP BY pid , reserveTime;";
        Query query = em.createNativeQuery(sql);


        query.unwrap(SQLQuery.class)
                .addScalar("vName", StandardBasicTypes.STRING)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("peoplenum", StandardBasicTypes.INTEGER)
                .addScalar("price", StandardBasicTypes.INTEGER)
                .setResultTransformer(Transformers.aliasToBean(DaochuPO.class));
        List<DaochuPO> sss = query.getResultList();


        List<String> heads = new ArrayList<>();
        heads.add("场地名称");
        heads.add("场馆名称");
        heads.add("总预约成功次数");
        heads.add("总金额");

        ByteArrayOutputStream baos = null;
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet("XXX集团员工信息表");
        headList(heads, sheet);
        int cell = 1;
        for (DaochuPO daochuPO : sss) {
            HSSFRow headerRow = sheet.createRow(cell);// 行
            int row = 0;
            List cloums = new ArrayList<>();
            cloums.add(daochuPO.getVName());
            cloums.add(daochuPO.getName());
            cloums.add(daochuPO.getPeoplenum());
            cloums.add(daochuPO.getPrice());

            for (Object aaa : cloums) {
                HSSFCell cell0 = headerRow.createCell(row); // 列
                cell0.setCellValue(aaa.toString());
                row += 1;
            }
            cell += 1;
        }


        baos = new ByteArrayOutputStream();
        workbook.write(baos);

        FileOutputStream output = new FileOutputStream("G://aaaa.xls");
        output.write(baos.toByteArray());
    }


    @Test
    @Transactional(rollbackFor = Exception.class)
    public void querytest() throws IOException {
        String sql = "SELECT reserveTime, count(*) AS numers FROM atxca.order WHERE type = 4 AND createTime BETWEEN '2019-05-08 00:00:00' AND '2019-05-09 23:59:59' GROUP BY pid , reserveTime ; ";
        Query query = em.createNativeQuery(sql);

        query.unwrap(SQLQuery.class)
                .addScalar("reserveTime", StandardBasicTypes.STRING)
                .addScalar("numers", StandardBasicTypes.INTEGER);

        List<Object> sss = query.getResultList();
        System.out.println(sss.size());
        System.out.println(sss.size());
        System.out.println(sss.size());
        for (Object aa : sss) {
            System.out.println(aa.toString());
        }
    }

    public void headList(List<String> heads, HSSFSheet sheet) {

        int count = 0;
        HSSFRow headerRow = sheet.createRow(0);// 行
        for (String head : heads) {
            HSSFCell cell0 = headerRow.createCell(count); // 列
            cell0.setCellValue(head);
            count = count + 1;
        }
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        DaochuPO daochuPO = new DaochuPO();
        daochuPO.setName("场馆名称01");
        daochuPO.setPeoplenum(5);
        daochuPO.setPrice(20);
        daochuPO.setVName("场地名称");


        List cloums = new ArrayList<>();
        cloums.add(daochuPO.getVName());
        cloums.add(daochuPO.getName());
        cloums.add(daochuPO.getPeoplenum());
        cloums.add(daochuPO.getPrice());

        for (Object aaa : cloums) {
            System.out.println(aaa);
        }
//        Field[] field = daochuPO.getClass().getDeclaredFields();
//        for (int j = 0; j < field.length; j++) {//遍历所有属性
//            String name = field[j].getName();
//            System.out.println("attribute name:"+name);
//
//            Method m = daochuPO.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
//            String value = (String) m.invoke(daochuPO);
//            System.out.println(value);
//        }
    }


}
