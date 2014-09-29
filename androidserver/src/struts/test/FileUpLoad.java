package struts.test;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class FileUpLoad extends ActionSupport{
	private File[] upload;
	private String[] uploadFileName;
	private String[] uploadContentType;
	public File[] getUpload() {
		return upload;
	}
	public void setUpload(File[] upload) {
		this.upload = upload;
	}
	public String[] getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String[] uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public String[] getUploadContentType() {
		return uploadContentType;
	}
	public void setUploadContentType(String[] uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	public String upload(){
		ServletContext servletContext = ServletActionContext.getServletContext();
		String dataUpload = servletContext.getRealPath("/WEB-INF");
		for(int i=0;i<upload.length;i++){
			File saveFile = new File(dataUpload, uploadFileName[i]);
			upload[i].renameTo(saveFile);
		}
		return SUCCESS;
	}
	

}
