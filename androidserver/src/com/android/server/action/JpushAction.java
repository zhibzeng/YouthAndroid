package com.android.server.action;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.aspectj.util.FileUtil;
import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

import com.android.server.Util.RandomTools;
import com.android.server.po.pushmessage;
import com.android.server.service.IPushMessageService;
import com.opensymphony.xwork2.ActionSupport;
public class JpushAction extends ActionSupport{
	private int sendNo = 1;			//(发送编号)
	private String imei = "";		// (IMEI字符串)
	private String msgTitle = "";	//(消息标题/通知标题)
	private String msgContent = ""; //(消息内容/通知内容)
	private String author;//发送人
	private IPushMessageService iPushMessageService;
	private List<pushmessage> pushmessages;
	/*
	 * 推送通知
	 * */
	public String send() throws UnsupportedEncodingException{
		JPushClient jpush = new JPushClient("swjtuyouth", "swjtuyouth", "83fbb9ee16b4095c957dc90e");
		//jpush.setEnableSSL(true);
		this.sendNo = new Random().nextInt(100000);
		String title=new String(msgTitle.getBytes("iso-8859-1"),"utf-8");
		String content=new String(msgContent.getBytes("iso-8859-1"),"utf-8");
		this.author=new String(this.author.getBytes("iso-8859-1"),"utf-8");
		MessageResult msgResult = jpush.sendNotificationWithAppKey(sendNo, title, content);
		pushmessage pushmessage= new pushmessage();
		pushmessage.setTitle(title);
		pushmessage.setContent(content);
		pushmessage.setAuthor(author);
		pushmessage.setSendtime(new Date());
		iPushMessageService.save(pushmessage);
		if (null != msgResult) {
		    if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
		        
		    	System.out.println("发送成功， sendNo=" + msgResult.getSendno());
		        
		    } else {
		        System.out.println("发送失败， 错误代码=" + msgResult.getErrcode() + ", 错误消息=" + msgResult.getErrmsg());
		    }
		} else {
		    System.out.println("无法获取数据");
		}
		return "success";
	}

	//获取推送通知列表
	public String getList(){
		pushmessages= new ArrayList<pushmessage>();
		pushmessages =iPushMessageService.listAll();
		return "listall";
	}
	public int getSendNo() {
		return sendNo;
	}

	public void setSendNo(int sendNo) {
		this.sendNo = sendNo;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public IPushMessageService getiPushMessageService() {
		return iPushMessageService;
	}

	public void setiPushMessageService(IPushMessageService iPushMessageService) {
		this.iPushMessageService = iPushMessageService;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<pushmessage> getPushmessages() {
		return pushmessages;
	}

	public void setPushmessages(List<pushmessage> pushmessages) {
		this.pushmessages = pushmessages;
	}
	
	
}
