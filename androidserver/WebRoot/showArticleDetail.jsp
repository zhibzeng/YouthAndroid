<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
 <%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>新闻详情</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css"
	href="res/css/default/om-default.css" />
	<script src="res/js/jquery.min.js"></script>
	<script src="res/js/operamasks-ui.min.js"></script>
	<script src="res/development-bundle/ui/om-grid-roweditor.js"></script>
	<script src="res/development-bundle/ui/om-validate.js"></script>
	<link rel="stylesheet" type="text/css" href="res/css/bootstrap-responsive.min.css">
	<link rel="stylesheet" type="text/css" href="res/css/bootstrap.min.css">
	<STYLE type="text/css">
		a{text-decoration: none;}
		a:HOVER {text-decoration: none;}
	</STYLE>
	
	<% 
		ValueStack vs = (ValueStack)request.getAttribute("struts.valueStack");  
		String newsid = vs.findValue("contenDetail.id").toString();  
	%>
	
	<script type="text/javascript">
		$(document).ready(function() {
		
		 //页面AJAX提交数据
		function submitAjAx(url){
			$.get(url, function (data, textStatus){
				$('#showTable').omGrid('saveChanges');	
			});
		}
	
		$('#showTable').omGrid({
       	 	 	height : 450,
         		width : 800,
        		limit : 15, //分页显示，每页显示8条
        		showIndex : false,//不显示最左边的ID列
      			singleSelect :false, //出现checkbox列，可以选择同时多行记录
         		colModel : [
         		{header:'ID',name:'id',width:80,align :'center',editor:{editable:false}},
                {header:'用户编号',name:'userid',width:80,align :'center',editor:{editable:false}},
                {header:'用户名称',name:'username',width:150, align : 'center',editor:{editable:false}},
                {header:'评论内容',name:'content',width:'autoExpand',align :'center',editor:{editable:false}},
                {header:'评论时间',name:'createtime',width:200,align :'center',editor:{editable:false}}
         		],
         		dataSource : 'queryBypage?newsid='+<%=newsid%>,
          //展开行时使用下面的方法生成详情，必须返回一个字符串
            //rowDetailsProvider:function(rowData,rowIndex){
             //  return '项目名称:'+rowData.name+'<a href="ProjectView.jsp?projectid='+rowData.id+'&isPlanComplete='+rowData.isPlanComplete+'&projectName='+rowData.name+'" target="_blank">查看WBS详情</a>';
            //}
         
     	});
     $('#del').click(function(){
            	var dels = $('#showTable').omGrid('getSelections');
            	if(dels.length <= 0 ){
            		alert('请选择删除的记录！');
            		return;
            	}
            	$('#showTable').omGrid('deleteRow',dels);
            	var data = $('#showTable').omGrid('getChanges');       
           		 for(var i=0;i<data.delete.length;i++){
            		var id= data.delete[i].id;
            		submitAjAx("commentDelete?id="+id);
            		/*****保存成功之后执行如下操作********/
            		/******或者执行$('#grid').omGrid('reload');***/
            	}
            	
            	
            });
	});
	</script>
  </head>
  <body>
  <jsp:include page="header.jsp"></jsp:include>
  <center>
 <div id="news" style="width:850px;border:1px solid silver;margin-top:20px;">
  	<h2><s:property value="contenDetail.title"/></h2>
 	<div id="newsTop" style="margin:20px;">
 		<span style="margin-right:20px;"><b>新闻来源: </b><s:property value="contenDetail.comefrom"/></span>
 		<span style="margin-right:20px;"><b>发布时间: </b><s:property value="contenDetail.time"/></span>
  		<span style="margin-right:20px;"><b>新闻类别: </b><s:property value="newsCategoryName"/></span>
		<span style="margin-right:20px;"><a href="NewsContentPageAction!articleDelete.action?id=<s:property value="contenDetail.id"/>">删除该文章</a></span>
		<span style="margin-right:20px;"><a href="NewsContentPageAction!articlePreUpdate.action?id=<s:property value="contenDetail.id"/>">修改该文章</a></span>
 	</div> 	
 	<div id="newscontent">
 	<s:if test="%{newsImageList!=null}">
 		<div id="newsimage" style="width:800px;">
 		<s:iterator value="newsImageList" status="status">
 		<img alt="新闻配图" width="300px" height="200px" src="<s:property value="imagepath"/>">
 		</s:iterator>
 		</div>
 	</s:if>
 	<s:else>
 	</s:else>
 	<!-- 新闻录音 -->
 	<s:if test="%{newsAudioList!=null}">
 		<div id="newsaudio" style="width:800px;">
 		<s:iterator value="newsAudioList" status="status">
 		<a href="<s:property value="audiopath"/>">点击播放新闻录音</a>
 		</s:iterator>
 		</div>
 	</s:if>
 	<s:else>
 	</s:else>
 		<div id="newsmain" style="width:800px;margin:20px;" >
 			<div id="newscontent" style="width:800px;text-align: left" >
 			<pre style="text-align: left">
 				<s:property value="contenDetail.content" escape="false" />
 			</pre>
 			</div>
 			<!-- 判断是否有评论 -->
 			<s:if test="%{total!=0}">
  				<h3>新闻评论</h3>
 		  		<table id="showTable"></table>
 		  		<input type="button" id="del" value="删除"/>
 			</s:if>
 			<s:else>
 			</s:else>
 		</div>
     </div>
 </div>	
  </center>
  

  <jsp:include page="footer.jsp"></jsp:include>
  </body>
</html>
