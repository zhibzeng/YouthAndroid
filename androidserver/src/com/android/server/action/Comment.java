package com.android.server.action;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.android.server.service.ICommentService;
public class Comment extends JsonActionSupport{
	private int id;
	private int newsid;
	private int userid;
	private String content;
	private String username;
	private String createtime;
	private ICommentService commentService;
	private int start;//默认获取第0页，每页返回10条记录
	private int limit;
	
	/**
	 * 提交新闻评论
	 * @param newsid userid content username 
	 */
	public void commentSave(){
		boolean flag = false;
		com.android.server.po.Comment comment = new com.android.server.po.Comment();
		comment.setNewsid(newsid);
		comment.setUserid(userid);
		comment.setContent(content);
		comment.setUsername(username);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
		String str = formatter.format(curDate);
		comment.setCreatetime(str);
		flag=commentService.save(comment);
		JSONObject o = new JSONObject();
		if(flag){
		o.put("type","1");
		}else{
			o.put("type","0");
		}
		write2Client(o.toString());
	}
	
	
	/**
	 * 根据新闻id获取评论
	 * @param newsid
	 * @return
	 */
	
	public void getCommentByNewsID(){
		List<com.android.server.po.Comment> commentList = null;
		commentList = commentService.getCommentByNewsID(newsid);
		JSONObject o = new JSONObject();
		JSONArray array = new JSONArray();
		if(commentList!=null){
			for (com.android.server.po.Comment comment : commentList) {
				JSONObject object = new JSONObject();
				object.put("id",comment.getId()+"");
				object.put("newsid",comment.getNewsid()+"");
				object.put("userid",comment.getUserid()+"");
				object.put("username",comment.getUsername());
				object.put("content", comment.getContent());
				object.put("createtime", comment.getCreatetime());
				array.add(object);
			}
			o.put("size",commentList.size()+"");
			o.put("rows",array);
		}else{
			o.put("size","0");
			o.put("rows","");
		}
		write2Client(o.toString());
	}
	
	
	/**
	 * 根据新闻id获取评论数目
	 */
	public void getCommentNumByNewsID(){
		List<com.android.server.po.Comment> commentList = null;
		commentList = commentService.getCommentByNewsID(newsid);
		JSONObject o = new JSONObject();
		if(commentList!=null){
			o.put("size",commentList.size()+"");
		}else{
			o.put("size","0");
		}
		write2Client(o.toString());
	}
	
	/**
	 * 根据用户ID获取新闻评论信息
	 *@param userid
	 * @return
	 */

	public void getCommentByUserID(){
		List<com.android.server.po.Comment> commentList = null;
		commentList = commentService.getCommentByUserID(userid);
		JSONObject o = new JSONObject();
		JSONArray array = new JSONArray();
		if(commentList!=null){
			for (com.android.server.po.Comment comment : commentList) {
				JSONObject object = new JSONObject();
				object.put("id",comment.getId()+"");
				object.put("newsid",comment.getNewsid()+"");
				object.put("userid",comment.getUserid()+"");
				object.put("username",comment.getUsername());
				object.put("content", comment.getContent());
				object.put("createtime", comment.getCreatetime());
				array.add(object);
			}
			o.put("size",commentList.size()+"");
			o.put("rows",array);
		}else{
			o.put("size","0");
			o.put("rows","");
		}
		write2Client(o.toString());
	}
	
	
	/**
	 * 分页获取新闻评论
	 * @author zhibinzeng
	 * @date 2013-3-19
	 * @param newsid 
	 * @param start
	 * @param limit
	 */
	/**
	 * 
	 */
	public void queryBypage(){
		List<com.android.server.po.Comment> commentList = commentService.queryByPage(newsid, start, limit);
		JSONObject model = new JSONObject();
		JSONArray array = new JSONArray();
		for (com.android.server.po.Comment comment : commentList) {
			JSONObject o = new JSONObject();
			o.put("id", comment.getId()+"");
			o.put("newsid",comment.getNewsid()+"");
			o.put("userid",comment.getUserid()+"");
			o.put("username",comment.getUsername()+"");
			o.put("content",comment.getContent());
			o.put("createtime",comment.getCreatetime());
			array.add(o);
		}
		model.put("total",commentService.getCommentCounts(newsid)+"");
		model.put("rows", array);
		//System.err.println(model.toString());
		write2Client(model.toString());
	}
	
	/**
	 * @description 删除指定评论
	 * @return
	 */
	public void commentDelete(){
		commentService.delete(id);
	}
	
	
	
	
	
	
	
	
	
	
	
	public int getStart() {
		return start;
	}


	public void setStart(int start) {
		this.start = start;
	}


	public int getLimit() {
		return limit;
	}


	public void setLimit(int limit) {
		this.limit = limit;
	}


	public ICommentService getCommentService() {
		return commentService;
	}
	public void setCommentService(ICommentService commentService) {
		this.commentService = commentService;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNewsid() {
		return newsid;
	}
	public void setNewsid(int newsid) {
		this.newsid = newsid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	

}
