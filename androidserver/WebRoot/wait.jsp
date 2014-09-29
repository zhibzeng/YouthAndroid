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
	<link rel="stylesheet" type="text/css" href="res/css/default/om-default.css" />
	<script src="res/js/jquery.min.js"></script>
	<script src="res/js/operamasks-ui.min.js"></script>
	<script src="res/development-bundle/ui/editor/omeditor.js "></script>
	<script type="text/javascript">
	$(function(){
            var editor = $('.editor').omEditor({
            width:800
            });
        });
	$(document).ready(function () {    
    /* 获得分类 */    
    $.post("NewsCategoryAction!listAll.action",function(data){    
        var jsonObj = eval("(" + data + ")");    
        for (var i = 0; i < jsonObj.length; i++) {    
            var $option = $("<option></option>");    
            $option.attr("value", jsonObj[i].id);    
            $option.text(jsonObj[i].name);    
            $("#category").append($option);    
        }    
      });  
    });  
	
	</script>
  </head>
  <body onload="showCategory()">
 	<!-- 新闻发布回调信息 -->
 	<span style="color:red;"><s:property value="message"/></span>
    <form action="NewsContentAction!save.action" method="post" enctype="multipart/form-data">
      <label for="myFile">新闻图片</label>
      <input type="file" name="file" />
      <!-- <input type="file" name="file" /> -->
      <br/>
      <label>新闻标题</label>
      <input type="text" name="title" id="title" />
      <br/>
      <label>新闻分类</label>
      <select id="category" name="category"></select> 
      <br/>
      <label>新闻内容</label>
 	 <textarea id="content" name="content" class="editor">&lt;p&gt;Initial value &lt;/p&gt;</textarea>
      <br/>
      <input type="reset" value="重置"/>
      <input type="submit" value="提交"/>
   </form>
</body>
</html>
