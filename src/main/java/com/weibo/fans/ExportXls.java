package com.weibo.fans;

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExportXls {


    public void getExportXls(Set<Fan> data, File f){
        getExportXls(new ArrayList<>(data),f);
    }

    /**
     * 导出excel的xlsx文件
     *
     * @param data 数据（包含标题）
     * @param f    保存路径
     */
    public void getExportXls(List<Fan> data, File f) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(f);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        SXSSFWorkbook wb = new SXSSFWorkbook(10000);//内存中保留 10000 条数据，以免内存溢出，其余写入 硬盘
        SXSSFSheet sheet = createSXSSFSheet(wb, f.getName().split("__")[0], data);

        try {

            wb.write(os);
            data = null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } finally {
            try {
                os.close();
                wb.dispose();
                wb.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }

    }

    private SXSSFSheet createSXSSFSheet(SXSSFWorkbook wb, String title, List<Fan> data) {
        SXSSFSheet sheet = wb.createSheet(title);
        int totalCount = data.size();//总记录数
        SXSSFRow row;
        SXSSFCell cell;
        row = sheet.createRow(0);

        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("名称");
        row.createCell(2).setCellValue("性别");
        row.createCell(3).setCellValue("关注数");
        row.createCell(4).setCellValue("粉丝数");
        row.createCell(5).setCellValue("动态数");
        row.createCell(6).setCellValue("是否认证");
        row.createCell(7).setCellValue("签名");


        for (int i = 0; i < totalCount; i++) {
            Fan fan = data.get(i);
            row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(fan.getId());
            row.createCell(1).setCellValue(fan.getScreen_name());
            row.createCell(2).setCellValue(("f".equals(fan.getGender()) ? "女" : "男"));
            row.createCell(3).setCellValue(fan.getFollow_count());
            row.createCell(4).setCellValue(fan.getFollowers_count());
            row.createCell(5).setCellValue(fan.getStatuses_count());
            row.createCell(6).setCellValue(fan.getVerified() == true ? 1 : 0);
            row.createCell(7).setCellValue(fan.getDescription());


        }


        return sheet;

    }

}

