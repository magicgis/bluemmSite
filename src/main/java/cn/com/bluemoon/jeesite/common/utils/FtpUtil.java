package cn.com.bluemoon.jeesite.common.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class FtpUtil extends FTPClient{
	
	public static final String SERVCIE_URL="qie360.cn";
	public static final String USER_NAME="qieftpresources";
	public static final String PASS_WORD ="tT1bCABVeFps";
	public static final int  PORT=21;
	
	public static final String FTP_URL="http://qieimg.qie360.cn";
	public static final String TEMP_UPLOAD="tempUpload";
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	public FtpUtil() {
		super();
	}

	/**
	 * @Title: existDirectory
	 * @Description: TODO 判断目录是否存在
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public boolean existDirectory(String path) throws IOException {
		boolean flag = false;
		FTPFile[] ftpFileArr = super.listFiles(path);
		for (FTPFile ftpFile : ftpFileArr) {
			if (ftpFile.isDirectory()
					&& ftpFile.getName().equalsIgnoreCase(path)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * @Title: removeDirectory
	 * @Description: TODO删除目录
	 * @param path
	 *            dir
	 * @param isAll
	 *            是否全删
	 * @return
	 * @throws IOException
	 */
	public boolean removeDirectory(String path, boolean isAll)
			throws IOException {

		if (!isAll) {
			return super.removeDirectory(path);
		}

		FTPFile[] ftpFileArr = super.listFiles(path);
		if (ftpFileArr == null || ftpFileArr.length == 0) {
			return removeDirectory(path);
		}
		//    
		for (FTPFile ftpFile : ftpFileArr) {
			String name = ftpFile.getName();
			if (ftpFile.isDirectory()) {
				removeDirectory(path + "/" + name, true);
			} else if (ftpFile.isFile()) {
				deleteFile(path + "/" + name);
			} else if (ftpFile.isSymbolicLink()) {

			} else if (ftpFile.isUnknown()) {

			}
		}
		return super.removeDirectory(path);
	}

	/**
	 * 上传文件到FTP服务器
	 * 
	 */
	public void uploadFile(String localFile, String id) {
		FileInputStream in = null;
		try {
			File tmp = new File(localFile);
			if (tmp.exists() && tmp.isFile()) {
				in = new FileInputStream(localFile);
				storeFile(id+ localFile.substring(localFile.lastIndexOf(".")),in);
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(in!=null){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String uploadFtp(File file,String url,String fileName){
		try{
			FtpUtil ftp = new FtpUtil();
			ftp.connect(FtpUtil.SERVCIE_URL,FtpUtil.PORT);
			ftp.setConnectTimeout(30 * 60 * 60 * 1000);
			if (ftp.login(FtpUtil.USER_NAME,FtpUtil.PASS_WORD)) {
				String[] folders = url.split("//");
				for (int i = 0; i < folders.length; i++) {
					if (!ftp.existDirectory(folders[i]) && folders[i] != null){
						ftp.mkd(folders[i]);
						ftp.changeWorkingDirectory(folders[i]);
					}
				}
		        ftp.setFileType(ftp.BINARY_FILE_TYPE);  
				ftp.uploadFile(file.getPath(), fileName);
				ftp.changeToParentDirectory();
				ftp.sendCommand("chmod 755 " + url + " \r\n");
				ftp.logout();
				ftp.disconnect();
			}
			return "1";
		}catch(Exception e){
			e.printStackTrace();
			return "0";
		}
		
		
	}

	public void uploadFileFileName(String localFile, String fileName) {
		FileInputStream in = null;
		try {
			File tmp = new File(localFile);
			if (tmp.exists() && tmp.isFile()) {
				in = new FileInputStream(localFile);
				storeFile(fileName,
						in);
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * 图片上传到ftp
	 */
	
	public  String ftpPhoto(String url,String userId,String flag,String userPhoto,String ftpUrl){
		try{
			String id=SerialNo.getUNID();
	          //文件名
	      	  String fileName = url.substring(url.lastIndexOf("/"), url.length());
	      	  //文件名后缀
	      	  String suffix=fileName.substring(fileName.lastIndexOf("."), fileName.length());
	          //新文件名
	         String newFileName=id+"_"+flag+suffix;
  		     connect(FtpUtil.SERVCIE_URL,FtpUtil.PORT);
 			setConnectTimeout(30 * 60 * 60 * 1000);
 			login(FtpUtil.USER_NAME,FtpUtil.PASS_WORD);
 			if (!existDirectory(userId) && userId != null){
				mkd(userId);
			}
 			changeWorkingDirectory(FtpUtil.TEMP_UPLOAD);
 			setFileType(BINARY_FILE_TYPE);  
 			String fileNameDel=url.substring(url.lastIndexOf("/")+1);
 			boolean falg=rename(fileNameDel, ".."+ftpUrl+newFileName);
 		    if(!StringUtil.isEmpty(userPhoto)){
 		    	String userPhotoTemp=userPhoto.substring(userPhoto.lastIndexOf("/")+1);
 		    	if (!existDirectory(userId) && userId != null){
 		    		changeWorkingDirectory("../"+userId);
 				}
 		    	boolean b=deleteFile(userPhotoTemp);
 		    	log.info("删除FTP图片:"+userPhotoTemp+","+b);
 		    }
 			changeToParentDirectory();
 			sendCommand("chmod 755 " + FtpUtil.TEMP_UPLOAD + " \r\n");
 			logout();
 			disconnect();
 			log.info("图片新路径："+FtpUtil.FTP_URL+ftpUrl+newFileName);
 			if(falg){
 				return FtpUtil.FTP_URL+ftpUrl+newFileName;
 			}
		}catch(Exception e){
			e.printStackTrace();
			log.info("图片上传Erorr"+e);
			return null;
		}
		return null;
	}
	
	/**
	 * 图片上传到ftp
	 */
	
	public  String ftpPhotoMap(String url,String userId,String flag,String userPhoto,String ftpUrl,Map<String,String> map){
		try{
			String id=SerialNo.getUNID();
	          //文件名
	      	  String fileName = url.substring(url.lastIndexOf("/"), url.length());
	      	  //文件名后缀
	      	  String suffix=fileName.substring(fileName.lastIndexOf("."), fileName.length());
	          //新文件名
	         String newFileName=id+"_"+flag+suffix;
  		     connect(FtpUtil.SERVCIE_URL,FtpUtil.PORT);
 			setConnectTimeout(30 * 60 * 60 * 1000);
 			login(FtpUtil.USER_NAME,FtpUtil.PASS_WORD);
 			if (!existDirectory(userId) && userId != null){
				mkd(userId);
			}
 			changeWorkingDirectory(FtpUtil.TEMP_UPLOAD);
 			setFileType(BINARY_FILE_TYPE);  
 			String fileNameDel=url.substring(url.lastIndexOf("/")+1);
 			boolean falg=rename(fileNameDel, ".."+ftpUrl+newFileName);
 		    if(!StringUtil.isEmpty(userPhoto)){
 		    	String userPhotoTemp=userPhoto.substring(userPhoto.lastIndexOf("/")+1);
 		    	boolean b=deleteFile(userPhotoTemp);
 		    	log.info("删除FTP图片:"+userPhotoTemp+","+b);
 		    }
 			changeToParentDirectory();
 			sendCommand("chmod 755 " + FtpUtil.TEMP_UPLOAD + " \r\n");
 			logout();
 			disconnect();
 			log.info("图片新路径："+FtpUtil.FTP_URL+ftpUrl+newFileName);
 			if(falg){
 				return FtpUtil.FTP_URL+ftpUrl+newFileName;
 			}
		}catch(Exception e){
			e.printStackTrace();
			log.info("图片上传Erorr"+e);
			return null;
		}
		return null;
	}
	
}

