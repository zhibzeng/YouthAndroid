<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 <%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>新闻列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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
  <th>新闻分类编号</th>
  <th>新闻分类名称</th>
  <th>操作</th>
  </tr>
  <s:iterator value="pageBean.list"  status="status">
  <tr>
  	<td><s:property value="id"/></td>
  	<td><s:property value="name"/></td>
  	<td><a href="NewsCategoryAction!updateShow.action?id=<s:property value="id"/>&name=<s:property value="name"/>">修改</a></td>
  </tr>
  </s:iterator>
</table>　

<table>
<tr><td>
共<s:property value="pageBean.allRow"/> 条记录
共<s:property value="pageBean.totalPage"/> 页
当前第<s:property value="pageBean.currentPage"/>页<br/>
</td>
<td>
　<s:if test="%{pageBean.currentPage == 1}">
	<!-- 第一页 上一页 -->
	</s:if>
　　　　<s:else>
　　　　　　<a href="NewsCategoryAction!queryPerPage.action?page=1">第一页</a>
　　　　　<a href="NewsCategoryAction!queryPerPage.action?page=<s:property value="%{pageBean.currentPage-1}"/>">上一页</a>
　　　　</s:else>
　<s:if test="%{pageBean.currentPage != pageBean.totalPage}">
　　　　　　<a href="NewsCategoryAction!queryPerPage.action?page=<s:property value="%{pageBean.currentPage+1}"/>">下一页</a>
　　　　　　<a href="NewsCategoryAction!queryPerPage.action?page=<s:property value="pageBean.totalPage"/>">最后一页</a>
　</s:if>
　<s:else>
　　　　　　<!-- 下一页 最后一页 -->
　</s:else>
</td>
</tr>
</table>

<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
