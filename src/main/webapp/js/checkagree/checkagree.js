$(function(){
	$.ajax({
		type: "post",
		url: "/news/user/isLogin",
		data: {},
		dataType: 'json',
		success: function(result) {
			if(result && result.code == 200){
				getAgreeNews(0, allNum);
				getCollectNews(0, allNum);
			}else{
				alert(result.message);
				window.location.href="login.html";
			}
		}
	});
});

var agreeStartNum = 0;
var collectStartNum = 0;
var allNum = 15;
var isAgreeLastPage = false;
var isCollectLastPage = false;

$('#agree').click(function (e) {
	  $(this).tab('show');
});
$('#collect').click(function (e) {
	  $(this).tab('show');
});
$('#prevAgree').click(function () {
	if(agreeStartNum == 0){
		alert("已是最前一页");
		return;
	}
	if(isAgreeLastPage){
		isAgreeLastPage = false;
	}
	agreeStartNum = agreeStartNum - allNum;
	getArgeeNews(agreeStartNum, allNum);
});
$('#nextAgree').click(function () {
	if(isAgreeLastPage){
		alert("已是最后一页");
		return;
	}
	agreeStartNum = agreeStartNum + allNum;
	getArgeeNews(agreeStartNum, allNum);
});
$('#prevCollect').click(function () {
	if(collectStartNum == 0){
		alert("已是最前一页");
		return;
	}
	if(isCollectLastPage){
		isCollectLastPage = false;
	}
	collectStartNum = collectStartNum - allNum;
	getCollectNews(collectStartNum, allNum);
});
$('#nextCollect').click(function () {
	if(isCollectLastPage){
		alert("已是最后一页");
		return;
	}
	collectStartNum = collectStartNum + allNum;
	getCollectNews(collectStartNum, allNum);
});


//获取已点赞新闻
function getAgreeNews(agreeStartNum, allNum){
	$('#agreeInfo').html('');
	$.ajax({
		type: "post",
		url: "/news/news/getagreeorcollectnews",
		data: {"startNum": agreeStartNum, "allNum": allNum, "type": 2},
		dataType: 'json',
		success: function(result) {
			if(result && result.info){
				var data = result.info;
				if(data.length < allNum){
					isAgreeLastPage = true;
				}
				for(var i=0; i<data.length; i++){
					var info = data[i];
					var oTr = $('<tr>').appendTo($('#agreeInfo'));
					if(i % 2 == 0){
						oTr.addClass('info');
					}
					var oTd1 = $('<td>').appendTo($(oTr));
					var oA = $('<a>').attr('href', "singlepage.html?id=" + info.id).attr('title', info.title).attr('target', "_blank").append(info.title).appendTo($(oTd1));
					var oTd2 = $('<td>').append(newsType2Name(info.type)).appendTo($(oTr));
					var oTd3 = $('<td>').append(info.agree).appendTo($(oTr));
					var oTd4 = $('<td>').append(info.collect).appendTo($(oTr));
					var oTd5 = $('<td>').append(formatDate(info.createTime)).appendTo($(oTr));
				}
			}
		}
	});
}

//获取已收藏新闻
function getCollectNews(collectStartNum, allNum){
	$('#collectInfo').html('');
	$.ajax({
		type: "post",
		url: "/news/news/getagreeorcollectnews",
		data: {"startNum": collectStartNum, "allNum": allNum, "type": 3},
		dataType: 'json',
		success: function(result) {
			if(result && result.info){
				var data = result.info;
				if(data.length < allNum){
					isCollectLastPage = true;
				}
				for(var i=0; i<data.length; i++){
					var info = data[i];
					var oTr = $('<tr>').appendTo($('#collectInfo'));
					if(i % 2 == 0){
						oTr.addClass('info');
					}
					var oTd1 = $('<td>').appendTo($(oTr));
					var oA = $('<a>').attr('href', "singlepage.html?id=" + info.id).attr('title', info.title).attr('target', "_blank").append(info.title).appendTo($(oTd1));
					var oTd2 = $('<td>').append(newsType2Name(info.type)).appendTo($(oTr));
					var oTd3 = $('<td>').append(info.agree).appendTo($(oTr));
					var oTd4 = $('<td>').append(info.collect).appendTo($(oTr));
					var oTd5 = $('<td>').append(formatDate(info.createTime)).appendTo($(oTr));
				}
			}
		}
	});
}

//将新闻类型转换为类型名称
function newsType2Name(type){
	var result;
	if(type == 0){
		result = '电子';
	}else if(type == 1){
		result = '互联网';
	}else if(type == 2){
		result = "软件";
	}else if(type == 3){
		result = '智能家居';
	}
	return result;
}

//格式化时间
function formatDate(millisecond) {
	var date = new Date(millisecond);
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	var hour = date.getHours();
	var minute = date.getMinutes();         
	var second = date.getSeconds();
	return year + "-" + formatTen(month) + "-" + formatTen(day) + " " + formatTen(hour)+ ":" + formatTen(minute)+ ":" + formatTen(second);
}
function formatTen(num) {
	return num > 9 ? (num + "") : ("0" + num);
}