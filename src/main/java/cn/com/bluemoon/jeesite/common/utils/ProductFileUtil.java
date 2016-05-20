package cn.com.bluemoon.jeesite.common.utils;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

/**
 *用户图片操作
 */
public class ProductFileUtil {
	//http://xx.com/upload/images/moon_mall/userImage/50/1441785926006.jpg
    public static String FILEDIR_PATH = "upload/images/mall_product/test";
    public static String FILEDIR = null;
    
    public static String getUserImg(HttpServletRequest request, String fileName){
    	String path = request.getContextPath();
    	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		String httpImg = basePath+ FILEDIR_PATH + fileName;
		return httpImg;
    }
    
    /**
     * 上传
     * @param request
     * @throws IOException
     */
    public static String upload(String fileName, byte[] fileBy, String itemSku,String filePath) {
		BufferedOutputStream bos = null;  
        FileOutputStream fos = null;  
        File fileImg = new File(filePath);  
		try {
			fos = new FileOutputStream(fileImg);  
			bos = new BufferedOutputStream(fos);  
			bos.write(fileBy);
			bos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
	        return "";
		} catch (IOException e) {
			e.printStackTrace();
	        return "";
		}  
		FtpUploadUtil.syschPricute(itemSku, new File(filePath));
        String newPath = FILEDIR_PATH + itemSku +"/"+ fileName;
        return newPath;
    }

    private static String initFilePath(String name, String itemSku, String fileName) {
        File file = new File(FILEDIR+itemSku);
        if (!file.exists()) {
            file.mkdirs();
        }
        String ext = name.substring(name.lastIndexOf('.'), name.length());
        return (file.getPath() + "/" + itemSku +"/"+ fileName + ext);
    }
    
    public static void download(String downloadfFileName, ServletOutputStream out) {
        try {
            FileInputStream in = new FileInputStream(new File(FILEDIR + "/" + downloadfFileName));
            write(in, out);
        } catch (FileNotFoundException e) {
            try {
                FileInputStream in = new FileInputStream(new File(FILEDIR + "/"
                        + new String(downloadfFileName.getBytes("iso-8859-1"),"utf-8")));
                write(in, out);
            } catch (IOException e1) {              
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }       
    }
    /**
     * 写入数据
     * @param in
     * @param out
     * @throws IOException
     */
    public static void write(InputStream in, OutputStream out) throws IOException{
        try{
            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        } finally {
            try {
                in.close();
            }
            catch (IOException ex) {
            }
            try {
                out.close();
            }
            catch (IOException ex) {
            }
        }
    }   
    
    public static byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }  

    public static void main(String[] args) {
    	String fileName = "D:\\linyihao\\workspace3\\bluemmsite\\src\\main\\webapp\\userfiles\\1\\images\\photo\\2015\\12\\112233.jpg";
    	byte[] fileByte =  getBytes(fileName);
    	String filePath = upload("112233.jpg", fileByte, "LYH",fileName);
    	System.out.println(filePath);
	}
       
}