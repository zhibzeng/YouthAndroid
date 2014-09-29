<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 <%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>新闻列表</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="res/css/bootstrap-responsive.min.css">
	<link rel="stylesheet" type="text/css" href="res/css/bootstrap.min.css">
	<STYLE type="text/css">
		a{text-decoration: none;}
		a:HOVER {text-decoration: none;}
	</STYLE>
  </head>
  <body>
   <jsp:include page="header.jsp"></jsp:include>
  <table style="width:800px;border:1px solid silver;" class="table table-condensed">
  <tr>
  <th>编号</th>
  <th>标题</th>
  <th>推送内容</th>
  <th>推送时间</th>
  <th>发送人</th>
  </tr>
  <s:iterator value="pushmessages"  status="status">
  <tr>
  	<td><s:property value="id"/></td>
  	<td><s:property value="title"/></td>
  	<td><s:property value="content"/></td>
  	<td><s:property value="sendtime"/></td>
  	<td><s:property value="author"/></td>
  </tr>
  </s:iterator>
</table>　

<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
