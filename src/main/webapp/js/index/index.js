$(function() {
    $(window).bind('scroll', function(){
    	onScroll();
    });
});

//定义下拉加载的数据变量
var storage = window.localStorage;
var username = storage.getItem("username");
if(username){
	$('#username').html(username);
}
var startNum = 0;
var allNum = 30;
var type = null;

var queryWord = "";
var searchFlag = false;
var param = getParam();
if(param && param.keyword){
	$('#queryWord').val(param.keyword);
	queryWord = param.keyword;
	queryNews();
}else{
	getNews();
}


//瀑布流布局
function waterFlow(){
	var container = $('#masonry');
	container.imagesLoaded(function() {
		container.masonry({
			itemSelector: '.box',
			isAnimated: true
		});
	});
}

function getNews(){
	// 如果曾经有过搜索，将搜索标识置为false
	if(searchFlag){
		searchFlag = false;
	}
	$.ajax({
		type: "post",
		url: "/news/news/getpersonalnews",
		data: {'startNum': startNum, 'allNum': allNum},
		dataType: 'json',
		success: function(result) {
			if(result && result.info){
				var data = result.info;
				for(var i=0; i<data.length; i++){
					var info = data[i];
					oDiv = $('<div>').addClass('box').appendTo($('#masonry'));
					oA = $('<a>').attr('href', "singlepage.html?id=" + info.id).attr('title', info.title).attr('target', "_blank").appendTo($(oDiv));
					oImg = $('<img>').attr('src', info.src).appendTo($(oA));
					oA.append(info.title);
				}
				changeNum();
			}
		}
	});
}

function changeNum(){
	if(null !=  startNum){
		startNum = startNum + 30;
	}
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

// 判断页面滚到底部加载新闻
function onScroll() {
    var winHeight = window.innerHeight ? window.innerHeight : $(window).height(),
	closeToBottom = ($(window).scrollTop() + winHeight > $(document).height() - 500);
    if (closeToBottom) {
    	// 如果进行过类型过滤或单词搜素，就继续再此基础上加载新闻，否则按常规加载
    	if(searchFlag){
    		queryNews();
    	}else{
    		getNews();
    	}
    }
};

// 回到顶部
function toTop(){
	window.scrollTo(0, 0);
}

// 搜索框回车
function onKeyDown(event){
	var e = event || window.event || arguments.callee.caller.arguments[0];
	if(e && e.keyCode==27){ // 按 Esc 
		//要做的事情
	}
	if(e && e.keyCode==113){ // 按 F2 
		//要做的事情
	}            
	if(e && e.keyCode==13){ // enter 键
		queryNews();
	}                 
}

//获取从主页传过来的参数
function getParam() {
	var url = location.search;
 	var param = new Object();
 	if (url.indexOf("?") != -1) { 
 		var str = url.substr(1);
 		strs = str.split("&");
 		for(var i = 0; i < strs.length; i ++) { 
 			param[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
 		}
 	}
 	return param;
}

//格式化时间
function formatDate(date) {
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

//根据类型，关键字搜索
function queryNews(){
	// 如果是第一次搜索，将搜索标识置为true，并将startNum归0
	if(!searchFlag){
		searchFlag = true;
	}
	$.ajax({
		type: "post",
		url: "/news/news/getnewsbylimit",
		data: {"startNum": startNum, "allNum": allNum, "type": type, "queryWord": queryWord},
		dataType: 'json',
		success: function(result) {
			if(result && result.info){
				var data = result.info;
				for(var i=0; i<data.length; i++){
					var info = data[i];
					oDiv = $('<div>').addClass('box').appendTo($('#masonry'));
					oA = $('<a>').attr('href', "singlepage.html?id=" + info.id).attr('title', info.title).attr('target', "_blank").appendTo($(oDiv));
					oImg = $('<img>').attr('src', info.src).appendTo($(oA));
					oA.append(info.title);
				}
				changeNum();
			}
		}
	});
}

$('#value99').click(function(){
	$('#value99').addClass('active');
	$('#value0').removeClass('active');
	$('#value1').removeClass('active');
	$('#value2').removeClass('active');
	$('#value3').removeClass('active');
	$('#masonry').html('');
	$('#queryWord').val('');
	startNum = 0;
	type = 0;
	queryWord = '';
	getNews();
 });

$('#value0').click(function(){
	$('#value99').removeClass('active');
	$('#value0').addClass('active');
	$('#value1').removeClass('active');
	$('#value2').removeClass('active');
	$('#value3').removeClass('active');
	$('#masonry').html('');
	$('#queryWord').val('');
	startNum = 0;
	type = 0;
	queryWord = '';
	queryNews();
 });
  
$('#value1').click(function(){
	$('#value99').removeClass('active');
	$('#value0').removeClass('active');
	$('#value1').addClass('active');
	$('#value2').removeClass('active');
	$('#value3').removeClass('active');
	$('#masonry').html('');
	$('#queryWord').val('');
	startNum = 0;
	type = 1;
	queryWord = '';
	queryNews();
});
  
$('#value2').click(function(){
	$('#value99').removeClass('active');
	$('#value0').removeClass('active');
	$('#value1').removeClass('active');
	$('#value2').addClass('active');
	$('#value3').removeClass('active');
	$('#masonry').html('');
	$('#queryWord').val('');
	startNum = 0;
	type = 2;
	queryWord = '';
	queryNews();
});
$('#value3').click(function(){
	$('#value99').removeClass('active');
	$('#value0').removeClass('active');
	$('#value1').removeClass('active');
	$('#value2').removeClass('active');
	$('#value3').addClass('active');
	$('#masonry').html('');
	$('#queryWord').val('');
	startNum = 0;
	type = 3;
	queryWord = '';
	queryNews();
});
  
$('#search').click(function(){
	$('#value99').removeClass('active');
	$('#value0').removeClass('active');
	$('#value1').removeClass('active');
	$('#value2').removeClass('active');
	$('#value3').removeClass('active');
	$('#masonry').html('');
	startNum = 0;
	type = null;
	queryWord = $('#queryWord').val();
	queryNews();
});