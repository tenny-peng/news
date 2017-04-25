var storage = window.localStorage;
function login(){
	if(!$("#username").val()){
		alert("请输入用户名");
		return;
	}
	if(!$("#password").val()){
		alert("请输入密码");
		return;
	}
	$.ajax({
		type: "post",
		url: "/news/user/login",
		data: {username: $("#username").val(), password: $("#password").val()},
		dataType: 'json',
		success: function(result) {
			if(result.code == 200){
				storage.setItem("username", $("#username").val());
				if(result.userType == 1){
					window.location.href = "manager.html";
				}else{
					window.location.href = "index.html"; 
				}
			}else{
				alert(result.message);
			}
		}
	});
}

function register() {
	if(!$("#s_username").val()){
		alert("请输入用户名");
		return;
	}
	if(!$("#s_password").val()){
		alert("请输入密码");
		return;
	}
	if(!$("#s_password_comfirm").val()){
		alert("请确认密码");
		return;
	}
	if($("#s_password").val() != $("#s_password_comfirm").val()){
		alert("两次密码不一致，请确认");
		return;
	}
	$.ajax({
		type: "post",
		url: "/news/user/adduser",
		data: {username: $("#s_username").val(), password: $("#s_password").val()},
		dataType: 'json',
		success: function(result) {
			if(result.code == 200){
				storage.setItem("username", $("#s_username").val());
				window.location.href="index.html";
			}else{
				alert(result.message);
			}
		}
	});
}
	
