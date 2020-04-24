package com.atxca.Util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author 王志鹏
 * @title: ExcelUtil
 * @projectName atxca
 * @description: TODO
 * @date 2019/4/22 11:41
 */
public class ExcelUtil {


    private static HSSFWorkbook creatExcel(String name) throws Exception {
        ByteArrayOutputStream baos = null;
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet("XXX集团员工信息表");

        HSSFRow headerRow = sheet.createRow(0);
        HSSFCell cell0 = headerRow.createCell(2);
        cell0.setCellValue("编号");
        baos = new ByteArrayOutputStream();
        workbook.write(baos);


        //src/main/resources/client.conf 内容仅一行 tracker_server=192.168.123.85:22122
        FastDFSClient fastDFSClient = new FastDFSClient();
        String url = fastDFSClient.uploadFile(baos.toByteArray(), "xls");
        System.out.println(url);

        return workbook;
    }


    public static void headList(List<String> heads, HSSFSheet sheet) {

        int count = 0;
        HSSFRow headerRow = sheet.createRow(0);// 行
        for (String head : heads) {
            HSSFCell cell0 = headerRow.createCell(count); // 列
            cell0.setCellValue(head);
            count = count + 1;
        }
    }

    public static void main(String[] args) throws Exception {
        ByteArrayOutputStream baos = null;
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet("XXX集团员工信息表");
        HSSFRow headerRow = sheet.createRow(1);// 列
        HSSFCell cell0 = headerRow.createCell(0); // 行
        cell0.setCellValue("编号");

        baos = new ByteArrayOutputStream();
        workbook.write(baos);

        FileOutputStream output = new FileOutputStream("G://aaaa.xls");
        output.write(baos.toByteArray());

        //src/main/resources/client.conf 内容仅一行 tracker_server=192.168.123.85:22122
//        FastDFSClient fastDFSClient = new FastDFSClient();
//        String url = fastDFSClient.uploadFile(baos.toByteArray(), "xls");
//        System.out.println(url);
    }
}
