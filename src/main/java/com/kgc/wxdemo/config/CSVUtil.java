package com.kgc.wxdemo.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.kgc.wxdemo.entity.User;

/**
 * 利用javacsv2.0做导入导出csv文件工具类<br/>
 * 
 * 
 * @author kpchen
 * 
 */
public class CSVUtil {

	static char separator = ',';

	public static void main(String[] args) throws Exception {

		// 测试导出
		String filePath = "D:/a.csv";
//		List<String[]> dataList = new ArrayList<String[]>();
//		for (int i = 0; i < 10; i++) {
//			dataList.add(new String[] { "0" + i, "小明" + i, "java" + i });
//		}
//		exportCsv(dataList, filePath);


		// 测试导入
		List<String[]> datas = importCsv(filePath);
		for ( int i=0;i<datas.size();i++){
			for ( int j=0;j<datas.get(i).length;j++){
				System.out.print(datas.get(i)[j]+",");

			}
			System.out.println();
		}
	}

	/**
	 * java导入csv文件
	 * 
	 * @param filePath
	 *            导入路径
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> importCsv(String filePath) throws Exception {
		CsvReader reader = null;
		List<String[]> dataList = new ArrayList<String[]>();
		try {
			reader = new CsvReader(filePath, separator, Charset.forName("GBK"));

			// 读取表头
			reader.readHeaders();
			// 逐条读取记录，直至读完
			while (reader.readRecord()) {
				dataList.add(reader.getRawRecord().split(","));
				// // 下面是几个常用的方法
				// 读取一条记录
				//System.out.println(reader.getRawRecord());
				// 按列名读取这条记录的值
				//System.out.println(reader.get(0));
				//System.out.println(reader.get(1));
				//System.out.println(reader.get(2));
				//System.out.println(reader.get(3));
			}
		} catch (Exception e) {
			System.out.println("读取CSV出错..." + e);
			throw e;
		} finally {
			if (null != reader) {
				reader.close();
			}
		}

		return dataList;
	}

	/**
	 * java导出cvs文件
	 * 
	 * @param dataList
	 *            数据集
	 * @param filePath
	 *            导出路径
	 * @return
	 * @throws Exception
	 */
	public static boolean exportCsv(List<String[]> dataList, String filePath) throws Exception {
		boolean isSuccess = false;
		CsvWriter writer = null;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath, true);
			writer = new CsvWriter(out, separator, Charset.forName("GBK"));
			for (String[] strs : dataList) {
				writer.writeRecord(strs);
			}

			isSuccess = true;
		} catch (Exception e) {
			System.out.println("生成CSV出错..." + e);
			throw e;
		} finally {
			if (null != writer) {
				writer.close();
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					System.out.println("exportCsv close Exception: " + e);
					throw e;
				}
			}
		}


		return isSuccess;
	}

	/**
	 * 删除.csv文件
	 * @param filePath 文件夹path
	 * @param fileName  文件名称
	 */
	public static void deleteFile(String filePath, String fileName) {

		File file = new File(filePath);

		if (file.exists()) {
			File[] files = file.listFiles();

			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					System.out.println(files[i].getName());
					if (files[i].getName().equals(fileName)) {
						files[i].delete();
						return;
					}
				}
			}
		}
	}

}
