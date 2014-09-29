package com.android.server.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;
import com.android.server.po.newsContent;
import com.android.server.po.newsaudio;
import com.android.server.po.newsimage;
import com.android.server.service.INewsAudioService;
import com.android.server.service.INewsContentService;
import com.android.server.service.INewsImageService;
import com.opensymphony.xwork2.conversion.annotations.Conversion;

public class NewsContentAction {
	private int id;
	private String title;
	private String content;
	private int category;
	private String image;
	private int viewnum;
	private int replynum;
	private Date time;
	private String comefrom;
	private INewsImageService newsImageService;
	private final static String IMGUPLOADDIR = "/upload/image";//image path
	private final static String AUDIOUPLOADDIR = "/upload/audio";//audio path
	//新闻配图
	private List<File> file=null;
	private List<String> fileFileName;
	private List<String> fileContentType;
	private List<String> fileNewName = new ArrayList();
	//新闻录音
	private File audiofile=null;
	private String audiofileFileName;
	private String audiofileFileContentType;
	private String audioFileNewName;
	private String message = "";
	private INewsContentService newsContentService;
	private INewsAudioService newsAudioService;
	
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

	public String save() throws FileNotFoundException, IOException {
		//Image path
		File imageDir = new File(ServletActionContext.getServletContext()
				.getRealPath(IMGUPLOADDIR));
		if (!imageDir.exists()) {
			imageDir.mkdirs();
		}
		//audio path
		File audioDir = new File(ServletActionContext.getServletContext()
				.getRealPath(AUDIOUPLOADDIR));
		if (!audioDir.exists()) {
			audioDir.mkdirs();
		}
		
		//check the image type
		if(file!=null){
			for (int i = 0; i < file.size(); i++) {
				if (checkFileType(fileContentType.get(i))) {
					uploadFile(i);
				} else {
					message = "您上传的 "+fileFileName.get(i)+" 不是指定格式的新闻配图，请重新上传！";
					return "saveError";
				}

			}
		}
		//check the audio type
		if(audiofile!=null){
			if(audiofileFileName.trim().toLowerCase().endsWith(".mp3")){
				uploadAudio(audiofile);
			}else{
				message = "您上传的 "+audiofileFileName+" 不是MP3格式的音频文件，请重新上传！";
				return "saveError";
			}
			
		}
		
		newsContent newsContent = new newsContent();
		newsContent.setTitle(title);
		String regx=" ";
		String contentConver="";
		Pattern p = Pattern.compile(regx);
		String[] subString = p.split(content);
		for(int i=0;i<subString.length;i++){
			if(!subString[i].trim().equals("")){
				contentConver=contentConver+subString[i].trim()+"\r\n";
			}	
		}
		newsContent.setContent(contentConver);
		newsContent.setViewnum(0);
		newsContent.setReplynum(0);
		newsContent.setCategory(category);
		newsContent.setTime(time);
		newsContent.setComefrom(comefrom);
		newsContentService.save(newsContent);
		//newsID
		id=newsContent.getId();
		//有图像文件
		if(file!=null){
			for(int i=0;i<file.size();i++){
				newsimage newsimage = new newsimage();
				newsimage.setNewsid(id);
				newsimage.setImagepath(fileNewName.get(i));
				newsImageService.save(newsimage);
			}
		}
		//有音频文件
		if(audiofile!=null){
			newsaudio newsaudio = new newsaudio();
			newsaudio.setNewsid(id);
			newsaudio.setAudiopath(audioFileNewName);
			newsAudioService.save(newsaudio);
		}
		message="您已经成功发布新闻："+title+"，如有需要请继续发布！";
		return "saveSuccess";
		}
	
	
	public String update() throws IOException, IOException{
		File dir = new File(ServletActionContext.getServletContext()
				.getRealPath(IMGUPLOADDIR));
		if (!dir.exists()) {
			dir.mkdirs();
		}
		//audio path
		File audioDir = new File(ServletActionContext.getServletContext()
				.getRealPath(AUDIOUPLOADDIR));
		if (!audioDir.exists()) {
			audioDir.mkdirs();
		}
		//如果不上传新的图片，则不更改
		if(file!=null){
			for (int i = 0; i < file.size(); i++) {
				if (checkFileType(fileContentType.get(i))) {
				uploadFile(i);
				} else {
				message ="updateError";
				return "updateError";
				}
			}
			if(file!=null){
				newsImageService.delete(id);
				for(int i=0;i<file.size();i++){
					newsimage newsimage = new newsimage();
					newsimage.setNewsid(id);
					newsimage.setImagepath(fileNewName.get(i));
					newsImageService.save(newsimage);
				}
			}
		}
		
		if(audiofile!=null){
			if(audiofileFileName.trim().toLowerCase().endsWith(".mp3")){
				uploadAudio(audiofile);
			}else{
				message ="updateError";
				return "updateError";
			}
			//有音频文件
			if(audiofile!=null){
				newsAudioService.delete(id);
				newsaudio newsaudio = new newsaudio();
				newsaudio.setNewsid(id);
				newsaudio.setAudiopath(audioFileNewName);
				newsAudioService.save(newsaudio);
			}
			
		}
		newsContent newsContent = new newsContent();
		newsContent.setId(id);
		newsContent.setTitle(title);
		String regx=System.getProperty("line.separator");
		String contentConver="";
		Pattern p = Pattern.compile(regx);
		String[] subString = p.split(content);
		for(int i=0;i<subString.length;i++){
			if(!subString[i].trim().equals("")){
				subString[i]=subString[i].trim();
				contentConver=contentConver+subString[i].trim()+System.getProperty("line.separator");
			}	
		}
		newsContent.setContent(contentConver);
		newsContent.setViewnum(0);
		newsContent.setReplynum(0);
		newsContent.setCategory(category);
		newsContent.setTime(time);
		newsContent.setComefrom(comefrom);
		newsContentService.update(newsContent);
		message="updateSuccess";
		return "updateSuccess";
	}
	
	
	
	//上传图片文件
	private void uploadFile(int i) throws FileNotFoundException, IOException {
		try {
			InputStream in = new FileInputStream(file.get(i));
			String newName = generateFileName(fileFileName.get(i));
			fileNewName.add("upload/image/" + newName);
			File uploadFile = new File(ServletActionContext.getServletContext()
					.getRealPath(IMGUPLOADDIR), newName);
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
	
	//上传音频文件
	private void uploadAudio(File file){
		try {
			InputStream in = new FileInputStream(file);
			String newName = generateFileName(audiofileFileName);
			audioFileNewName="upload/audio/" + newName;
			File uploadFile = new File(ServletActionContext.getServletContext()
					.getRealPath(AUDIOUPLOADDIR), newName);
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


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public INewsContentService getNewsContentService() {
		return newsContentService;
	}

	public void setNewsContentService(INewsContentService newsContentService) {
		this.newsContentService = newsContentService;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getViewnum() {
		return viewnum;
	}

	public void setViewnum(int viewnum) {
		this.viewnum = viewnum;
	}

	public int getReplynum() {
		return replynum;
	}

	public void setReplynum(int replynum) {
		this.replynum = replynum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	

	public String getComefrom() {
		return comefrom;
	}

	public void setComefrom(String comefrom) {
		this.comefrom = comefrom;
	}

	public List<String> getFileNewName() {
		return fileNewName;
	}

	public void setFileNewName(List<String> fileNewName) {
		this.fileNewName = fileNewName;
	}
	public INewsImageService getNewsImageService() {
		return newsImageService;
	}
	public void setNewsImageService(INewsImageService newsImageService) {
		this.newsImageService = newsImageService;
	}
	public File getAudiofile() {
		return audiofile;
	}
	public void setAudiofile(File audiofile) {
		this.audiofile = audiofile;
	}
	
	public String getAudiofileFileName() {
		return audiofileFileName;
	}
	public void setAudiofileFileName(String audiofileFileName) {
		this.audiofileFileName = audiofileFileName;
	}
	public String getAudiofileFileContentType() {
		return audiofileFileContentType;
	}
	public void setAudiofileFileContentType(String audiofileFileContentType) {
		this.audiofileFileContentType = audiofileFileContentType;
	}
	public String getAudioFileNewName() {
		return audioFileNewName;
	}
	public void setAudioFileNewName(String audioFileNewName) {
		this.audioFileNewName = audioFileNewName;
	}
	public INewsAudioService getNewsAudioService() {
		return newsAudioService;
	}
	public void setNewsAudioService(INewsAudioService newsAudioService) {
		this.newsAudioService = newsAudioService;
	}
	
	

}
