<%@ page language="java" contentType="text/html; charset=UTF-8"  
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
  
<html xmlns="http://www.w3.org/1999/xhtml">
<head><meta http-equiv="Content-Type" content="text/html; charset=utf-8" /><title>
	数据管理系统
</title>
<link href="/css/login.css" rel="stylesheet" type="text/css" />
<link href="/css/loginmin.css" rel="stylesheet" type="text/css" />
<link href="/css/loginstyle.css" rel="stylesheet" type="text/css" />
<link href="/css/iconfont.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript">
        function RefreshValidateCode() {
            $("#imgvalidatecode").attr("src", "/verify/validateCode?rand=" + new Date()/100);
            //.src = "ValidateCode.aspx?action=" + Math.random();
        }
    </script>
</head>
<body>
    <form method="post" action="Login.aspx" id="form1">

<script type="text/javascript">

var theForm = document.forms['form1'];
if (!theForm) {
    theForm = document.form1;
}
function __doPostBack(eventTarget, eventArgument) {
    if (!theForm.onsubmit || (theForm.onsubmit() != false)) {
        theForm.__EVENTTARGET.value = eventTarget;
        theForm.__EVENTARGUMENT.value = eventArgument;
        theForm.submit();
    }
}

</script>


        <input type="hidden" id="TenantId" name="TenantId" value="" />
        <div class="header">
        </div>
        <div class="loginWraper">
            <div id="loginform" class="loginBox">

                <div class="row cl">
                    <label class="form-label col-3">
                        <i class="Hui-iconfont" style="float: right; margin-right: 10px;">&#xe60d;</i></label>
                    <div class="formControls col-8">
                        <input name="txtUserName" type="text" id="txtUserName" placeholder="账户" class="input-text size-L"  />
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-3">
                        <i class="Hui-iconfont" style="float: right; margin-right: 10px;">&#xe60e;</i></label>
                    <div class="formControls col-8">
                        <input name="txtPassword" type="password" id="txtPassword" placeholder="密码" class="input-text size-L"  />
                    </div>
                </div>
                <div class="row cl">
                    <div class="formControls col-8 col-offset-3">
                        <input style="width:160px;" name="txtValidateCode" type="text" id="txtValidateCode" class="input-text size-L" placeholder="验证码" maxlength="5">

                        <img id="imgvalidatecode" alt="看不清？换一张！" title="看不清？换一张！" src="/verify/validateCode" style="cursor: pointer; width: 90px; height: 40px;" onclick="RefreshValidateCode();">
  

                        <a id="kanbuq" style="" href="javascript:RefreshValidateCode();">看不清，换一张</a>
                    </div>
                </div>
                <div class="row">
                    <div class="formControls col-8 col-offset-3">
                        <input type="button" id="btnLogin" value="&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;"
                            onclick="return ValidateInfo();" class="btn btn-success radius size-L" />
                        <input name="" type="reset" class="btn btn-default radius size-L" value="&nbsp;取&nbsp;&nbsp;&nbsp;&nbsp;消&nbsp;" />
                    </div>
                </div>
            </div>
        </div>
    


</form>

    <script type="text/javascript" src="/js/jquery/1.9.1/jquery.min.js"></script>
</body>
</html>
<script language="javascript" type="text/javascript">
    //验证用户
    function ValidateInfo() {
        var userName = document.getElementById("txtUserName").value;
        var pwd = document.getElementById("txtPassword").value;
        var valiCode = document.getElementById("txtValidateCode").value;

        if (userName == "") {
            alert("用户名不能为空，请重新输入。");
            $('#txtUserName').focus();
            return false;
        }
        if (pwd == "") {
            alert("密码不能为空，请重新输入。");
            $('#txtPassword').focus();
            return false;
        }
        if (valiCode == "") {
            alert("验证码不能为空，请重新输入。");
            $('#txtValidateCode').focus();
            return false;
        } else {
            $.ajax({
                type: 'post', //使用post方法访问后台
                url: '/theSpider/login', //要访问的后台地址
                data: {"username":userName,"password":pwd,"vcode":valiCode},
                async: false,
                success: function (res) {//msg为返回的数据，在这里做数据绑定
                    if (res.status != true) {
						alert(res.error);
                        RefreshValidateCode();
                        return false;
                    } else {
                        location.href = res.data.redirectTo;
                    }
                },
                error: function (dt, st) {
                    alert("登录失败！");
                }
            });
        }

    }

    $("body").keydown(function () {
        if (event.keyCode == "13") {//keyCode=13是回车键
            ValidateInfo();
            //$('#btnSumit').click();
        }
    });
</script>

  