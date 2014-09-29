package com.android.server.action;

import java.util.ArrayList;
import java.util.List;

import com.android.server.po.PageBean;
import com.android.server.po.newsContent;
import com.android.server.po.newsaudio;
import com.android.server.po.newsimage;
import com.android.server.service.ICommentService;
import com.android.server.service.INewsAudioService;
import com.android.server.service.INewsCategoryService;
import com.android.server.service.INewsContentService;
import com.android.server.service.INewsImageService;

public class NewsContentPageAction {
	private List<String> categoryName;
	private String newsCategoryName;
	private int id;
	private newsContent contenDetail;
	private int page;
	private PageBean pageBean;
	private String message="";
	private INewsContentService newsContentService;
	private INewsCategoryService newsCategoryService;
	private List<newsimage> newsImageList;
	private List<newsaudio> newsAudioList;
	private INewsImageService newsImageService;
	private INewsAudioService newsAudioService;
	private ICommentService commentService;
	private static int PERPAGENUM=10;//每页显示的数目
	private Integer total = 0;
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String queryPerPage(){
		categoryName = new ArrayList();
		this.pageBean = newsContentService.queryForPage(PERPAGENUM, page);
		for (int i=0;i<pageBean.getList().size();i++) {
			newsContent content = (newsContent) pageBean.getList().get(i);
			categoryName.add(i,newsCategoryService.queryById(content.getCategory()).getName());
		}
		return "queryPerPageSuccess";
	}

	public String articleDetail(){
		contenDetail = newsContentService.queryById(id);
		newsImageList = newsImageService.listById(id);
		newsAudioList = newsAudioService.listById(id);
		newsCategoryName = newsCategoryService.queryById(contenDetail.getCategory()).getName();
		total = commentService.getCommentCounts(id);
		return "detailSuccess";
	}

	public ICommentService getCommentService() {
		return commentService;
	}

	public void setCommentService(ICommentService commentService) {
		this.commentService = commentService;
	}

	public String articleDelete(){
		if(newsContentService.articleDelete(id)){
			return "deleteSuccess";
		}else{
			return "deleteError";
		}
		
	}

	public String articlePreUpdate() throws Exception{
		if(!message.equals("")){
		if(message.equals("updateNoFile")){
			message="请上传新闻配图！";
		}
		if(message.equals("updateError")){
			message="您上传的不是指定格式的新闻图片！请重新上传！";
			
		}if(message.equals("updateSuccess")){
			message = "文章已经成功修改！";
		}
		}else{message="";}
		contenDetail = newsContentService.queryById(id);
		return "preUpdateSuccess";
		
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
	public INewsContentService getNewsContentService() {
		return newsContentService;
	}
	public void setNewsContentService(INewsContentService newsContentService) {
		this.newsContentService = newsContentService;
	}


	public List<String> getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(List<String> categoryName) {
		this.categoryName = categoryName;
	}


	public INewsCategoryService getNewsCategoryService() {
		return newsCategoryService;
	}


	public void setNewsCategoryService(INewsCategoryService newsCategoryService) {
		this.newsCategoryService = newsCategoryService;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public newsContent getContenDetail() {
		return contenDetail;
	}

	public void setContenDetail(newsContent contenDetail) {
		this.contenDetail = contenDetail;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNewsCategoryName() {
		return newsCategoryName;
	}

	public void setNewsCategoryName(String newsCategoryName) {
		this.newsCategoryName = newsCategoryName;
	}

	public List<newsimage> getNewsImageList() {
		return newsImageList;
	}

	public void setNewsImageList(List<newsimage> newsImageList) {
		this.newsImageList = newsImageList;
	}

	public INewsImageService getNewsImageService() {
		return newsImageService;
	}

	public void setNewsImageService(INewsImageService newsImageService) {
		this.newsImageService = newsImageService;
	}

	public List<newsaudio> getNewsAudioList() {
		return newsAudioList;
	}

	public void setNewsAudioList(List<newsaudio> newsAudioList) {
		this.newsAudioList = newsAudioList;
	}

	public INewsAudioService getNewsAudioService() {
		return newsAudioService;
	}

	public void setNewsAudioService(INewsAudioService newsAudioService) {
		this.newsAudioService = newsAudioService;
	}
	
	

	

}
