$(function(){
	$.ajax({
		type: "post",
		url: "/news/user/isAdminLogin",
		data: {},
		dataType: 'json',
		success: function(result) {
			if(result && result.code == 200){
				getNews(0, allNum);
				getUsers();
			}else{
				alert(result.message);
				window.location.href="login.html";
			}
		}
	});
});

var startNum = 0;
var allNum = 15;
var isLastPage = false;

$('#news').click(function (e) {
	//阻止冒泡事件，如打开，则a标签点击不跳转; bootstrap的data-toggle="tab"属性也会使a标签点击不跳转
//	  e.preventDefault();
	  $(this).tab('show');
});
$('#user').click(function (e) {
//	  e.preventDefault();
	  $(this).tab('show');
});
$('#prev').click(function () {
	if(startNum == 0){
		alert("已是最前一页");
		return;
	}
	if(isLastPage){
		isLastPage = false;
	}
	startNum = startNum - allNum;
	getNews(startNum, allNum);
});
$('#next').click(function () {
	if(isLastPage){
		alert("已是最后一页");
		return;
	}
	startNum = startNum + allNum;
	getNews(startNum, allNum);
});

//获取新闻信息
function getNews(startNum, allNum){
	$('#newsInfo').html('');
	$.ajax({
		type: "post",
		url: "/news/news/getnewsbylimit",
		data: {"startNum": startNum, "allNum": allNum},
		dataType: 'json',
		success: function(result) {
			if(result && result.info){
				var data = result.info;
				if(data.length < allNum){
					isLastPage = true;
				}
				for(var i=0; i<data.length; i++){
					var info = data[i];
					var oTr = $('<tr>').appendTo($('#newsInfo'));
					if(i % 2 == 0){
						oTr.addClass('info');
					}
					var oTd1 = $('<td>').appendTo($(oTr));
					var oA = $('<a>').attr('href', "singlepage.html?id=" + info.id).attr('title', info.title).attr('target', "_blank").append(info.title).appendTo($(oTd1));
					var oTd2 = $('<td>').append(newsType2Name(info.type)).appendTo($(oTr));
					var oTd3 = $('<td>').append(info.keyword).appendTo($(oTr));
					var oTd4 = $('<td>').append(info.click).appendTo($(oTr));
					var oTd5 = $('<td>').append(info.agree).appendTo($(oTr));
					var oTd6 = $('<td>').append(info.collect).appendTo($(oTr));
					var oTd7 = $('<td>').append(formatDate(info.createTime)).appendTo($(oTr));
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

// 获取所有用户信息
function getUsers(){
	$('#userInfo').html('');
	$.ajax({
		type: "post",
		url: "/news/user/getallusers",
		data: {},
		dataType: 'json',
		success: function(result) {
			if(result && result.info){
				var data = result.info;
				for(var i=0; i<data.length; i++){
					var info = data[i];
					var oTr = $('<tr>').appendTo($('#userInfo'));
					if(i % 2 == 0){
						oTr.addClass('info');
					}
					oTd1 = $('<td>').append(info.username).appendTo($(oTr));
					oTd2 = $('<td>').append(info.password).appendTo($(oTr));
					oTd3 = $('<td>').append(userType2Name(info.type)).appendTo($(oTr));
					oTd4 = $('<td>').append(formatDate(info.createTime)).appendTo($(oTr));
					oTd5 = $('<button>').attr('onclick', 'delUser(' + info.id + ')').addClass('btn btn-default btn-danger').append('删除').appendTo($(oTr));
				}
			}
		}
	});
}

function delUser(id){
	$.ajax({
		type: "post",
		url: "/news/user/deluserbyid",
		data: {"id": id},
		dataType: 'json',
		success: function(result) {
			if(result){
				getUsers();
				alert(result.message);
			}
		}
	});
}

// 将用户类型转换为类型名称
function userType2Name(type){
	var result;
	if(type == 1){
		result = '管理员';
	}else if(type == 2){
		result = '用户';
	}
	return result;
}

//从今日头条获取新闻
function getLastNews(){
	$('#myModal').modal('show');
	$.ajax({
		type: "post",
		url: "/news/manager/getinfo",
		data: {},
		dataType: 'json',
		success: function(result) {
			if(result && result.code == 200){
				getNews(0,15);
				$('#myModal').modal('hide');
			}else{
				alert(result.message);
			}
		}
	});
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