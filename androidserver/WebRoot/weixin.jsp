<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>YouthApp</title>
		<link type="text/css" rel="stylesheet" href="res/css/weixin.css" />
		<!--jQuery-->
		<script type="text/javascript" src="res/js/jquery.js"></script>
		<!--jQuery动画暂停插件-->
		<script type="text/javascript" src="res/js/jquery.pause.min.js"></script>
		<!--滚动效果js-->
		<script type="text/javascript" src="res/js/weiboscroll.js"></script>

<script>
	$(document).ready(function() {
		$.getJSON('getNews?category=23&length=20', function(data) {
			$.each(data.rows, function(idx, item) {
			if(idx==0){return true;}
			if(item.image.length==0){return true;}
			$("#list").append("<li><div class=&quot;div_left&quot;><a href="+item.image[0].path+"> <img src="+item.image[0].path+"title=&quot;Jarvis_风&quot;></a</div><div class=&quot;div_right&quot;><a href=&quot;#&quot; target=&quot;_blank&quot;>"+item.image[0].path+":</a>"+item.title+"<div class=&quot;twit_item_time&quot;>3分钟前</div></div></li>");
			});
		});
	});
</script>
		
		
	</head>
	<body>
		<div id="box_title">
			大家正在说
		</div>
		<div id="con">
			<div class="bottomcover" style="z-index: 2;"></div>
			<ul id="list">
			<!-- 
				<li>
					<div class="div_left">
						<a href="http://weibo.com/1777258407"> <img
								src="http://tp4.sinaimg.cn/1777258407/50/5627346616/1"
								title="Jarvis_风"> </a>
					</div>
					<div class="div_right">
						<a href="http://weibo.com/1866580377" target="_blank">Jarvis_风</a>：唉，总是在越忙的时候事情越多。。。我的神曲你在哪里呀~~~亲耐滴&middot;你再不出来你就木有人疼了！！#248#
						<div class="twit_item_time">
							3分钟前
						</div>
					</div>
				</li>
				 -->
			</ul>
		</div>

	</body>
</html>