package com.example.basictest.utils;

import android.os.Environment;


import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

public class PdfToTxt {

/*	public static void main(String[] args) {
		try {
			//取得F盘下的pdf的内容
			readPdf("F:/SJXLX/心理咨询师考试重点整理---20150429.pdf");
		} catch (Exception e) {
			e.printStackTrace();
		}
	} */
	
	/**
	 * 传入一个.pdf文件
	 * @param file
	 * @throws Exception
	 */
	public static String readPdf(String file) throws Exception {
		File pdf=new File(file);
		PDDocument document=null;
		String content=null;
		try
		{
			// 方式一：
			/**
			 InputStream input = null;
			 input = new FileInputStream( pdfFile );
			 //加载 pdf 文档
			 PDFParser parser = new PDFParser(new RandomAccessBuffer(input));
			 parser.parse();
			 document = parser.getPDDocument();
			 **/

			// 方式二：
			document=PDDocument.load(pdf);

			// 获取页码
			int pages = document.getNumberOfPages();

			// 读文本内容
			PDFTextStripper stripper=new PDFTextStripper();
			// 设置按顺序输出
			stripper.setSortByPosition(true);
			stripper.setStartPage(1);
			stripper.setEndPage(pages);
			content = stripper.getText(document);
			System.out.println(content);
			return content;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return content;
	}
}