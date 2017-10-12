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
	<title>汇率管理</title>
</head> 
<body>
  <jsp:include page="share.jsp" />
  <section class="Hui-article-box">
    <div id="iframe_box" class="Hui-article">
      <form method="post" action="OTAFareAnalysis.aspx" id="form1">
        <nav class="breadcrumb">
          <i class="Hui-iconfont"></i> 首页
          <span class="c-gray en">&gt;</span> 实时数据分析
          <span class="c-gray en">&gt;</span>汇率管理
        </nav>
        <div class="pd-20">
        	<div class="form_li clearfix">
        		<div class="checked_after">
        			<ul class="clearfix">
                  		<li class="left_txt">名称 ： </li>
                  		<li class="right_inp"><input type="text" id="name" name="name"  class="input-text" autocomplete="off" value="${ currency.getName() }"></li>
                	</ul>
                	<ul class="clearfix">
                  		<li class="left_txt">代码： </li>
                  		<li class="right_inp"><input type="text" id="code" name="code" class="input-text" autocomplete="off" value="${ currency.getCode() }"></li>
                	</ul>
                	<ul class="clearfix huilv">
                  		<li class="left_txt">汇率： </li>
                  		<li class="right_inp"><input type="text" id="rate" name="rate"  class="input-text" autocomplete="off" value="${ currency.getRate() }"><span name="check" id="check" class="btn btn-gray" style="margin-left:-3px;">查询实时汇率</span></li>
                	</ul>
                	<ul class="clearfix sub_box">
                  		<li><button type="button" id="confirm" class="btn size-L btn-success" /><c:if test="${ currency.getCode() != null }">修改</c:if><c:if test="${ currency.getCode() == null }">添加</c:if></button></li>
                	</ul>
        		</div>
        	</div>
        </div>
      </form>
    </div>
  </section>
<script type="text/javascript">
	$("#confirm").bind("click", function () { 
		var name = $("#name").val();
		var code = $("#code").val();
		var rate = $("#rate").val();
		
		if(!name) {
			alert("请填写名称");
		}
		else if(!code){
			alert("请填写代码");
		}
		else if(!rate){
			alert("请填写汇率");
		}
		
		$.ajax({
	        cache: true,
	        type: "POST",
	        url: "/finance/save",
	        data:{
	        	name: name,
	        	code: code,
	        	rate: rate
	        },
	        success: function(response) {
	            if(response){
	            	if(response == "unlogin"){
	        			location.href = "/";
	        			return;
	        		}
	            	if(response.data){
	            		document.location.href = "/finance/list";
	            	}
	            }else{
	                //alert(response.error);
	            }
	        }
	    });
	});
	
	$("#check").bind("click", function () {
		var code = $("#code").val();
		if(!code){
			alert("请填写代码");
		}
		$.ajax({
	        cache: true,
	        type: "POST",
	        url: "/finance/check",
	        data:{
	        	code: code
	        },
	        success: function(response) {
	            if(response){
	            	if(response.data){
	            		$("#rate").attr("value",response.data);
	            	}
	            	else{
	            		 alert("确认代码是否正确");
	            	}
	            }else{
	                alert("请求错误");
	            }
	        }
	    });
	});
</script>
  <script type="text/javascript" src="/js/select_pop.js"></script>
  <script type="text/javascript" src="/js/left_menu.js"></script>
  <script type="text/javascript" src="/js/left_fold.js"></script>
  <script type="text/javascript" src="/js/jquery.linq.min.js"></script>
</body>
</html>