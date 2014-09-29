package com.android.server.action;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.android.server.po.PageBean;
import com.android.server.po.newsCategory;
import com.android.server.po.newsCategoryImage;
import com.android.server.po.newsimage;
import com.android.server.service.INewsCategoryImageService;
import com.android.server.service.INewsCategoryService;

public class NewsCategoryAction {
	private int id;
	private int page;
	private PageBean pageBean;
	private String name;
	private INewsCategoryService newsCategoryService;
	private INewsCategoryImageService categoryImageService;
	private final static String UPLOADDIR = "upload/categoryimage";
	private List<File> file=null;
	private List<String> fileFileName;
	private List<String> fileContentType;
	private List<String> fileNewName = new ArrayList();
	private String imagepath;
	private static int PERPAGENUM=10;//每页显示的数目
	// check the file type
	private boolean checkFileType(String type) {
		String Ex[] = { "image/bmp", "image/png", "image/gif", "image/jpeg",
				"image/pjpeg" };
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

	
	
	
	public String save() throws FileNotFoundException, IOException{
		if(!name.equals("")){
			newsCategory newsCategory = new newsCategory();
			newsCategory.setName(name);
			newsCategoryService.save(newsCategory);
			id=newsCategoryService.getID(name);
			File dir = new File(ServletActionContext.getServletContext()
					.getRealPath(UPLOADDIR));
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if(file!=null){
				for (int i = 0; i < file.size(); i++) {
					if (checkFileType(fileContentType.get(i))) {
						uploadFile(i);
					} else {
						//message = "您上传的 "+fileFileName.get(i)+" 不是指定格式的新闻配图，请重新上传！";
						//return "saveError";
					}

				}
			}
			
			if(file!=null){
				for(int i=0;i<file.size();i++){
					newsCategoryImage categoryImage = new newsCategoryImage();
					categoryImage.setCategoryid(id);
					categoryImage.setImagepath(fileNewName.get(i));
					categoryImageService.save(categoryImage);
				}
			}
			return "SaveSuccess";
		}else{
			return "SaveError";
		}
		
	}
	
	public void listAll() throws IOException{
				List  newsCategories = newsCategoryService.ListAll();
		    	Iterator<newsCategory> itr =(Iterator<newsCategory>)newsCategories.iterator();
		    	JSONArray jsonArray=new JSONArray();
		    	while(itr.hasNext())
		    	{
		    		 newsCategory category=(newsCategory) itr.next();
		    		 JSONObject temp = new JSONObject()  
		             .element( "id", category.getId())  
		             .element( "name",category.getName());
		             jsonArray.add(temp);
		    	}
		    	HttpServletResponse response = ServletActionContext.getResponse(); 
		    	response.setContentType("text/html"); 
		        response.setCharacterEncoding("UTF-8");
		        response.getWriter().print(jsonArray.toString());         
	
	}
	public String update() throws FileNotFoundException, IOException{
		File dir = new File(ServletActionContext.getServletContext()
				.getRealPath(UPLOADDIR));
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if(file!=null){
			for (int i = 0; i < file.size(); i++) {
				if (checkFileType(fileContentType.get(i))) {
					uploadFile(i);
				} 
				categoryImageService.delete(id);
				newsCategoryImage categoryImage = new newsCategoryImage();
				categoryImage.setCategoryid(id);
				categoryImage.setImagepath(fileNewName.get(i));
				categoryImageService.save(categoryImage);
			}
			
		}
		newsCategory newsCategory = new newsCategory();
		newsCategory.setId(id);
		newsCategory.setName(name);
		newsCategoryService.update(newsCategory);
		return "updateSuccess";
	}
	public String queryPerPage(){
		this.pageBean = newsCategoryService.queryForPage(PERPAGENUM, page);
		return "queryPerPageSuccess";
	}
	
	private void uploadFile(int i) throws FileNotFoundException, IOException {

		try {
			InputStream in = new FileInputStream(file.get(i));
			String newName = generateFileName(fileFileName.get(i));
			fileNewName.add(UPLOADDIR+"/" + newName);
			File uploadFile = new File(ServletActionContext.getServletContext()
					.getRealPath(UPLOADDIR), newName);
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

	}
	
	public String updateShow() throws UnsupportedEncodingException{
		this.name=new String(name.getBytes("iso-8859-1"),"utf-8");
		newsCategoryImage categoryImage = categoryImageService.getByCategoryID(id);
		if(categoryImage!=null){
			this.imagepath=categoryImage.getImagepath();	
		}else{
			this.imagepath="";//��ͼƬ
		}
		return "updateshow";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public INewsCategoryService getNewsCategoryService() {
		return newsCategoryService;
	}

	public void setNewsCategoryService(INewsCategoryService newsCategoryService) {
		this.newsCategoryService = newsCategoryService;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public INewsCategoryImageService getCategoryImageService() {
		return categoryImageService;
	}
	public void setCategoryImageService(
			INewsCategoryImageService categoryImageService) {
		this.categoryImageService = categoryImageService;
	}
	public List<File> getFile() {
		return file;
	}
	public void setFile(List<File> file) {
		this.file = file;
	}
	public List<String> getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(List<String> fileFileName) {
		this.fileFileName = fileFileName;
	}
	public List<String> getFileContentType() {
		return fileContentType;
	}
	public void setFileContentType(List<String> fileContentType) {
		this.fileContentType = fileContentType;
	}
	public List<String> getFileNewName() {
		return fileNewName;
	}
	public void setFileNewName(List<String> fileNewName) {
		this.fileNewName = fileNewName;
	}
	public String getImagepath() {
		return imagepath;
	}
	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}
	
	

}
