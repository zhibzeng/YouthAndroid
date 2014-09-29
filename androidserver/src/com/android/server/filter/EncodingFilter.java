package com.android.server.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class EncodingFilter implements Filter {
   FilterConfig config = null;
   // default to GBK
   private String targetEncoding = "UTF-8";

   public void init(FilterConfig config) throws ServletException {
       this.config = config;
       this.targetEncoding = config.getInitParameter("encoding");
   }

   public void destroy() {
       config = null;
       targetEncoding = null;
   }

   public void doFilter(ServletRequest srequest, ServletResponse sresponse,
                        FilterChain chain) throws IOException, ServletException {

       HttpServletRequest request = (HttpServletRequest)srequest;
       request.setCharacterEncoding("UTF-8");
       chain.doFilter(srequest, sresponse);
   }

public FilterConfig getConfig() {
	return config;
}

public void setConfig(FilterConfig config) {
	this.config = config;
}

public String getTargetEncoding() {
	return targetEncoding;
}

public void setTargetEncoding(String targetEncoding) {
	this.targetEncoding = targetEncoding;
}
   
}