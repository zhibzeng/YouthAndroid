package com.android.server.action;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.android.server.Util.FileUpload;
import com.android.server.po.homeimage;
import com.android.server.service.IHomeImageService;
public class HomeImageAction {
	private int id;
	private String path;
	private File file=null;
	private String fileFileName;
	private String fileContentType;
	private IHomeImageService homeImageService;
	private homeimage Homeimage;
	private String HomeimagePath;
	private String UPLOADPATH="upload/homeimage";
	public String uploadFile() throws FileNotFoundException, IOException{
		FileUpload fileUpload = new FileUpload();
		fileUpload.setFile(file);
		fileUpload.setFileContentType(fileContentType);
		fileUpload.setFileFileName(fileFileName);
		fileUpload.setPATH(UPLOADPATH);
		String callback = fileUpload.main();
		if(callback.equals("error")){
			return "error";
		}else{
			homeimage homeimage = new homeimage();
			homeimage.setPath(UPLOADPATH+"/"+callback);
			homeImageService.save(homeimage);
			return display();
		}
	}
	
	public String update() throws FileNotFoundException, IOException{
		homeImageService.delete(id);
		FileUpload fileUpload = new FileUpload();
		fileUpload.setFile(file);
		fileUpload.setFileContentType(fileContentType);
		fileUpload.setFileFileName(fileFileName);
		fileUpload.setPATH(UPLOADPATH);
		String callback = fileUpload.main();
		if(callback.equals("error")){
			return "error";
		}else{
			homeimage homeimage = new homeimage();
			homeimage.setPath(UPLOADPATH+"/"+callback);
			homeImageService.save(homeimage);
			return "uploadsuccess";
		}
	}
	
	public String display(){
		Homeimage=homeImageService.getImage();
		if(Homeimage==null){
			return "error";
		}else{
			HomeimagePath=Homeimage.getPath();
			this.id=Homeimage.getId();
			return "success";	
		}
		
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
	public IHomeImageService getHomeImageService() {
		return homeImageService;
	}
	public void setHomeImageService(IHomeImageService homeImageService) {
		this.homeImageService = homeImageService;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public homeimage getHomeimage() {
		return Homeimage;
	}

	public void setHomeimage(homeimage homeimage) {
		Homeimage = homeimage;
	}

	public String getHomeimagePath() {
		return HomeimagePath;
	}

	public void setHomeimagePath(String homeimagePath) {
		HomeimagePath = homeimagePath;
	}

	public String getUPLOADPATH() {
		return UPLOADPATH;
	}

	public void setUPLOADPATH(String uPLOADPATH) {
		UPLOADPATH = uPLOADPATH;
	}

}
