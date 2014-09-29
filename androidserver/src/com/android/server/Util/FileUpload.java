package com.android.server.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.apache.struts2.ServletActionContext;

public class FileUpload {
	private String Ex[]={ "image/bmp", "image/png", "image/gif", "image/jpeg",
	"image/pjpeg" };			//Allow Types
	private String PATH;			//Upload Path
	private File file=null; 		//Upload File
	private String fileFileName;	//File Name
	private String fileContentType;	//File Type
	private String newName;			//File New Name
	
	//constructor
	public FileUpload(){};
	public FileUpload(File file,String fileFileName,String fileContentType,String PATH,String Ex[]){
		this.Ex=Ex;
		this.PATH=PATH;
		this.file=file;
		this.fileContentType=fileContentType;
		this.fileFileName=fileFileName;
	}
	
	
	// check the file type
	private boolean checkFileType(String type) {
		boolean flag = false;
		if (type == null || type.equals("")) {
			return false;
		} else {
			for (int i = 0; i < Ex.length; i++) {
				if (type.equals(Ex[i])) {
					flag = true;
				} else {
					continue;
				}
			}
			return flag;
		}
	}
	
	// New Upload File Name
	private String generateFileName(String fileName) {
		DateFormat format = new SimpleDateFormat("yyMMddHHmmss");
		String formatDate = format.format(new Date());
		int random = new Random().nextInt(10000);
		int position = fileName.lastIndexOf(".");
		String extension = fileName.substring(position);
		return formatDate + random + extension;
	}
	
	//Upload File Main Method
	public boolean uploadFile() throws FileNotFoundException, IOException {
		try {
			boolean flag;
			flag=checkFileType(fileContentType);
			if(!flag){
				return false;
			}
			InputStream in = new FileInputStream(file);
			newName = generateFileName(fileFileName);
			File uploadFile = new File(ServletActionContext.getServletContext()
					.getRealPath(PATH), newName);
			//可以使用 FileUtils.copyFile(file.get(i), uploadFile);
			OutputStream out = new FileOutputStream(uploadFile);
			byte[] buffer = new byte[1024 * 1024];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.flush();
			out.close();
			
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return true;
	}
	
	public String main() throws FileNotFoundException, IOException{
		File dir = new File(ServletActionContext.getServletContext()
				.getRealPath(PATH));
		if (!dir.exists()) {
			dir.mkdirs();
		}
		boolean flag=uploadFile();
		if(flag){
			return newName;
		}else{
			return "error";
		}
	}
	public String[] getEx() {
		return Ex;
	}
	public void setEx(String[] ex) {
		Ex = ex;
	}
	public String getPATH() {
		return PATH;
	}
	public void setPATH(String pATH) {
		PATH = pATH;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	public String getFileContentType() {
		return fileContentType;
	}
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
	
	
	
}
