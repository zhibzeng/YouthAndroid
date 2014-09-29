<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'strutsTest.jsp' starting page</title>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  <body>
    <h3>Struts2 Test</h3>
    Contact:<s:property value="#session.customer.contact"/><br/>
    EMail:<s:property value="#session.customer.email"/>
    <hr/>
    <s:form action="SetTestAction!put.action" name="form1" method="POST">
    	<s:select list="citys" name="list" id="list"></s:select>
    	
    	<s:doubleselect label="级联表" labelposition="left"  name="country" list="{'中国','美国','韩国'}" 
     doubleList="top=='中国'?{'1','2','3'}:(top=='美国'?{'a','b','c'}:{'11','22','33'})" 
    	doubleName="city"></s:doubleselect>
    	
    	<s:submit></s:submit>
    
    </s:form>
    <s:actionerror/>
    <s:fielderror/>
    <s:form action="FileUpLoad" enctype="multipart/form-data">
    <s:file name="upload" label="选择文件" labelposition="left"></s:file>
    <s:file name="upload" label="选择文件" labelposition="left"></s:file>
    <s:file name="upload" label="选择文件" labelposition="left"></s:file>
    <s:submit value="上传"></s:submit>
    </s:form>
  </body>
</html>
