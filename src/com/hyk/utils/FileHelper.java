package com.hyk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;

public class FileHelper {
	private Context context;
	private String SDPATH;
	private String FILESPATH;

	public FileHelper(Context context) {
		this.context = context;
		SDPATH = Environment.getExternalStorageDirectory().getPath() + "//";
		FILESPATH = this.context.getFilesDir().getPath() + "//";
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 删除SD卡上的文件
	 * 
	 * @param fileName
	 */
	public boolean delSDFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file == null || !file.exists() || file.isDirectory())
			return false;
		file.delete();
		return true;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public String creatSDDir(String dirName) {
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (!sdCardExist) {
			// 如果不存在SD卡，进行提示
			ToastUtil.show(context, CHString.NOT_SDKA);
			return null;
		} else {
			File dir = new File(SDPATH + dirName);
			if (!dir.exists()) {
				dir.mkdir();
			}
			return dir.getAbsolutePath();
		}
	}

	/**
	 * 删除SD卡上的目录
	 * 
	 * @param dirName
	 */
	public boolean delSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		return delDir(dir);
	}

	/**
	 * 创建二级目录
	 * 
	 * @param firstName
	 *            一级目录
	 * @param secondFile
	 *            二级目录
	 * @return
	 */
	public String dirSecondFile(String firstName, String secondFile) {
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (!sdCardExist) {
			// 如果不存在SD卡，进行提示
			ToastUtil.show(context, CHString.NOT_SDKA);
			return null;
		} else {
			File dir = new File(SDPATH + firstName);
			if (!dir.exists()) {
				dir.mkdir();
			}
			File dirSecondFile = new File(SDPATH + firstName + "//" + secondFile);// 新建二级主目录
			if (!dirSecondFile.exists()) {// 判断文件夹目录是否存在

				dirSecondFile.mkdir();// 如果不存在则创建

			}
			return dirSecondFile.getAbsolutePath();
		}

	}

	/**
	 * 修改SD卡上的文件或目录名
	 * 
	 * @param fileName
	 */
	public boolean renameSDFile(String oldfileName, String newFileName) {
		File oleFile = new File(SDPATH + oldfileName);
		File newFile = new File(SDPATH + newFileName);
		return oleFile.renameTo(newFile);
	}

	/**
	 * 拷贝SD卡上的单个文件
	 * 
	 * @param path
	 * @throws IOException
	 */
	public boolean copySDFileTo(String srcFileName, String destFileName) throws IOException {
		File srcFile = new File(SDPATH + srcFileName);
		File destFile = new File(SDPATH + destFileName);
		return copyFileTo(srcFile, destFile);
	}

	/**
	 * 拷贝SD卡上指定目录的所有文件
	 * 
	 * @param srcDirName
	 * @param destDirName
	 * @return
	 * @throws IOException
	 */
	public boolean copySDFilesTo(String srcDirName, String destDirName) throws IOException {
		File srcDir = new File(SDPATH + srcDirName);
		File destDir = new File(SDPATH + destDirName);
		return copyFilesTo(srcDir, destDir);
	}

	/**
	 * 移动SD卡上的单个文件
	 * 
	 * @param srcFileName
	 * @param destFileName
	 * @return
	 * @throws IOException
	 */
	public boolean moveSDFileTo(String srcFileName, String destFileName) throws IOException {
		File srcFile = new File(SDPATH + srcFileName);
		File destFile = new File(SDPATH + destFileName);
		return moveFileTo(srcFile, destFile);
	}

	/**
	 * 移动SD卡上的指定目录的所有文件
	 * 
	 * @param srcDirName
	 * @param destDirName
	 * @return
	 * @throws IOException
	 */
	public boolean moveSDFilesTo(String srcDirName, String destDirName) throws IOException {
		File srcDir = new File(SDPATH + srcDirName);
		File destDir = new File(SDPATH + destDirName);
		return moveFilesTo(srcDir, destDir);
	}

	/**
	 * 建立私有文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File creatDataFile(String fileName) throws IOException {
		File file = new File(FILESPATH + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 建立私有目录
	 * 
	 * @param dirName
	 * @return
	 */
	public File creatDataDir(String dirName) {
		File dir = new File(FILESPATH + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 删除私有文件
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean delDataFile(String fileName) {
		File file = new File(FILESPATH + fileName);
		return delFile(file);
	}

	/**
	 * 删除私有目录
	 * 
	 * @param dirName
	 * @return
	 */
	public boolean delDataDir(String dirName) {
		File file = new File(FILESPATH + dirName);
		return delDir(file);
	}

	/**
	 * 更改私有文件名
	 * 
	 * @param oldName
	 * @param newName
	 * @return
	 */
	public boolean renameDataFile(String oldName, String newName) {
		File oldFile = new File(FILESPATH + oldName);
		File newFile = new File(FILESPATH + newName);
		return oldFile.renameTo(newFile);
	}

	/**
	 * 在私有目录下进行文件复制
	 * 
	 * @param srcFileName
	 *            ： 包含路径及文件名
	 * @param destFileName
	 * @return
	 * @throws IOException
	 */
	public boolean copyDataFileTo(String srcFileName, String destFileName) throws IOException {
		File srcFile = new File(FILESPATH + srcFileName);
		File destFile = new File(FILESPATH + destFileName);
		return copyFileTo(srcFile, destFile);
	}

	/**
	 * 复制私有目录里指定目录的所有文件
	 * 
	 * @param srcDirName
	 * @param destDirName
	 * @return
	 * @throws IOException
	 */
	public boolean copyDataFilesTo(String srcDirName, String destDirName) throws IOException {
		File srcDir = new File(FILESPATH + srcDirName);
		File destDir = new File(FILESPATH + destDirName);
		return copyFilesTo(srcDir, destDir);
	}

	/**
	 * 移动私有目录下的单个文件
	 * 
	 * @param srcFileName
	 * @param destFileName
	 * @return
	 * @throws IOException
	 */
	public boolean moveDataFileTo(String srcFileName, String destFileName) throws IOException {
		File srcFile = new File(FILESPATH + srcFileName);
		File destFile = new File(FILESPATH + destFileName);
		return moveFileTo(srcFile, destFile);
	}

	/**
	 * 移动私有目录下的指定目录下的所有文件
	 * 
	 * @param srcDirName
	 * @param destDirName
	 * @return
	 * @throws IOException
	 */
	public boolean moveDataFilesTo(String srcDirName, String destDirName) throws IOException {
		File srcDir = new File(FILESPATH + srcDirName);
		File destDir = new File(FILESPATH + destDirName);
		return moveFilesTo(srcDir, destDir);
	}

	/**
	 * 删除一个文件
	 * 
	 * @param file
	 * @return
	 */
	public boolean delFile(File file) {
		if (file.isDirectory())
			return false;
		return file.delete();
	}

	/**
	 * 删除一个目录（可以是非空目录）
	 * 
	 * @param dir
	 */
	public boolean delDir(File dir) {
		if (dir == null || !dir.exists() || dir.isFile()) {
			return false;
		}
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				delDir(file);// 递归
			}
		}
		dir.delete();
		return true;
	}

	/**
	 * 拷贝一个文件,srcFile源文件，destFile目标文件
	 * 
	 * @param path
	 * @throws IOException
	 */
	public boolean copyFileTo(File srcFile, File destFile) throws IOException {
		if (srcFile.isDirectory() || destFile.isDirectory())
			return false;// 判断是否是文件
		FileInputStream fis = new FileInputStream(srcFile);
		FileOutputStream fos = new FileOutputStream(destFile);
		int readLen = 0;
		byte[] buf = new byte[1024];
		while ((readLen = fis.read(buf)) != -1) {
			fos.write(buf, 0, readLen);
		}
		fos.flush();
		fos.close();
		fis.close();
		return true;
	}

	/**
	 * 拷贝目录下的所有文件到指定目录
	 * 
	 * @param srcDir
	 * @param destDir
	 * @return
	 * @throws IOException
	 */
	public boolean copyFilesTo(File srcDir, File destDir) throws IOException {
		if (!srcDir.isDirectory() || !destDir.isDirectory())
			return false;// 判断是否是目录
		if (!destDir.exists())
			return false;// 判断目标目录是否存在
		File[] srcFiles = srcDir.listFiles();
		for (int i = 0; i < srcFiles.length; i++) {
			if (srcFiles[i].isFile()) {
				// 获得目标文件
				File destFile = new File(destDir.getPath() + "//" + srcFiles[i].getName());
				copyFileTo(srcFiles[i], destFile);
			} else if (srcFiles[i].isDirectory()) {
				File theDestDir = new File(destDir.getPath() + "//" + srcFiles[i].getName());
				copyFilesTo(srcFiles[i], theDestDir);
			}
		}
		return true;
	}

	/**
	 * 移动一个文件
	 * 
	 * @param srcFile
	 * @param destFile
	 * @return
	 * @throws IOException
	 */
	public boolean moveFileTo(File srcFile, File destFile) throws IOException {
		boolean iscopy = copyFileTo(srcFile, destFile);
		if (!iscopy)
			return false;
		delFile(srcFile);
		return true;
	}

	/**
	 * 移动目录下的所有文件到指定目录
	 * 
	 * @param srcDir
	 * @param destDir
	 * @return
	 * @throws IOException
	 */
	public boolean moveFilesTo(File srcDir, File destDir) throws IOException {
		if (!srcDir.isDirectory() || !destDir.isDirectory()) {
			return false;
		}
		File[] srcDirFiles = srcDir.listFiles();
		for (int i = 0; i < srcDirFiles.length; i++) {
			if (srcDirFiles[i].isFile()) {
				File oneDestFile = new File(destDir.getPath() + "//" + srcDirFiles[i].getName());
				moveFileTo(srcDirFiles[i], oneDestFile);
				delFile(srcDirFiles[i]);
			} else if (srcDirFiles[i].isDirectory()) {
				File oneDestFile = new File(destDir.getPath() + "//" + srcDirFiles[i].getName());
				moveFilesTo(srcDirFiles[i], oneDestFile);
				delDir(srcDirFiles[i]);
			}

		}
		return true;
	}

	/**
	 * SD卡是否存在
	 * 
	 * @return
	 */
	public boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * SD卡剩余空间
	 * 
	 * @return
	 */
	public long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/**
	 * 保存图片到指定文件夹
	 * 
	 * @param bm
	 * @param picName
	 */
	public String saveBitmap(Bitmap bm, String path, String picName) {
		String imgPath = "";
		try {
			File f = new File(path, picName + ".jpg");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			imgPath = path + "/" + picName + ".jpg";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imgPath;
	}

	/**
	 * SD卡总容量
	 */
	public long getSDAllSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 获取所有数据块数
		long allBlocks = sf.getBlockCount();
		// 返回SD卡大小
		// return allBlocks * blockSize; //单位Byte
		// return (allBlocks * blockSize)/1024; //单位KB
		return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
	}
}
