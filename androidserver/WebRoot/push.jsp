<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 <%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>通知推送</title>
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
	<form action="pushMessage.action" method="GET">
   <table>
   		<tr>
   			<td><label>通知标题</label></td>
   			<td><input name="msgTitle" id="msgTitle" type="text"/></td>
   		</tr>
   		<tr>
   			<td><label>发送人</label></td>
   			<td> <input name="author" id="author" type="text"/></td>
   		</tr>
   		<tr>
   			<td><label>通知内容</label></td>
   			<td><textarea id="msgContent" name="msgContent"  rows="10" cols="20"></textarea></td>
   		</tr>
   		<tr>
   			<td><input type="reset" class="btn btn-danger" value="重置"/></td>
   			<td><input type="submit"  class="btn btn-success" value="提交"/> </td>
   		</tr>
   </table>
  
	</form>
	</center>
  <jsp:include page="footer.jsp"></jsp:include>
  </body>
</html>
