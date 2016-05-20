package cn.com.bluemoon.jeesite.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.com.bluemoon.jeesite.modules.item.entity.MallItemInfo;
import cn.com.bluemoon.jeesite.modules.parameter.service.MallSysParameterService;

/**
 * 192.168.240.3 vsftp 用户mallftp 密码bmt.bm.123
 * 
 * @author hunaihong
 */
public class FtpUploadUtil {
	@Autowired
	private static MallSysParameterService mallSysParameterService;
	private static Logger log = (Logger) LoggerFactory.getLogger(FtpUploadUtil.class);
//
	public static final String SERVCIE_URL="192.168.240.3";
	public static final String USER_NAME="mallftp";
	public static final String PASS_WORD ="bmt.bm.123";
	

//	public static final String SERVCIE_URL="172.16.2.245";
//	public static final String USER_NAME="bmviewer";
//	PUBLIC STATIC FINAL STRING PASS_WORD ="MALL...12345";
	public static final int  PORT=21;
	
	public static final String FTP_URL="http://192.168.240.3";
	public static final String TEMP_UPLOAD="/upload/images/mall_product/test/";
	
	public static boolean syschPricute(String itemSku, File imageFile) {
		FileInputStream input = null;
		try {
			input = new FileInputStream(imageFile);
		    //设置响应给前台内容的数据格式  
		    //上传文件的原名(即上传前的文件名字)  
		    String newFileName = imageFile.getName();
		    FtpUtil ftp = new FtpUtil();
		    ftp.connect(SERVCIE_URL,PORT);
			ftp.setConnectTimeout(30 * 60 * 60 * 1000);
			ftp.login(USER_NAME,PASS_WORD);
			
			String dir = TEMP_UPLOAD + itemSku;
			if (!ftp.existDirectory(dir)){
				ftp.mkd(dir);
				ftp.changeWorkingDirectory(dir);
			}
			ftp.deleteFile(newFileName);
			
			ftp.setFileType(ftp.BINARY_FILE_TYPE);  
			boolean flag = ftp.storeFile(newFileName,input);
		    ftp.changeToParentDirectory();
			ftp.sendCommand("chmod 755 " + dir + " \r\n");
			ftp.logout();
			ftp.disconnect();
		    String basePath=FTP_URL+"/"+dir+"/"+newFileName;
		    log.info("图片上传成功！图片文件上传路径"+basePath);
		    return Boolean.TRUE;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	    return Boolean.FALSE;
	}
	
	public static String getUserImg(HttpServletRequest request, String fileName){
    	String path = request.getContextPath() + "/userFiles";
    	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
		String httpImg = basePath + fileName;
		return httpImg;
    }
	
	public static String getNewFilePath(String itemSku, String fileName,
			String service_url,int port,String user_name,String pass_word,String temp_upload) {
		File imageFile = new File(fileName);
		FileInputStream input = null;
		try {
			input = new FileInputStream(imageFile);
		    //设置响应给前台内容的数据格式  
		    //上传文件的原名(即上传前的文件名字)  
		    String newFileName = imageFile.getName();
		   // newFileName = prodId+suffix;
		    FtpUtil ftp = new FtpUtil();
		    ftp.connect(service_url,port);
			ftp.setConnectTimeout(30 * 60 * 60 * 1000);
			boolean loginFlag = ftp.login(user_name,pass_word);
			
			String dir = temp_upload + itemSku;
			if (!ftp.existDirectory(temp_upload)){
				ftp.mkd(temp_upload);
				ftp.changeWorkingDirectory(temp_upload);
			}
			if (!ftp.existDirectory(dir)){
				ftp.mkd(dir);
				ftp.changeWorkingDirectory(dir);
			}
			ftp.deleteFile(newFileName);
			
			ftp.setFileType(ftp.BINARY_FILE_TYPE);  
			boolean uploadFlag = ftp.storeFile(newFileName,input);
		    ftp.changeToParentDirectory();
			ftp.sendCommand("chmod 755 " + dir + " \r\n");
			ftp.logout();
			ftp.disconnect();
			String basePath = "";
			if(uploadFlag){
				basePath=dir+"/"+newFileName;
				log.info("图片上传成功！图片文件上传路径"+basePath);
			}
				return basePath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	    return "";
	}
	
	public static String getNewFilePathByRecommend(String itemSku, String fileName,
			String service_url,int port,String user_name,String pass_word,String temp_upload) {
		File imageFile = new File(fileName);
		FileInputStream input = null;
		try {
			input = new FileInputStream(imageFile);
		    //设置响应给前台内容的数据格式  
		    //上传文件的原名(即上传前的文件名字)  
		    String newFileName = imageFile.getName();
		    FtpUtil ftp = new FtpUtil();
		    ftp.connect(service_url,port);
			ftp.setConnectTimeout(30 * 60 * 60 * 1000);
			boolean loginFlag = ftp.login(user_name,pass_word);
			ftp.enterLocalPassiveMode();
			String dir = temp_upload + itemSku;
			String[] folders = null;
			folders = dir.split("/");
			for (int i = 0; i < folders.length; i++) {
				if (folders[i] != null && !"".equals(folders[i]) && !ftp.existDirectory(folders[i]) ){
					int c = ftp.mkd(folders[i]);
					boolean z = ftp.changeWorkingDirectory(folders[i]);
				}
			}
			ftp.deleteFile(newFileName);
			
			ftp.setFileType(ftp.BINARY_FILE_TYPE);  
			
			boolean uploadFlag = ftp.storeFile(newFileName,input);
		    ftp.changeToParentDirectory();
			ftp.sendCommand("chmod 755 " + dir + " \r\n");
			ftp.logout();
			ftp.disconnect();
			String basePath = "";
			if(uploadFlag){
				basePath=dir+"/"+newFileName;
				log.info("图片上传成功！图片文件上传路径"+basePath);
			}
				return basePath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	    return "";
	}

	public static void main(String[] args) throws Exception {
		File file = new File("D:\\linyihao\\workspace3\\bluemmsite\\src\\main\\webapp\\userfiles\\1\\images\\photo\\2015\\12\\112233.jpg");
		boolean flag = syschPricute("00000002", file);
		System.out.println(flag);
//		System.out.println(file.getName());
//		// =======上传测试============
//		 String localFile = "E:\\test.txt";
//		 String url = "192.168.240.3";
//		 int port = 21;
//		 String username ="mallftp";
//		 String password ="bmt.bm.123";
//		 String path ="/home/ftpuser/kw/";
//		 String filename ="test.txt";
//		 InputStream input = new FileInputStream(file);
//		 uploadFile(url, port, username, password, path, filename, input);
		// =======上传测试============

		// =======下载测试============
//		String localPath = "D:\\";
//		String url = "192.168.240.3";
//		int port = 21;
//		String username = "mallftp";
//		String password = "bmt.bm.123";
//		String remotePath = "/home/ftpuser/";
//		String fileName = "test1.txt";
//		downFile(url, port, username, password, remotePath, fileName, localPath);
		// =======下载测试============

	}

	/**
	 * Description: 向FTP服务器上传文件
	 * 
	 * @param url
	 *            FTP服务器 hostname
	 * @param port
	 *            FTP服务器端口 默认端 21
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param path
	 *            FTP服务器保存目录
	 * @param filename
	 *            上传到FTP服务器上的文件名
	 * @param input
	 *            输入流
	 * @return 成功返回true，否则返回false
	 */
	public static boolean uploadFile(String url, int port, String username,
			String password, String path, String filename, InputStream input) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);// 连接FTP服务器
			// ftp.connect(url);//连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(path);
			ftp.storeFile(filename, input);

			input.close();
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}

	/**
	 * Description: 从FTP服务器下载文件
	 * 
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param fileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return
	 */
	public static boolean downFile(String url, int port, String username,
			String password, String remotePath, String fileName,
			String localPath) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath + "/" + ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}

			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}
	
	public static String uploadImage(String imgPath,String fileName,HttpServletRequest request,int position,String imgType){
		String ftp = "FTP";
		String rootPath = request.getSession().getServletContext().getRealPath("/userfiles");
		if(fileName.contains("userfiles")){
			fileName = fileName.substring(fileName.indexOf("userfiles")+"userfiles".length());
		}
		String filePath =  rootPath+fileName;
		String FTP_USERNAME = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_USERNAME");
		String FTP_PASSWORD = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_PASSWORD");
		String FTP_URL = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_URL");
		int FTP_PORT = Integer.valueOf(mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_PORT"));
		String TEMP_UPLOAD = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "TEMP_UPLOAD");
		String mainPath = FtpUploadUtil.getNewFilePath(imgPath,filePath, 
				FTP_URL, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, TEMP_UPLOAD);
		return mainPath;
	}
}
