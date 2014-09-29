<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 <%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>新闻类别添加</title>
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
    <center>
    <div id="edit">
    <form action="NewsCategoryAction!save.action" method="post" enctype="multipart/form-data">
    <label for="myFile">新闻类别图片</label>
    <input type="file" name="file" />
    <label>新添类别名称</label>
    <input name="name" id="name" type="text"/>
    <input type="reset" class="btn btn-danger" value="重置"/>
    <input type="submit"  class="btn btn-success" value="提交"/>    
    </form>
    </div>
    </center>
  <jsp:include page="footer.jsp"></jsp:include>
  </body>
</html>
