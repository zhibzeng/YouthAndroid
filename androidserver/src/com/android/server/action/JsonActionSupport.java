package com.android.server.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public abstract class JsonActionSupport extends ActionSupport {
	
	private static final long serialVersionUID = 1L;

	protected void write2Client(String str) {
		HttpServletResponse response = ServletActionContext.getResponse();
		PrintWriter writer;
		try {
		  	response.setContentType("text/html"); 
	        response.setCharacterEncoding("UTF-8");
			writer = response.getWriter();
			writer.write(str);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
