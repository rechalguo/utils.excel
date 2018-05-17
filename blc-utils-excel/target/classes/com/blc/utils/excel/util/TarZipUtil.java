package com.blc.utils.excel.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;

public class TarZipUtil {

	/**
	 * 将文件写入到tar压缩包
	 * @param tout
	 * @param book
	 * @param fileName
	 * @throws IOException
	 */
	public static void wrapMessages(TarArchiveOutputStream tout,Workbook book,String fileName) throws IOException {
		String tempPath = System.getProperty("java.io.tmpdir");
		FileOutputStream fout = null;
		FileInputStream fin = null;
		String path = tempPath.concat(File.separator).concat(fileName);
		File f = null;
		TarArchiveEntry e = null;
		try {
			//设置Zip文件注释   TarArchiveEntry
			fout = new FileOutputStream(tempPath.concat(File.separator).concat(fileName));
			book.write(fout);
			f = new File(path);
			e = new TarArchiveEntry(f, f.getName()); 
			e.setSize(f.length());
			tout.putArchiveEntry(e);
			fin = new FileInputStream(f);
			IOUtils.copy(fin, tout);
			tout.closeArchiveEntry(); 
		} catch (FileNotFoundException ee) {
			ee.printStackTrace();
		} catch (IOException ee) {
			ee.printStackTrace();
		} finally {
			book.close();
		}
	}
	
	/**
	 * 将文件写入到zip压缩包
	 * @param tout
	 * @param book
	 * @param fileName
	 * @throws IOException
	 */
	public static long wrapMessagesToZip(ZipOutputStream  zout,Workbook book,String fileName) throws Exception {
		String tempPath = System.getProperty("java.io.tmpdir");
		FileOutputStream fout = null;
		String path = tempPath.concat(File.separator).concat(fileName);
		ZipEntry e = null;
		File f = null;
		//设置Zip文件注释   TarArchiveEntry
		fout = new FileOutputStream(path);
		book.write(fout);
		fout.flush();
		book.close();
		f = new File(path);
		e = new ZipEntry(f.getName());
		e.setSize(f.length());
		zout.putNextEntry(e);
		FileInputStream fin = new FileInputStream(f);
		IOUtils.copy(fin, zout);
		zout.closeEntry();
		try {
			return f.length();
		}catch(Exception ee) {
			System.out.println("delete file [ " + f.getAbsolutePath()+" ] error ");
		}finally {
			fin.close();
			fout.close();
			f.delete();
		}
		return f.length();
	}
	
	/**
	 * 获取zip压缩输出流
	 * @param out
	 * @return
	 */
	public static ZipOutputStream getZipOutputStream(OutputStream out) {
		CheckedOutputStream cos = new CheckedOutputStream(out, new CRC32());
		ZipOutputStream zos = new ZipOutputStream(cos);
		return zos;
	}
	
	public static ZipOutputStream wrapZipOutputStreamsWithsWorkbooks(ZipOutputStream zipOut, Map<String, Workbook> wbs)
		throws Exception{
		if(zipOut==null) return null;
		if(wbs ==null) return null;
		for(String name: wbs.keySet()) {
			wrapZipOutputStreamsWithWorkbook(zipOut, wbs.get(name), name);
		}
		return zipOut;
	}
	
	/**
	 * 将EXCEL 的workbook对象写入到压缩啊包，并制定EXCEL文件名 newName
	 * @param zipOut
	 * @param wb
	 * @param newName
	 * @throws Exception
	 */
	private static void wrapZipOutputStreamsWithWorkbook(ZipOutputStream zipOut, Workbook wb, String newName)
		throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		wb.write(bos);
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		wrapZipOutputStream(zipOut, bis, newName);
		wb.close();
	}
	
	/**
	 * 将单个输入流写入到 压缩包
	 * @param zipOut
	 * @param input
	 * @param newName
	 * @throws Exception
	 */
	private static void wrapZipOutputStream(ZipOutputStream zipOut, InputStream input, String newName)
			throws Exception {
		zipOut.putNextEntry(new ZipEntry(newName)) ;
		int temp = 0 ;
		while((temp=input.read())!=-1){	// 读取内容
			zipOut.write(temp) ;	// 压缩输出
		}
		input.close() ;	// 关闭输入流
		zipOut.closeEntry();
	}
	
}
