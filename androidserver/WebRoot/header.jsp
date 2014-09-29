<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
  <head>
  <title>青年传媒新闻发布系统</title>
	<link rel="stylesheet" type="text/css"
	href="res/css/default/om-default.css" />
	<script src="res/js/jquery.min.js"></script>
	<script src="res/js/operamasks-ui.min.js"></script>
	<script src="res/development-bundle/ui/editor/omeditor.js "></script>
   <style>
   	html, body{ width: 100%; height: 100%; padding: 0; margin: 0;overflow: hidden;}
    
   		
		#north-panel{
			background: url("res/img/headerBg.jpg") repeat scroll 0 0;
			text-align: center;

		}

		.om-borderlayout-region-west .om-borderlayout-region-header{

		 	padding: 0px;

		 	border: 0;

		 	height: 28px;

		 	line-height: 28px;

		 	background: url("res/img/left-menuBg.jpg") repeat scroll 0 0 #FFFFFF;

		 	border-right:1px solid #99A8BB;

		 	font-size: 14px;

		 	font-weight: bold;

		 	cursor: pointer;

		}
		.om-accordion{	background: url("res/img/left-menuBg.jpg") repeat scroll 0 0 }
		.nav-panel{background: url("res/img/left-menuBg.jpg") repeat scroll 0 0 }

		.nav-item{margin-top:10px;}
		.nav-item a{text-decoration:none;color:black;}
    </style>

    <script type="text/javascript">
    $(document).ready(function() {
            var element = $('body').omBorderLayout({
           	   panels:[{
           	        id:"north-panel",
           	        region:"north",
           	        header : false,
           	        height : 80

           	    },{

           	        id:"south-panel",

           	        title:"This is south panel",

           	        region:"south",

           	        resizable:true,

           	        collapsible:true,

           	        height:80,

           	        header:false

           	    },{

           	        id:"center-panel",
           	     	header:false,
           	        region:"center"

           	    },{

           	        id:"west-panel",

           	        resizable:true,

           	        collapsible:true,

           	        title:"导航条",

           	        region:"west",

           	        expandToBottom : true,

           	        width:170

           	    }],

           	    hideCollapsBtn : true,

           	    spacing : 8

            });

            // 默认关闭下面的面板

            element.omBorderLayout("collapseRegion","south");

            // 导航面板里由5个单独的panel构成

            var menuPanel = [{id:"nav-panel-1" , title:"新闻分类"},

                            // {id:"nav-panel-2" , title:"项目管理"},

                             //{id:"nav-panel-3" , title:"组织机构"},

                             //{id:"nav-panel-4" , title:"主营业务"},

                             {id:"nav-panel-5" , title:"新闻内容"}];

            $(menuPanel).each(function(index , panel){

                $("#"+panel.id).omPanel({

                    title : panel.title,

                    collapsible:true,

                    // 面板收缩和展开的时候重新计算自定义滚动条是否显示

                    onCollapse : function(){

                        $("#west-panel").omScrollbar("refresh");

                    },

                    onExpand : function(){

                        $("#west-panel").omScrollbar("refresh");

                    }

                });

            });
            
            $("#help-panel").omPanel({

            	title : "欢迎信息",collapsible:true,iconCls:"icon-help",_helpMsg:true

            });

 
            $("span#save").omButton({width:100,height:30});

            // 导航面板使用自定义滚动条

            $("#west-panel").omScrollbar();

            });

    </script>

  </head>
  
  <body>

   	<div id="north-panel">
   	<br/>
   	<h2>青年传媒新闻发布系统</h2>
   	</div>
   	<div id="center-panel" style="padding: 5px 10px 0px 10px;">
    	<div id="center-tab">
	         <div id="tab1" style="padding: 0;position: relative;">
		   		<div id="help-panel">感谢您使用青年传媒新闻发布系统！</div>
	   		</div>
		</div>