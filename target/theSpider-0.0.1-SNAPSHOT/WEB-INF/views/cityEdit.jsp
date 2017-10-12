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
	<title>三字码管理</title>
</head> 
<body>
  <jsp:include page="share.jsp" />
  <section class="Hui-article-box">
    <div id="iframe_box" class="Hui-article">
      <form method="post" action="OTAFareAnalysis.aspx" id="form1">
        <nav class="breadcrumb">
          <i class="Hui-iconfont"></i> 首页
          <span class="c-gray en">&gt;</span> 实时数据分析
          <span class="c-gray en">&gt;</span>三字码管理
        </nav>
        <div class="pd-20">
        	<c:if test="${ city.getArea() != null }">
        		<div class="pd-5 clearfix">
            		<a href="/airport/list?cityId=${ city.getId() }" type="button" class="btn btn-success flaot_r">机场详情 + </a>
          		</div>
          	</c:if>
        	<div class="form_li clearfix">
        		<div class="checked_after">
        			<ul class="clearfix">
                  		<li class="left_txt">城市名称： </li>
                  		<li class="right_inp"><input type="text" id="name" name="name"  class="input-text" autocomplete="off" <c:if test="${ city.getArea() != null }">value="${city.getName()}"</c:if><c:if test="${ city.getArea() == null }">value=""</c:if>></li>
                	</ul>
                	<ul class="clearfix">
                  		<li class="left_txt">城市代码： </li>
                  		<li class="right_inp"><input type="text" id="code" name="code" class="input-text" autocomplete="off" <c:if test="${ city.getArea() != null }">value="${city.getCode()}"</c:if><c:if test="${ city.getArea() == null }">value=""</c:if>></li>
                	</ul>
                	<ul class="clearfix">
                  		<li class="left_txt">国家： </li>
                  		<li class="right_inp"><input type="text" id="country" name="country"  class="input-text" autocomplete="off" <c:if test="${ city.getArea() != null }">value="${city.getCountry()}"</c:if><c:if test="${ city.getArea() == null }">value=""</c:if>></li>
                	</ul>
                	<ul class="clearfix">
                  		<li class="left_txt">区域：</li>
                  		<li class="left_txt" style="width:260px;"></li>
                  		<span class="select-box inline">
                  			<select id="area" class="select" style="width: 208px; margin-top:0px; vertical-align:top;">
	                  			<option value="亚洲" <c:if test="${ city.getArea().equals('亚洲')}">selected="selected"</c:if>>亚洲</option>
				               	<option value="欧洲" <c:if test="${ city.getArea().equals('欧洲')}">selected="selected"</c:if>>欧洲</option>
				               	<option value="美洲" <c:if test="${ city.getArea().equals('美洲')}">selected="selected"</c:if>>美洲</option>
				               	<option value="非洲" <c:if test="${ city.getArea().equals('非洲')}">selected="selected"</c:if>>非洲</option>
				               	<option value="大洋洲" <c:if test="${ city.getArea().equals('大洋洲')}">selected="selected"</c:if>>大洋洲</option>
				            </select>
                  		</span>
                	</ul>
                	<ul class="clearfix">
                  		<li class="left_txt">查询词： </li>
                  		<li class="right_inp"><input type="text" id="querys" name="querys"  class="input-text" autocomplete="off" <c:if test="${ city.getArea() != null }">value="${city.getQuerys()}"</c:if><c:if test="${ city.getArea() == null }">value=""</c:if>></li>
                	</ul>
                	<ul class="clearfix sub_box">
                  		<li><button type="button" id="confirm" class="btn size-L btn-success" /><c:if test="${ city.getArea() != null }">修改</c:if><c:if test="${ city.getArea() == null }">添加 + </c:if></button></li>
                	</ul>
        		</div>
        	</div>
       </div>
      </form>
    </div>
  </section>
<script type="text/javascript">
	$("#confirm").bind("click", function () { 
		var id = "${city.getId()}";
		var name = $("#name").val();
		var code = $("#code").val();
		var country = $("#country").val();
		var area = $("#area").val();
		var querys = $("#querys").val();
		
		if(!name) {
			alert("请填写城市名称");
		}
		else if(!code){
			alert("请填写城市代码");
		}
		else if(!country){
			alert("请填写国家名称");
		}
		else if(!area){
			alert("请选择区域");
		}
		else{
			$.ajax({
		        cache: true,
		        type: "POST",
		        url: "/city/save",
		        data:{
		        	id: id,
		        	name: name,
		        	code: code,
		        	country : country,
		        	area: area,
		        	querys: querys
		        },
		        success: function(response) {
		            if(response){
		            	if(response == "unlogin"){
		        			location.href = "/";
		        			return;
		        		}
		            	if(response.data){
		            		if(!id){
		            			document.location.href = "/airport/list?cityId=" + response.data;
		            		}
		            		else{
		            			document.location.href = "/city/list";
		            		}
		            	}
		            }else{
		                //alert(response.error);
		            }
		        }
		    });
		}
	});
</script>
  <script type="text/javascript" src="/js/select_pop.js"></script>
  <script type="text/javascript" src="/js/left_menu.js"></script>
  <script type="text/javascript" src="/js/left_fold.js"></script>
  <script type="text/javascript" src="/js/QueryInterCityNew.js"></script>
  <script type="text/javascript" src="/js/jquery.linq.min.js"></script>
</body>
</html>