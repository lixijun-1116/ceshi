package com.kgc.wxdemo.config;




import com.kgc.wxdemo.entity.User;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version v1.0
 * @ProjectName: wxdemo
 * @ClassName: JxlExcel
 * @Description: TODO(一句话描述该类的功能)
 * @Author: 李茜骏
 * @Date: 2020/6/16 14:42
 */

public class JxlExcel {

    /**
     * 检验文件是否有效
     *
     * @param file
     * @throws Exception
     */
    public static void checkFile(MultipartFile file) throws Exception {
        //判断文件存在不存在
        if (null == file){
            throw new FileNotFoundException("文件不存在");
        }

        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")){
            throw new IOException(fileName+"不是excel文件");
        }
    }

    public static Workbook getWorkBook(MultipartFile file){
        // 获得文件名
        String fileName = file.getOriginalFilename();
        // 创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            if (fileName.endsWith("xls")){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if (fileName.endsWith("xlsx")){
                //2003
                workbook = new XSSFWorkbook(is);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return workbook;
    }
    /**
     * 获取解析后的文件内容
     *
     * @return
     */
    public static List<User> readExcel(MultipartFile file) throws Exception {


        List<User> list = new ArrayList<>();
        try {
            checkFile(file);
            //获得Workbook工作薄对象
            Workbook workbook = getWorkBook(file);
            System.out.println(workbook);
            int sheetSize = workbook.getNumberOfSheets();

            for (int i = 0; i<sheetSize;i++){
                //获取第一个张表
                Sheet sheet = workbook.getSheetAt(i);
                if (sheet == null) {
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //获取每行中的字段
                for (int j = firstRowNum+1;j<=lastRowNum;j++){
                    Row row = sheet.getRow(j); //获取行
                    if (row == null){  //掠过空格
                        continue;
                    }else{
                        //获取单元个中的值存入对象
                        User user = new User();
                        row.getCell(1).setCellType(CellType.STRING);

                        user.setName(row.getCell(0).getStringCellValue());
                        user.setPassword(row.getCell(1).getStringCellValue());
                        user.setGender(row.getCell(2).getStringCellValue());
                        String date = row.getCell(3).getStringCellValue();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = format.parse(date);
                        user.setDate(date1);
                        list.add(user);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Excel导入失败");
        }

        return list;
    }


    /**
     * 导出
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(List<User> list){
        //第一步创建一个HSSFWorkbook，对应一个EXCEL文件
        HSSFWorkbook wb = new HSSFWorkbook();

        //第二步 在workbook中添加一个sheet，对应excel文件中的sheet
        HSSFSheet sheet = wb.createSheet();

        //第三步 在sheet中添加表头第0行，注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        //第四步，创建单元格，并设置表头  设置表头剧中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        HSSFCell cell1 = row.createCell(0);
        cell1.setCellValue("姓名");
        cell1.setCellStyle(style);

        HSSFCell cell2 = row.createCell(1);
        cell2.setCellValue("密码");
        cell2.setCellStyle(style);

        HSSFCell cell3 = row.createCell(2);
        cell3.setCellValue("性别");
        cell3.setCellStyle(style);

        HSSFCell cell4 = row.createCell(3);
        cell4.setCellValue("出生日期");
        cell4.setCellStyle(style);

        //创建内容
        for (int i=0;i<list.size();i++){
            //第一行是表头，第二行开始插入数据
            row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(list.get(i).getName());
            row.createCell(1).setCellValue(list.get(i).getPassword());
            row.createCell(2).setCellValue(list.get(i).getGender());
            row.createCell(3).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        }
        return wb;
    }
}
