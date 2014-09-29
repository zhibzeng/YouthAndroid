<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 <%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>新闻类别添加</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="res/css/bootstrap-responsive.min.css">
	<link rel="stylesheet" type="text/css" href="res/css/bootstrap.min.css">
  </head>
  
  <body>
  <jsp:include page="header.jsp"></jsp:include>
    
    <form action="NewsCategoryAction!update.action" method="post" enctype="multipart/form-data">
    <input name="id" id="id" type="hidden" value="<s:property value="id"/>" readonly="readonly"/>
    <label for="myFile">更新新闻类别图片</label>
    <input type="file" name="file" />
    <label>新闻类别图片</label>
   <img src="<s:property value="imagepath"/>" />
    <label>类别名称</label>
    <input name="name" id="name" type="text" value="<s:property value="name"/>"/>
    <input type="submit" class="btn btn-success" value="修改"/>    
    </form>
    
  <jsp:include page="footer.jsp"></jsp:include>
  </body>
</html>