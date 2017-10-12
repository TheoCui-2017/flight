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
	<title>航段管理</title>
</head> 
<body>
  <jsp:include page="share.jsp" />
  <section class="Hui-article-box">
    <div id="iframe_box" class="Hui-article">
      <form method="post" action="OTAFareAnalysis.aspx" id="form1">
        <nav class="breadcrumb">
          <i class="Hui-iconfont"></i> 首页
          <span class="c-gray en">&gt;</span> 实时数据分析
          <span class="c-gray en">&gt;</span>航段管理
        </nav>
        <div class="pd-20">
        	<div class="form_li clearfix">
        		<div class="checked_after">
        			<ul class="clearfix">
                  		<li class="left_txt">出发城市： </li>
                  		<li class="right_inp"><input type="text" id="txtDepartCity" name="txtDepartCity"  placeHolder="中文/拼音" class="input-text" autocomplete="off" <c:if test="${ leg.getArea() != null }">value="${leg.getDepartCity()}(${leg.getDepartCityCode()})"</c:if><c:if test="${ leg.getArea() == null }">value=""</c:if>></li>
                	</ul>
                	<ul class="clearfix">
                  		<li class="left_txt">到达城市： </li>
                  		<li class="right_inp"><input type="text" id="txtDestCity" name="txtDestCity"   placeHolder="中文/拼音" class="input-text" autocomplete="off" <c:if test="${ leg.getArea() != null }">value="${leg.getArrivalCity()}(${leg.getArrivalCityCode()})"</c:if><c:if test="${ leg.getArea() == null }">value=""</c:if>></li>
                	</ul>
                	<ul class="clearfix">
                  		<li class="left_txt">区域列表：</li>
                  		<li class="left_txt" style="width:260px;"></li>
                  		<span class="select-box inline">
                  			<select id="area" class="select" style="width: 208px; margin-top:0px; vertical-align:top;">
	                  			<option value="亚洲" <c:if test="${ leg.getArea().equals('亚洲')}">selected="selected"</c:if>>亚洲</option>
				               	<option value="欧洲" <c:if test="${ leg.getArea().equals('欧洲')}">selected="selected"</c:if>>欧洲</option>
				               	<option value="美洲" <c:if test="${ leg.getArea().equals('美洲')}">selected="selected"</c:if>>美洲</option>
				               	<option value="非洲" <c:if test="${ leg.getArea().equals('非洲')}">selected="selected"</c:if>>非洲</option>
				               	<option value="大洋洲" <c:if test="${ leg.getArea().equals('大洋洲')}">selected="selected"</c:if>>大洋洲</option>
				            </select>
                  		</span>
                	</ul>
                	<ul class="clearfix sub_box">
                  		<li><button type="button" id="confirm" class="btn size-L btn-success" /><c:if test="${ leg.getArea() != null }">修改</c:if><c:if test="${ leg.getArea() == null }">添加 + </c:if></button></li>
                	</ul>
        		</div>
        	</div>
       </div>
      </form>
    </div>
  </section>
<script type="text/javascript">
	$("#confirm").bind("click", function () { 
		var txtDepartCity = $("#txtDepartCity").val();
		var txtDestCity = $("#txtDestCity").val();
		var area = $("#area").val();
		
		if(!txtDepartCity) {
			alert("请选择出发地");
		}
		else if(!txtDestCity){
			alert("请选择到达");
		}
		else if(!area){
			alert("请选择区域");
		}
		
		var departCity = txtDepartCity.split('(')[0];
		var departCityCode = txtDepartCity.split('(')[1].split(')')[0];
		
		var arrivalCity = txtDestCity.split('(')[0];
		var arrivalCityCode = txtDestCity.split('(')[1].split(')')[0];
		
		$.ajax({
	        cache: true,
	        type: "POST",
	        url: "/leg/save",
	        data:{
	        	depart_city: departCity,
	        	depart_city_code: departCityCode,
	        	arrival_city : arrivalCity,
	        	arrival_city_code: arrivalCityCode,
	        	area: area
	        },
	        success: function(response) {
	            if(response){
	            	if(response == "unlogin"){
	        			location.href = "/";
	        			return;
	        		}
	            	if(response.data){
	            		document.location.href = "/leg/list";
	            	}
	            }else{
	                //alert(response.error);
	            }
	        }
	    });
	});
</script>
  <script type="text/javascript" src="/js/select_pop.js"></script>
  <script type="text/javascript" src="/js/left_menu.js"></script>
  <script type="text/javascript" src="/js/left_fold.js"></script>
  <script type="text/javascript" src="/js/QueryInterCityNew.js"></script>
  <script type="text/javascript" src="/js/jquery.linq.min.js"></script>
</body>
</html>