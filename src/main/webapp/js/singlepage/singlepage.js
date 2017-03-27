$(function() {
	getContent();
	getComment();
	getRecommendNews();
	getVideoCourse();
	getHotJob();
	setUserRecord();
	getAgreeAndCollectState();
});

var storage = window.localStorage;
var username = storage.getItem("username");
if(username){
	$('#username').html(username);
}

// 总点赞和收藏数
var totleAgree = 0;
var totleCollect = 0;
// 当前用户是否已点击和收藏
var isAgree = false;
var isCollect = false;
// 是否可以点击，防止用户操作过快
var isAgreeEnableClick = true;
var isCollectEnableClick = true;

var newsId;
var param = getParam();
if(null != param && null != param.id && '' != param.id){
	newsId = param.id;
}
//获取新闻内容
function getContent(){
	$.ajax({
		type: "post",
		url: "/news/news/getnewsbyid",
		data: {"newsId": newsId},
		dataType: 'json',
		success: function(result) {
			if(result && result.info){
				var info = result.info;
				$('#artical_title').html(info.title);
				$('#artical_content').html(info.content);
				if(info.keyword){
					var keywordArr = info.keyword.split(',');
					if(keywordArr){
						$('<span>').append('*').appendTo($('#artical_keyword'));
						for(var i=0; i<keywordArr.length; i++){
							$('<a>').attr('href', "index.html?keyword=" + keywordArr[i]).append(keywordArr[i]).attr('target', "_blank").appendTo($('#artical_keyword'));
							if(i != keywordArr.length-1){
								$('<span>').append('/').appendTo($('#artical_keyword'));
							}
						}
					}
					if(info.agree){
						totleAgree = info.agree;
						$('#agreeCount').html(totleAgree);
					}
					if(info.collect){
						totleCollect = info.collect;
						$('#collectCount').html(info.collect);
					}
				}
				
			}
		}
	});
}

//根据id设置该页面的评论
function getComment(){
	$('#comments').html('');
	$.ajax({
		type: "post",
		url: "/news/comment/getcommentsbynewsid",
		data: {"newsId": newsId},
		dataType: "json",
		success: function(data){
			var comments = data.info;
			$.each(comments, function(key, value){
				var oDiv = $('<div>').appendTo($('#comments'));
				var oH4 = $('<h4>').append(value.username + "&nbsp;&nbsp;&nbsp;&nbsp;" + value.createTime).appendTo($(oDiv));
				var op = $('<p>').append(value.content).appendTo($(oDiv));
			});
		}
	});
}

//获取推荐新闻
function getRecommendNews(){
	$.ajax({
		type: "post",
		url: "/news/news/getrecommendnews",
		data: {"newsId": newsId},
		dataType: 'json',
		success: function(result) {
			if(result && result.info){
				var data = result.info;
				for(var i=0; i<data.length; i++){
					var info = data[i];
					oDiv = $('<div>').addClass('bigbox').appendTo($('#recommend_news'));
					oA = $('<a>').attr('href', "singlepage.html?id=" + info.id).attr('title', info.title).attr('target', "_blank").appendTo($(oDiv));
					oImg = $('<img>').attr('src', info.src).appendTo($(oA));
					oA.append(info.title);
				}
			}
		}
	});
}

//获取视频教程
function getVideoCourse(){
	var oDiv = $('<div>').addClass('bigbox').appendTo($('#video_course'));
	var oA = $('<a>').attr('href', "http://edu.51cto.com/course/course_id-7347.html").attr('title', "VMware Site Recovery Manager 6.1").attr('target', "_blank").appendTo($(oDiv));
	var oImg = $('<img>').attr('src', 'http://s1.51cto.com/images/201610/e29b65236fdf5dafc37071293e63b3ff5e81d9.jpg').appendTo($(oDiv));
	oA.append("VMware Site Recovery Manager 6.1");
	
	var oDiv = $('<div>').addClass('bigbox').appendTo($('#video_course'));
	var oA = $('<a>').attr('href', "http://edu.51cto.com/course/course_id-7342.html").attr('title', "大学嵌入式物联网项目实训视频课程").attr('target', "_blank").appendTo($(oDiv));
	var oImg = $('<img>').attr('src', 'http://s1.51cto.com/images/201610/e1f7138402d1bcab018689f483a968756bb057.jpg').appendTo($(oDiv));
	oA.append("大学嵌入式物联网项目实训视频课程");
	
	var oDiv = $('<div>').addClass('bigbox').appendTo($('#video_course'));
	var oA = $('<a>').attr('href', "http://edu.51cto.com/course/course_id-7333.html").attr('title', "软考网络工程师之内存地址快速计算专题视频课程").attr('target', "_blank").appendTo($(oDiv));
	var oImg = $('<img>').attr('src', 'http://s1.51cto.com/images/201610/74a16456200bd975cd8464ea98848d76d06a44.jpg').appendTo($(oDiv));
	oA.append("软考网络工程师之内存地址快速计算专题视频课程");
}

//获取热门职位
function getHotJob(){
	var oDiv = $('<div>').addClass('jobbox').appendTo($('#hot_job'));
	var oJob = $('<a>').append('UI设计师').attr('href', 'http://gaozhao.51cto.com/job/view/id-4816.html').attr('target', "_blank").appendTo($(oDiv));
	var oInfo = $('<div>').append('全职/不限/不限		5k-10k		亮金信息').appendTo($(oDiv));
	var oDashed = $('<div>').append('----------------------------------------').appendTo($(oDiv));
	
	var oDiv = $('<div>').addClass('jobbox').appendTo($('#hot_job'));
	var oJob = $('<a>').append('高级C++开发工程师').attr('href', 'http://gaozhao.51cto.com/job/view/id-4816.html').attr('target', "_blank").appendTo($(oDiv));
	var oInfo = $('<div>').append('全职/5-10年/本科		15k-30k		神奇软件').appendTo($(oDiv));
	var oDashed = $('<div>').append('-----------------------------------------').appendTo($(oDiv));
	
	var oDiv = $('<div>').addClass('jobbox').appendTo($('#hot_job'));
	var oJob = $('<a>').append('产品经理-金融科技解决方案方向').attr('href', 'http://gaozhao.51cto.com/job/view/id-4816.html').attr('target', "_blank").appendTo($(oDiv));
	var oInfo = $('<div>').append('全职/5-10年/本科		30k-60k		新浪视觉').appendTo($(oDiv));
	var oDashed = $('<div>').append('-----------------------------------------').appendTo($(oDiv));
	
	var oDiv = $('<div>').addClass('jobbox').appendTo($('#hot_job'));
	var oJob = $('<a>').append('Java工程师').attr('href', 'http://gaozhao.51cto.com/job/view/id-4816.html').attr('target', "_blank").appendTo($(oDiv));
	var oInfo = $('<div>').append('全职/1-3年/本科		15k-20k		高达软件').appendTo($(oDiv));
	var oDashed = $('<div>').append('-----------------------------------------').appendTo($(oDiv));
	
	var oDiv = $('<div>').addClass('jobbox').appendTo($('#hot_job'));
	var oJob = $('<a>').append('WEB前端工程师').attr('href', 'http://gaozhao.51cto.com/job/view/id-4816.html').attr('target', "_blank").appendTo($(oDiv));
	var oInfo = $('<div>').append('全职/不限/不限		9k-15k		圆通集团').appendTo($(oDiv));
	var oDashed = $('<div>').append('-----------------------------------------').appendTo($(oDiv));
}

function submitComment() {
	if(username == null){
		alert('请先登录!');
		return;
	}
	var comment = $('#addComment').val();
	$.ajax({
		type: "post",
		url: "/news/comment/addcomment",
		data: {"comment": comment, "newsId": newsId},
		dataType: 'json',
		success: function(result) {
			if(result.code == 200){
				$('#addComment').val('');
				alert(result.message);
				getComment();
			}else{
				alert(result.message);
			}
		}
	});
}
 
//记录该新闻点击量，用户点击该类型新闻记录，用户点击新闻记录
function setUserRecord(){
	if(username != null){
		$.ajax({
			type: "post",
			url: "/news/user/setuserrecord",
			data: {"newsId": newsId},
			dataType: 'json',
			success: function(result) {
			}
		});
	}
}

function getAgreeAndCollectState(){
	if(username != null){
		$.ajax({
			type: "post",
			url: "/news/user/getagreeandcollectstate",
			data: {"newsId": newsId},
			dataType: 'json',
			success: function(result) {
				if(result.agreeClick){
					isAgree = true;
					$('#agreeCount').addClass('color_red');
				}
				if(result.collectClick){
					isCollect = true;
					$('#collectCount').addClass('color_red');
				}
			}
		});
	}
}


// 点赞/取消赞
function changeAgree(){
	if(username == null){
		alert('请先登录!');
		return;
	}
	if(!isAgreeEnableClick){
		alert('点击太快，请休息一下!');
		return;
	}
	isAgreeEnableClick = false;
	$.ajax({
		type: "post",
		url: "/news/user/changeagreeorcollectstate",
		data: {"newsId": newsId, "type": 2},
		dataType: 'json',
		success: function(result) {
			if(result.code == 400){
				alert(result.message);
			}else{
				if(isAgree == true){
					isAgree = false;
					totleAgree = totleAgree - 1;
					$('#agreeCount').html(totleAgree);
					$('#agreeCount').removeClass('color_red');
				}else if(isAgree == false){
					isAgree = true;
					totleAgree = totleAgree + 1;
					$('#agreeCount').html(totleAgree);
					$('#agreeCount').addClass('color_red');
				}
			}
			isAgreeEnableClick = true;
		}
	});
}

// 收藏/取消收藏
function changeCollect(){
	if(username == null){
		alert('请先登录!');
		return;
	}
	if(!isCollectEnableClick){
		alert('点击太快，请休息一下!');
		return;
	}
	isCollectEnableClick = false;
	$.ajax({
		type: "post",
		url: "/news/user/changeagreeorcollectstate",
		data: {"newsId": newsId, "type": 3},
		dataType: 'json',
		success: function(result) {
			if(result.code == 400){
				alert(result.message);
			}else{
				if(isCollect == true){
					isCollect = false;
					totleCollect = totleCollect - 1;
					$('#collectCount').html(totleCollect);
					$('#collectCount').removeClass('color_red');
				}else if(isCollect == false){
					isCollect = true;
					totleCollect = totleCollect + 1;
					$('#collectCount').html(totleCollect);
					$('#collectCount').addClass('color_red');
				}
			}
			isCollectEnableClick = true;
		}
	});
}

function loginOut(){
	var username = storage.getItem("username");
	if(!username){
		alert("未登录无须退出!");
		return;
	}
	$.ajax({
		type: "post",
		url: "/news/user/loginout",
		data:{},
		dataType: 'json',
		success: function(result){
			if(result && result.code == 200){
				storage.removeItem("username");
				location.reload();
			}
		}
	});
}

// 回到顶部
function toTop(){
	window.scrollTo(0, 0);
}

//获取从主页传过来的参数
function getParam() {
	var url = location.search;
 	var param = new Object();
 	if (url.indexOf("?") != -1) {
 		var str = url.substr(1);
 		strs = str.split("&");
 		for(var i = 0; i < strs.length; i ++) { 
 			param[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
 		}
 	}
 	return param;
}

//格式化时间
function formatTen(num) {
	return num > 9 ? (num + "") : ("0" + num);
}
function formatDate(date) {
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	var hour = date.getHours();
	var minute = date.getMinutes();         
	var second = date.getSeconds();
	return year + "-" + formatTen(month) + "-" + formatTen(day) + " " + formatTen(hour)+ ":" + formatTen(minute)+ ":" + formatTen(second);
}
