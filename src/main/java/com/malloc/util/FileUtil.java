package com.malloc.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc 文件工具类
 *
 * @author liwei
 *
 */
public final class FileUtil {

	private static Logger logger = Logger.getLogger(FileUtil.class);
	private static String CLASSNAME = FileUtil.class.getName();

	private FileUtil(){}

	/**
	 * 把文件复制到指定文件夹下，字节流方式
	 *
	 * @param beginFileName
	 * @param endDir
	 * @return
	 */
	public static boolean copyFileStream(String beginFileName, String endDir) {
		logger.debug(">>>>>" + CLASSNAME + ".copyFileStream().....");
		logger.debug("beginFileName:" + beginFileName);
		logger.debug("endDir:" + endDir);

		boolean b = false;
		if(StringUtil.isTrimNull(beginFileName) || StringUtil.isTrimNull(endDir)) {
			return b;
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			File beginFile = new File(beginFileName);
			File endFile = new File(endDir);

			if (!beginFile.exists()) {
				System.out.println("文件不存在！");
				throw new Exception("文件不存在！");
			} else {
				if (!endFile.exists()) {
					endFile.mkdirs();
				}
				if (!endDir.endsWith(beginFile.separator)) {
					endDir += beginFile.separator;
				}
				String str = endDir + beginFile.getName();
				endFile = new File(str);

				//如果文件名以存在，则黏贴的文件前加_
				if(endFile.exists()) {
					str = endDir + "_" + beginFile.getName();
					endFile = new File(str);
				}

				fis = new FileInputStream(beginFile);
				fos = new FileOutputStream(endFile);
				bis = new BufferedInputStream(fis);
				bos = new BufferedOutputStream(fos);

				byte[] buffer=new byte[1024*1024*8];
				int readByte = 0;
				while((readByte = bis.read(buffer)) != -1){
					bos.write(buffer, 0, readByte);
				}

				b = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeFile(bis, bos, null, null);
			logger.debug("<<<<<" + CLASSNAME + ".copyFileStream().");
			return b;
		}
	}

	/**
	 * 把文件复制到指定文件夹下，字符流方式
	 *
	 * @param beginFilename
	 * @param endFiles
	 * @return
	 */
	public static boolean copyFile(String beginFilename, String endFiles) {
		logger.debug(">>>>>" + CLASSNAME + ".copyFile().....");
		logger.debug("beginFilename:" + beginFilename);
		logger.debug("endFiles:" + endFiles);

		boolean b = false;
		BufferedReader br = null;
		BufferedWriter bw = null;
		Reader reader = null;
		Writer writer = null;

		try {
			File beginFile = new File(beginFilename);
			File endFile = new File(endFiles);

			if (!beginFile.exists()) {
				System.out.println("文件不存在！");
				throw new Exception("文件不存在！");
			} else {
				if (!endFile.exists()) {
					endFile.mkdirs();
				}
				if (!endFiles.endsWith("/") || !endFiles.endsWith("\\")) {
					endFiles += "/";
				}
				endFiles += beginFile.getName();
				endFile = new File(endFiles);

				reader = new FileReader(beginFile);
				br = new BufferedReader(reader);
				writer = new FileWriter(endFile);
				bw = new BufferedWriter(writer);

				char[] buffer = new char[1024*8];
				int readByte = 0;
				while((readByte = br.read(buffer)) != -1){
					bw.write(buffer, 0, readByte);
				}

				b = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeFile(null, null, br, bw);
			logger.debug("<<<<<" + CLASSNAME + ".copyFile().");
			return b;
		}
	}

	/**
	 * 把文件夹sourcePath复制到newPath文件夹下，返回复制文件夹情况（第一个成功复制文件个数，第二个失败复制个数，第三个字符串）
	 *
	 * @param sourcePath
	 * @param newPath
	 */
	public static List copyDir(String sourcePath, String newPath) {
		logger.debug(">>>>>" + CLASSNAME + ".copyDir().....");
		logger.debug("sourcePath:" + sourcePath);

		List list = new ArrayList();
		try {
			if(sourcePath == null || sourcePath.trim().length() <= 0 || newPath == null || newPath.trim().length() <= 0) {
				list.add("参数有为空或空字符串！");
				return list;
			}

			File file = new File(sourcePath);
			if(!file.isDirectory()) {
				list.add("sourcePath不是文件夹路径！");
				return list;
			}

			String[] filePath = file.list();
			newPath = newPath + file.separator + file.getName();
			File newFile = new File(newPath);
			if (!newFile.exists()) {
				newFile.mkdirs();
			}

			if(!newFile.isDirectory()) {
				list.add("newPath不是文件夹路径！");
				return list;
			}

			int ok = 0;
			List list2 = null;
			for (int i = 0; i < filePath.length; i++) {
				if ((new File(sourcePath + file.separator + filePath[i])).isDirectory()) {
					list2 = copyDir(sourcePath  + file.separator  + filePath[i], newPath);
					if(list2.size() > 0) {
						for(int j = 0; j < list2.size(); j++) {
							if(list2.get(0) instanceof String) {
								break;
							}
							if(j == 0) {
								ok = ok + (Integer) list2.get(j);
							}
						}
					}
				}

				if (new File(sourcePath  + file.separator + filePath[i]).isFile()) {
					boolean b = copyFileStream(sourcePath + file.separator + filePath[i], newPath + file.separator);
					if(b) {
						ok++;
					}
				}
			}

			String str = "成功复制文件" + ok + "个，失败" + ((Integer)getDirFileNumber(sourcePath).get(1) - ok) + "个！";
			list.add(ok);
			list.add((Integer) getDirFileNumber(sourcePath).get(1) - ok);
			list.add(str);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".copyDir().");
			return list;
		}
	}

	/**
	 * 关闭资源
	 *
	 * @param is
	 * @param os
	 * @param reader
	 * @param writer
	 */
	public static boolean closeFile(InputStream is, OutputStream os, Reader reader, Writer writer) {
		logger.debug(">>>>>" + CLASSNAME + ".closeFile().....");
		logger.debug("is:" + is);
		logger.debug("os:" + os);
		logger.debug("reader:" + reader);
		logger.debug("writer:" + writer);

		boolean b = false;
		try {
			if (os != null) {
				os.close();
				os = null;
			}
			if (is != null) {
				is.close();
				is = null;
			}
			if (writer != null) {
				writer.close();
				writer = null;
			}
			if (reader != null) {
				reader.close();
				reader = null;
			}

			if(os == null && is == null && writer == null && reader == null) {
				b = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".closeFile().");
			return b;
		}
	}

	/**
	 * 文件fileName是否以endsWith结尾的绝对路径，例如.xls、.xlsx
	 *
	 * @param fileName
	 * @param endsWith
	 * @return
	 */
	public static boolean isFileNameEndsWith(String fileName, String endsWith) {
		logger.debug(">>>>>" + CLASSNAME + ".isFileNameEndsWith().....");
		logger.debug("fileName:" + fileName);
		logger.debug("endsWith:" + endsWith);

		boolean b = false;
		try {
			//如果传入的参数为空或者为空字符串空格，则返回false
			if (fileName == null || fileName.trim().length() <= 0
					|| endsWith == null || endsWith.trim().length() <= 0) {
				return b;
			}

			if(!endsWith.contains(".")) {
				endsWith = "." + endsWith;
			}

			//判断文件字符串是否为检验的字符串结尾
			if (fileName.endsWith(endsWith)) {
				//选择除去结尾的字符串
				String[] split = fileName.split(endsWith);
				if (split.length > 0) {
					String str = split[0];
					//判断是否为绝对路径
					if (str.contains(":")) {
						String[] split2 = str.split(":");
						//判断是否是盘符
						if (split2.length > 1 && split2[0].length() == 1
								&& split2[0].equals(str.substring(0, 1))) {
							String pan = split2[0];
							String fileName2 = pan + "test_test";
							logger.debug("fileName2:" + fileName2);

							File file = new File(fileName2);
							//测试是否能创建文件夹成功
							while (file.exists()) {
								fileName2 = fileName2 + 1;
								file = new File(fileName2);
							}

							boolean b2 = file.mkdir();
							if (b2) {
								logger.debug("测试创建文件夹成功");
								b2 = file.delete();
								logger.debug("是否删除测试文件夹:" + b2);

								String str2 = split2[1];
								String[] split3 = str2.split("/");
								List<String> list = new ArrayList<String>();
								for (int i = 0; i < split3.length; i++) {
									String[] split4 = split3[i].split("\\\\");
									for (int j = 0; j < split4.length; j++) {
										list.add(split4[j]);
									}
								}

								boolean b3 = false;
								for (int i = 0; i < list.size(); i++) {
									// \ / : * ? # ” < > |
									String str3 = list.get(i);
									//判断文件夹、文件命名是否符合规范
									if (str3.contains("\\")
											|| str3.contains("/")
											|| str3.contains(":")
											|| str3.contains("*")
											|| str3.contains("?")
											|| str3.contains("#")
											|| str3.contains("\"")
											|| str3.contains("<")
											|| str3.contains(">")
											|| str3.contains(">")
											|| str3.contains("|")
											|| str3.contains(" ")
											|| str3.length() >= 255) {
										b3 = true;
									}
								}
								b = !b3;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".isFileNameEndsWith().");
			return b;
		}
	}

	/**
	 * 统计文件夹里面的文件夹和文件个数，正常情况（第一个放文件夹个数，第二个放文件个数，第三个放字符串），失败情况（第一个为字符串）
	 *
	 * @param strDirectoryName
	 * @return
	 */
	public static List getDirFileNumber(String strDirectoryName) {
		logger.debug(">>>>>" + CLASSNAME + ".getDirFileNumber().....");
		logger.debug("strDirectoryName:" + strDirectoryName);
		List list = new ArrayList();
		try {
			if(strDirectoryName == null || strDirectoryName.trim().length() <= 0) {
				String str = "参数有为空或空字符串！";
				list .add(str);
				return list;
			}

			File file = new File(strDirectoryName);
			if(!file.isDirectory()) {
				list.add("strDirectoryName不是文件夹路径！");
				return list;
			}

			String[] filePath = file.list();

			List list2 = new ArrayList();
			int isDir = 0;
			int isFile = 0;

			for (int i = 0; i < filePath.length; i++) {
				if ((new File(strDirectoryName + file.separator + filePath[i])).isDirectory()) {
					list2 = getDirFileNumber(strDirectoryName  + file.separator  + filePath[i]);
					if(list2.size() > 0) {
						isDir++;
						for(int j = 0; j < list2.size(); j++) {
							if(list2.get(0) instanceof String) {
								break;
							}
							if(j == 0) {
								isDir = isDir + (Integer)list2.get(j);
							} else if(j == 1) {
								isFile = isFile + (Integer)list2.get(j);
							}
						}
					}
				}

				if (new File(strDirectoryName  + file.separator + filePath[i]).isFile()) {
					isFile++;
				}

			}
			String str = "文件夹" + isDir + "个，文件" + isFile + "个";
			list.add(isDir);
			list.add(isFile);
			list.add(str);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".getDirFileNumber().");
			return list;
		}
	}

	/**
	 * 获取文件大小，以字符串返回，如果不是文件则返回参数
	 *
	 * @param fileName
	 * @return
	 */
	public static String getFileSizeString(String fileName) {
		logger.debug(">>>>>" + CLASSNAME + ".getFileSizeString().....");
		logger.debug("fileName:" + fileName);

		String str = "";
		try {
			if(fileName == null || fileName.trim().length() <= 0) {
				return fileName;
			}

			File file = new File(fileName);
			if(!file.isFile()) {
				return fileName;
			}

			long length = file.length();
			if(length < 1024L) {
				str = str + length + "B";
			} else if(length < 1024*1024L) {
				str = str + (length/1024) + "K" + (length%1024) + "B";
			} else if(length < 1024*1024*1024L) {
				str = str + (length/1024/1024) + "M" + (length/1024%1024) + "K" + (length%1024) + "B";
			} else if(length < 1024*1024*1024*1024L) {
				str = str + (length/1024/1024/1024) + "G" + (length/1024/1024%1024) + "M" + (length/1024/1024%1024) + "K" + (length%1024) + "B";
			} else {
				str = str + (length/1024/1024/1024/1024) + "T" + (length/1024/1024/1024%1024) + "G" + (length/1024/1024%1024) + "M" + (length/1024%1024) + "K" + (length%1024) + "B";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".getFileSizeString().");
			return str;
		}
	}

	/**
	 * 把指定类型的文件复制到目标文件夹下，正常情况（第一个放文件个数，第二个放复制文件成功个数，第三个放字符串），失败情况（第一个为字符串）
	 *
	 * @param beginFileName
	 * @param endDir
	 * @param fileTpye
	 * @return
	 */
	public static List copyDirTpye(String beginFileName, String endDir, String fileTpye) {
		logger.debug(">>>>>" + CLASSNAME + ".copyDirTpye().....");
		logger.debug("beginFileName:" + beginFileName);
		logger.debug("endDir:" + endDir);
		logger.debug("fileTpye:" + fileTpye);

		List list = new ArrayList();
		String str = "";
		try {
			if(beginFileName == null || beginFileName.trim().length() <= 0 || endDir == null || endDir.trim().length() <= 0) {
				str = "参数" + beginFileName + "为空或空字符串！";
				list.add(str);
				return list;
			}

			File beginFile = new File(beginFileName);
			File endFile = new File(endDir);

			if(!beginFile.exists() || !beginFile.isDirectory()) {
				str = "参数" + beginFileName + "不是文件夹";
				list.add(str);
				return list;
			}

			if(fileTpye == null || fileTpye.trim().length() <= 0) {
				str = "参数" + fileTpye + "为空或空字符串";
				list.add(str);
				return list;
			}

			if(!endFile.exists()) {
				endFile.mkdirs();
			}

			if(!endFile.exists() || !endFile.isDirectory()) {
				str = "参数" + endDir + "不是文件夹";
				list.add(str);
				return list;
			}

			if(!fileTpye.contains(".")) {
				fileTpye = "." + fileTpye;
			}

			String[] files = beginFile.list();

			int number = 0;
			int ok = 0;
			for(int i = 0; i < files.length; i++) {
				if(files[i].endsWith(fileTpye)) {
					number++;
					boolean b = copyFileStream(beginFileName + beginFile.separator + files[i], endDir + beginFile.separator + beginFile.getName());
					if(b) {
						ok++;
					}
				}
			}

			if(number == 0) {
				str = "文件夹" + beginFileName + "里没有类型为" + fileTpye + "的文件！";
				list.add(number);
				list.add(str);
				return list;
			}

			str = "一共有" + number + "个" + fileTpye + "类型的文件，成功复制" + ok + "个文件，失败" + (number-ok) + "个";

			list.add(number);
			list.add(ok);
			list.add(str);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".copyDirTpye().");
			return list;
		}
	}

	/**
	 * 把指定多个类型的文件复制到目标文件夹下，正常情况（第一个放文件个数，第二个放复制文件成功个数，第三个放字符串），失败情况（第一个为字符串）
	 *
	 * @param beginFileName
	 * @param endDir
	 * @param fileTpyes
	 * @return
	 */
	public static List copyDirTpyes(String beginFileName, String endDir, List<String> fileTpyes) {
		logger.debug(">>>>>" + CLASSNAME + ".copyDirTpyes().....");
		logger.debug("beginFileName:" + beginFileName);
		logger.debug("endDir:" + endDir);
		logger.debug("fileTpyes:" + fileTpyes);

		List list = new ArrayList();
		String str = "";
		try {
			if(beginFileName == null || beginFileName.trim().length() <= 0 || endDir == null || endDir.trim().length() <= 0) {
				str = "参数" + beginFileName + "为空或空字符串！";
				list.add(str);
				return list;
			}

			File beginFile = new File(beginFileName);
			File endFile = new File(endDir);

			if(!beginFile.exists() || !beginFile.isDirectory()) {
				str = "参数" + beginFileName + "不是文件夹";
				list.add(str);
				return list;
			}

			if(!endFile.exists()) {
				endFile.mkdirs();
			}

			if(!endFile.exists() || !endFile.isDirectory()) {
				str = "参数" + endDir + "不是文件夹";
				list.add(str);
				return list;
			}

			List list2 = null;
			int number = 0;
			int ok = 0;
			for(int i = 0; i < fileTpyes.size(); i++) {
				list2 = copyDirTpye(beginFileName, endDir, fileTpyes.get(i));
				if(list2.size() > 2) {
					number = number + (Integer) list2.get(0);
					ok = ok + (Integer)list2.get(1);
				}
			}

			if(number == 0) {
				str = "文件夹" + beginFileName + "里没有类型为" + fileTpyes + "的文件！";
				list.add(number);
				list.add(str);
				return list;
			}

			str = "一共有" + number + "个" + fileTpyes +"类型的文件，成功复制" + ok + "个文件，失败" + (number-ok) + "个";
			list.add(number);
			list.add(ok);
			list.add(str);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".copyDirTpyes().");
			return list;
		}
	}

	/**
	 * 把指定多个类型的文件复制到目标文件夹下，正常情况（第一个放文件个数，第二个放复制文件成功个数，第三个放字符串），失败情况（第一个为字符串）
	 *
	 * @param beginFileName
	 * @param endDir
	 * @param fileTpyes
	 * @return
	 */
	public static List copyDirTpyes(String beginFileName, String endDir, String[] fileTpyes) {
		List list = new ArrayList();
		List<String> listTpyes = new ArrayList<String>();

		if(fileTpyes == null || fileTpyes.length <= 0) {
			list.add("参数" + fileTpyes + "为空或没有类型！");
			return list;
		}

		for(int i = 0; i < fileTpyes.length; i++) {
			listTpyes.add(fileTpyes[i]);
		}

		list = copyDirTpyes(beginFileName, endDir, listTpyes);
		return list;
	}

	/**
	 * 把文件夹所有【包括子文件夹】指定类型的文件复制到目标文件夹【子文件夹一一对应】下，正常情况（第一个放文件个数，第二个放复制文件成功个数，第三个放字符串），失败情况（第一个为字符串）
	 * @param beginFileName
	 * @param endDir
	 * @param fileTpye
	 * @return
	 */
	public static List copyDirTpyeAll(String beginFileName, String endDir, String fileTpye) {
		logger.debug(">>>>>" + CLASSNAME + ".copyDirTpyeAll().....");
		logger.debug("beginFileName:" + beginFileName);
		logger.debug("endDir:" + endDir);
		logger.debug("fileTpye:" + fileTpye);
		List list = new ArrayList();
		String str = "";
		try {
			if(beginFileName == null || beginFileName.trim().length() <= 0 || endDir == null || endDir.trim().length() <= 0) {
				str = "参数" + beginFileName + "为空或空字符串！";
				list.add(str);
				return list;
			}

			File beginFile = new File(beginFileName);
			File endFile = new File(endDir);

			if(!beginFile.exists() || !beginFile.isDirectory()) {
				str = "参数" + beginFileName + "不是文件夹";
				list.add(str);
				return list;
			}

			if(fileTpye == null || fileTpye.trim().length() <= 0) {
				str = "参数" + fileTpye + "为空或空字符串";
				list.add(str);
				return list;
			}

			if(!endFile.exists()) {
				endFile.mkdirs();
			}

			if(!endFile.exists() || !endFile.isDirectory()) {
				str = "参数" + endDir + "不是文件夹";
				list.add(str);
				return list;
			}

			if(!fileTpye.contains(".")) {
				fileTpye = "." + fileTpye;
			}

			String[] files = beginFile.list();

			int number = 0;
			int ok = 0;
			for(int i = 0; i < files.length; i++) {
				List list2 = new ArrayList();
				if(files[i].endsWith(fileTpye)) {
					number++;
					boolean b = copyFileStream(beginFileName + beginFile.separator + files[i], endDir + beginFile.separator + beginFile.getName());
					if(b) {
						ok++;
					}
				} else if(new File(beginFileName + beginFile.separator + files[i]).isDirectory()) {
					list2 = copyDirTpyeAll(beginFileName + beginFile.separator + files[i], endDir + beginFile.separator + files[i], fileTpye);
					if(list2.size() > 2) {
						number = number + (Integer)list2.get(0);
						ok = ok + (Integer)list2.get(1);
					}
				}
			}

			if(number == 0) {
				str = "文件夹" + beginFileName + "里没有类型为" + fileTpye + "的文件！";
				list.add(number);
				list.add(str);
				return list;
			}

			str = "一共有" + number + "个" + fileTpye + "类型的文件，成功复制" + ok + "个文件，失败" + (number-ok) + "个";

			list.add(number);
			list.add(ok);
			list.add(str);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".copyDirTpyeAll().");
			return list;
		}
	}

	/**
	 * 把文件夹所有【包括子文件夹】指定类型的文件复制到目标文件夹【就在此文件夹】下，正常情况（第一个放文件个数，第二个放复制文件成功个数，第三个放字符串），失败情况（第一个为字符串）
	 * @param beginFileName
	 * @param inDir
	 * @param fileTpye
	 * @return
	 */
	public static List copyDirTpyeAllInDir(String beginFileName, String inDir, String fileTpye) {
		logger.debug(">>>>>" + CLASSNAME + ".copyDirTpyeAllInDir().....");
		logger.debug("beginFileName:" + beginFileName);
		logger.debug("endDir:" + inDir);
		logger.debug("fileTpye:" + fileTpye);
		List list = new ArrayList();
		String str = "";
		try {
			if(beginFileName == null || beginFileName.trim().length() <= 0 || inDir == null || inDir.trim().length() <= 0) {
				str = "参数" + beginFileName + "为空或空字符串！";
				list.add(str);
				return list;
			}

			File beginFile = new File(beginFileName);
			File endFile = new File(inDir);

			if(!beginFile.exists() || !beginFile.isDirectory()) {
				str = "参数" + beginFileName + "不是文件夹";
				list.add(str);
				return list;
			}

			if(fileTpye == null || fileTpye.trim().length() <= 0) {
				str = "参数" + fileTpye + "为空或空字符串";
				list.add(str);
				return list;
			}

			if(!endFile.exists()) {
				endFile.mkdirs();
			}

			if(!endFile.exists() || !endFile.isDirectory()) {
				str = "参数" + inDir + "不是文件夹";
				list.add(str);
				return list;
			}

			if(!fileTpye.contains(".")) {
				fileTpye = "." + fileTpye;
			}

			String[] files = beginFile.list();

			int number = 0;
			int ok = 0;
			for(int i = 0; i < files.length; i++) {
				List list2 = new ArrayList();
				if(files[i].endsWith(fileTpye)) {
					number++;
					boolean b = copyFileStream(beginFileName + beginFile.separator + files[i], inDir + beginFile.separator + beginFile.getName());
					if(b) {
						ok++;
					}
				} else if(new File(beginFileName + beginFile.separator + files[i]).isDirectory()) {
					list2 = copyDirTpyeAll(beginFileName + beginFile.separator + files[i], inDir, fileTpye);
					if(list2.size() > 2) {
						number = number + (Integer)list2.get(0);
						ok = ok + (Integer)list2.get(1);
					}
				}
			}

			if(number == 0) {
				str = "文件夹" + beginFileName + "里没有类型为" + fileTpye + "的文件！";
				list.add(number);
				list.add(str);
				return list;
			}

			str = "一共有" + number + "个" + fileTpye + "类型的文件，成功复制" + ok + "个文件，失败" + (number-ok) + "个";

			list.add(number);
			list.add(ok);
			list.add(str);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".copyDirTpyeAllInDir().");
			return list;
		}
	}

	/**
	 * 把文件夹所有【包括子文件夹】多个类型的文件复制到目标文件夹【子文件夹一一对应】下，正常情况（第一个放文件个数，第二个放复制文件成功个数，第三个放字符串），失败情况（第一个为字符串）
	 *
	 * @param beginFileName
	 * @param endDir
	 * @param fileTpyes
	 * @return
	 */
	public static List copyDirTpyesAll(String beginFileName, String endDir, List<String> fileTpyes) {
		logger.debug(">>>>>" + CLASSNAME + ".copyDirTpyesAll().....");
		logger.debug("beginFileName:" + beginFileName);
		logger.debug("endDir:" + endDir);
		logger.debug("fileTpyes:" + fileTpyes);

		List list = new ArrayList();
		String str = "";
		try {
			if(beginFileName == null || beginFileName.trim().length() <= 0 || endDir == null || endDir.trim().length() <= 0) {
				str = "参数" + beginFileName + "为空或空字符串！";
				list.add(str);
				return list;
			}

			File beginFile = new File(beginFileName);
			File endFile = new File(endDir);

			if(!beginFile.exists() || !beginFile.isDirectory()) {
				str = "参数" + beginFileName + "不是文件夹";
				list.add(str);
				return list;
			}

			if(!endFile.exists()) {
				endFile.mkdirs();
			}

			if(!endFile.exists() || !endFile.isDirectory()) {
				str = "参数" + endDir + "不是文件夹";
				list.add(str);
				return list;
			}

			List list2 = null;
			int number = 0;
			int ok = 0;
			for(int i = 0; i < fileTpyes.size(); i++) {
				list2 = copyDirTpyeAll(beginFileName, endDir, fileTpyes.get(i));
				if(list2.size() > 2) {
					number = number + (Integer) list2.get(0);
					ok = ok + (Integer)list2.get(1);
				}
			}

			if(number == 0) {
				str = "文件夹" + beginFileName + "里没有类型为" + fileTpyes + "的文件！";
				list.add(number);
				list.add(str);
				return list;
			}

			str = "一共有" + number + "个" + fileTpyes +"类型的文件，成功复制" + ok + "个文件，失败" + (number-ok) + "个";
			list.add(number);
			list.add(ok);
			list.add(str);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".copyDirTpyesAll().");
			return list;
		}
	}

	/**
	 * 把文件夹所有【包括子文件夹】多个类型的文件复制到目标文件夹【就在此文件夹】下，正常情况（第一个放文件个数，第二个放复制文件成功个数，第三个放字符串），失败情况（第一个为字符串）
	 *
	 * @param beginFileName
	 * @param inDir
	 * @param fileTpyes
	 * @return
	 */
	public static List copyDirTpyesAllInDir(String beginFileName, String inDir, List<String> fileTpyes) {
		logger.debug(">>>>>" + CLASSNAME + ".copyDirTpyesAllInDir().....");
		logger.debug("beginFileName:" + beginFileName);
		logger.debug("endDir:" + inDir);
		logger.debug("fileTpyes:" + fileTpyes);

		List list = new ArrayList();
		String str = "";
		try {
			if(beginFileName == null || beginFileName.trim().length() <= 0 || inDir == null || inDir.trim().length() <= 0) {
				str = "参数" + beginFileName + "为空或空字符串！";
				list.add(str);
				return list;
			}

			File beginFile = new File(beginFileName);
			File endFile = new File(inDir);

			if(!beginFile.exists() || !beginFile.isDirectory()) {
				str = "参数" + beginFileName + "不是文件夹";
				list.add(str);
				return list;
			}

			if(!endFile.exists()) {
				endFile.mkdirs();
			}

			if(!endFile.exists() || !endFile.isDirectory()) {
				str = "参数" + inDir + "不是文件夹";
				list.add(str);
				return list;
			}

			List list2 = null;
			int number = 0;
			int ok = 0;
			for(int i = 0; i < fileTpyes.size(); i++) {
				list2 = copyDirTpyeAllInDir(beginFileName, inDir, fileTpyes.get(i));
				if(list2.size() > 2) {
					number = number + (Integer) list2.get(0);
					ok = ok + (Integer)list2.get(1);
				}
			}

			if(number == 0) {
				str = "文件夹" + beginFileName + "里没有类型为" + fileTpyes + "的文件！";
				list.add(number);
				list.add(str);
				return list;
			}

			str = "一共有" + number + "个" + fileTpyes +"类型的文件，成功复制" + ok + "个文件，失败" + (number-ok) + "个";
			list.add(number);
			list.add(ok);
			list.add(str);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".copyDirTpyesAllInDir().");
			return list;
		}
	}

	/**
	 * 把文件夹所有【包括子文件夹】多个类型的文件复制到目标文件夹【子文件夹一一对应】下，正常情况（第一个放文件个数，第二个放复制文件成功个数，第三个放字符串），失败情况（第一个为字符串）
	 *
	 * @param beginFileName
	 * @param endDir
	 * @param fileTpyes
	 * @return
	 */
	public static List copyDirTpyesAll(String beginFileName, String endDir, String[] fileTpyes) {
		List list = new ArrayList();
		List<String> listTpyes = new ArrayList<String>();

		if(fileTpyes == null || fileTpyes.length <= 0) {
			list.add("参数" + fileTpyes + "为空或没有类型！");
			return list;
		}

		for(int i = 0; i < fileTpyes.length; i++) {
			listTpyes.add(fileTpyes[i]);
		}

		list = copyDirTpyesAll(beginFileName, endDir, listTpyes);
		return list;
	}

	/**
	 * 把文件夹所有【包括子文件夹】多个类型的文件复制到目标文件夹【就在此文件夹】下，正常情况（第一个放文件个数，第二个放复制文件成功个数，第三个放字符串），失败情况（第一个为字符串）
	 *
	 * @param beginFileName
	 * @param inDir
	 * @param fileTpyes
	 * @return
	 */
	public static List copyDirTpyesAllInDir(String beginFileName, String inDir, String[] fileTpyes) {
		List list = new ArrayList();
		List<String> listTpyes = new ArrayList<String>();

		if(fileTpyes == null || fileTpyes.length <= 0) {
			list.add("参数" + fileTpyes + "为空或没有类型！");
			return list;
		}

		for(int i = 0; i < fileTpyes.length; i++) {
			listTpyes.add(fileTpyes[i]);
		}

		list = copyDirTpyesAllInDir(beginFileName, inDir, listTpyes);
		return list;
	}

	/**
	 * 删除文件夹，包含子文件夹
	 *
	 * @param strDir
	 */
	public static void removeDir(String strDir) {
		logger.debug(">>>>>" + CLASSNAME + ".removeDir().....");
		logger.debug("strDir:" + strDir);

		try {
			if(strDir == null || strDir.trim().length() <= 0){
				return ;
			}

			File file = new File(strDir);
			if(!file.exists()) {
				return ;
			}

			if(!file.isDirectory()) {
				return ;
			}

			String[] fileList = file.list();
			for(int i = 0; i < fileList.length; i++) {
				File file2 = new File(strDir + file.separator + fileList[i]);
				if(file2.isDirectory()) {
					removeDir(strDir + file.separator + fileList[i]);
					file2.delete();
				} else if(file2.isFile()) {
					file2.delete();
				}
			}
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".removeDir().");
		}
	}

    /**
     * @desc 获取文件夹下所有文件的绝对路径
     * @param file
     * @return
     */
	public static List<String> getFileNames(File file) {
		if(file == null || !file.isDirectory()) {
			return null;
		}

		List<String> list = new ArrayList<>();
		String[] files = file.list();
		for(int i = 0; i < files.length; i++) {
            File file1 = new File(file.getPath() + File.separator + files[i]);
            if(file1.exists() && file1.isDirectory()) {
                List<String> fileNames = getFileNames(file1);
                if(fileNames != null && fileNames.size() > 0) {
                    list.addAll(fileNames);
                }
            } else if(file1.exists() && file1.isFile()) {
                list.add(file.getPath() + File.separator + file1.getName());
            }
		}
		return list;
	}

    /**
     * @desc 获取文件夹下所有文件的绝对路径
     * @param fileNames
     * @return
     */
    public static List<String> getFileNames(String fileNames) {
	    if(fileNames == null || fileNames.length() <= 0) {
	        return null;
        }

        File file = new File(fileNames);
	    if(!file.exists() || !file.isDirectory()) {
	        return null;
        }

        return getFileNames(file);
    }

    /**
     * @desc 判断两个文件是否相同
     * @param file1
     * @param file2
     * @return
     */
    public static boolean equalsFile(File file1, File file2) {
        if(file1 == null || file2 == null || !file1.isFile() || !file2.isFile()) {
            return false;
        }

        int length1 = (int) file1.length();
        int length2 = (int) file2.length();
        if(length1 != length2) {
            return false;
        }

        FileInputStream fis1 = null;
        FileInputStream fis2 = null;
        boolean b = true;
        try {
            fis1 = new FileInputStream(file1);
            fis2 = new FileInputStream(file2);
            byte[] data1 = new byte[length1];
            byte[] data2 = new byte[length2];
            fis1.read(data1);
            fis2.read(data2);
            for (int i=0; i<length1; i++) {
                //只要有一个字节不同，两个文件就不一样
                if (data1[i] != data2[i]) {
                    b = false;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeFile(fis1, null, null, null);
            closeFile(fis2, null, null, null);
            return b;
        }
    }

    /**
     * @desc 判断两个文件是否相同
     * @param fileName1
     * @param fileName2
     * @return
     */
    public static boolean equalsFile(String fileName1, String fileName2) {
        if(fileName1 == null || fileName2 == null || fileName1.length() <= 0 || fileName2.length() <= 0) {
            return false;
        }

        return equalsFile(new File(fileName1), new File(fileName2));
    }

    /**
     * @desc 把文件夹里相同的文件列出来，相同的放在一个List
     * @param file
     * @return
     */
    public static List<List<String>> getRepetitionFiles(File file) {
		if(file == null || !file.isDirectory()) {
			return null;
		}

        List<String> fileNames = getFileNames(file);
        if(fileNames == null || fileNames.size() <= 0) {
            return null;
        }

        List<List<String>> arrayLists = new ArrayList<>();

        int size = fileNames.size();

        List<Integer> subscript = new ArrayList<>();
        List<String> list = null;
        for(int i = 0; i < size; i++) {
            if(subscript.contains(i)) {//如果前面有相同了，就跳过此文件
                continue;
            }
            String fileName = fileNames.get(i);
            list = new ArrayList<>();
            list.add(fileName);
            subscript.add(i);
            for(int j = i + 1; j < size; j++) {
                String fileName2 = fileNames.get(j);
                if(equalsFile(fileName, fileName2)) {
                    list.add(fileName2);
                    subscript.add(j);
                }
            }
            if(list.size() > 1) {
                arrayLists.add(list);
            }
        }

        return arrayLists;
	}

    /**
     * @desc 把文件夹里相同的文件列出来，相同的放在一个List
     * @param fileName
     * @return
     */
    public static List<List<String>> getRepetitionFiles(String fileName) {
        if(fileName == null || fileName.length() <= 0) {
            return null;
        }

        return getRepetitionFiles(new File(fileName));
    }

	public static Boolean checkDir(String filePath) {
    	if(null == filePath || filePath.trim().length() <= 0) {
    		return false;
		}

		File file = new File(filePath);
    	if(file.isFile()) {
    		return false;
		}

		if(!file.exists()){
			file.mkdirs();
		}

		return true;
	}

	//删除文件夹下指定的类型文件
	//删除文件夹下指定的多个类型文件
	//删除文件夹下指定的类型文件，包含文件夹下的文件夹
	//删除文件夹下指定的多个类型文件，包含文件夹下的文件夹

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//boolean b = copyFile("E:/test/test.txt", "d:/test\\");
		//boolean b = copyFileStream("E:/test/cn_windows_7_professional_x64_dvd_x15-65791.iso", "d:/test\\");
		//System.out.println(b);

		/*List list = copyDir("E:/test", "d:/test");
		//List list = getDirFileNumber("E:/test");
		for(int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}*/

		/*String fileSize = getFileSizeString("E:\\个人文件\\softwart\\system\\windows\\cn_windows_7_professional_x64_dvd_x15-65791.iso");
		System.out.println(fileSize);*/

		//copyDirTpye("E:/test", "d:/test", "txt");
		//copyDirTpyeAll("E:/test", "d:/test", "txt");
		String[] str = {"txt","xls"};
		//copyDirTpyesAll("E:/test", "d:/test", str);
		copyDirTpyesAllInDir("E:/test", "d:/test", str);
		//removeDir("D:\\test\\test");

	}
}
