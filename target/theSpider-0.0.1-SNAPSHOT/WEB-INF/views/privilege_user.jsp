<%@ page language="java" contentType="text/html; charset=UTF-8"  
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<jsp:include page="header.jsp" />
	<link href="/css/modal.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="/js/modal.js"></script>
	<script type="text/javascript" src="/js/select_box.js"></script>
	<title>用户权限管理</title>
</head> 
<body>
  <jsp:include page="share.jsp" />
  <section class="Hui-article-box">
    <div id="iframe_box" class="Hui-article">
      <form method="post" id="form1">
        <nav class="breadcrumb">
          <i class="Hui-iconfont"></i> 首页
          <span class="c-gray en">&gt;</span> 用户管理
          <span class="c-gray en">&gt;</span>用户权限管理
        </nav>
        <div class="pd-20">
        	<div class="mt-5">
        		<div class="form_li clearfix">
                <ul class="clearfix">
                  <li class="left_txt">用户名 ： </li>
                    <li class="right_inp"><input type="text" class="input-text" id="username" value=""></li>
                </ul>
                <div class="set_permissions first_permission">
                  <h3>设置实时数据分析权限：</h3>
                  <div class="set_list">
                    <span class="show_dimension">
                          <input type="checkbox" name="real" id="aaa" value="/flight"><label for="aaa">航班查询</label>
                    </span>
                    <span class="show_dimension">
                         <input type="checkbox" name="real" id="bbb" value="/finance"><label for="bbb">汇率管理</label>
                    </span>
                    <span class="show_dimension">
                         <input type="checkbox" name="real" id="ccc" value="/leg"><label for="ccc">航段管理</label>
                    </span>
                    <span class="show_dimension">
                          <input type="checkbox" name="real"" id="ddd" value="/city/airport"><label for="ddd">三字码管理</label>
                    </span>
                    <span class="show_dimension">
                         <input type="checkbox" name="real"" id="eee" value="/job"><label for="eee">爬虫任务管理</label>
                    </span>
                  </div>
                </div>
                <div class="set_permissions">
                  <h3>设置报表数据分析管理权限：</h3>
                  <div class="set_list">
                      <span class="show_dimension">
                            <input type="checkbox" name="report"" id="fff" value="/report/leg"><label for="fff">指定航段价格趋势监控</label>
                      </span>
                      <span class="show_dimension">
                           <input type="checkbox" name="report"" id="ggg" value="/report/area"><label for="ggg">区域市场价格趋势监控</label>
                      </span>
                      <span class="show_dimension">
                           <input type="checkbox" name="report"" id="hhh" value="/report/excel"><label for="hhh">指定航线价格监控</label>
                      </span>
                  </div>
                </div>
                <div class="set_permissions">
                  <h3>设置用户管理权限：</h3>
                  <div class="set_list">
                      <span class="show_dimension">
                            <input type="checkbox" name="user"" id="iii" value="/user"><label for="iii">用户权限管理</label>
                      </span>
                  </div>
                </div>

                <ul class="clearfix sub_box">
                    <li><button type="button" class="btn size-L btn-success" id="saveInfo">提交设置</button></li>
                </ul>
            </div>
        	</div>
        </div>
      </form>
    </div>
  </section>
<script type="application/javascript">  
  $(document).ready(function(){	
  	$("#saveInfo").click(function(){
    	var username = $("#username").val();
    	var	privileges = [];
    	
    	$("input:checkbox[name='real']:checked").each(function() {
    		if($(this).val() == "/city/airport"){
    			privileges.push("/city");
    			privileges.push("/airport");
    		}
    		else{
    			privileges.push($(this).val());
    		}
		});
    	
    	$("input:checkbox[name='report']:checked").each(function() {
    		privileges.push($(this).val());
		});
    	
    	$("input:checkbox[name='user']:checked").each(function() {
    		privileges.push($(this).val());
		});
    	
    	if(!username){
    		alert("请填写并发数");
    	}
    	else if(!privileges,length){
    		alert("请填写抓取间隔");
    	}
    	else{
    		// ajax请求
    		$.ajax({
    			cache: true,
    			type: "POST",
    			url: "/user/savePrivilege",
    			data:{
    				username: username,
    				privileges: privileges.toString()
    			},
    			success: function(response) {
    				if(response){
    					if(response == "unlogin"){
    	        			location.href = "/";
    	        			return;
    	        		}
    					alert("保存成功！");
    					document.location.reload();
    				}
    				else{
    					alert("保存失败！");
    				}
    			}
    		});
    	}
    });
  });
</script>
  <script type="text/javascript" src="/js/select_pop.js"></script>
  <script type="text/javascript" src="/js/left_menu.js"></script>
  <script type="text/javascript" src="/js/left_fold.js"></script>
</body>
</html>