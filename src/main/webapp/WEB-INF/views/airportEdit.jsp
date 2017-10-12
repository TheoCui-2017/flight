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
	<title>机场三字码管理</title>
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
        	<div class="form_li clearfix">
        		<div class="checked_after">
        			<ul class="clearfix">
                  		<li class="left_txt">机场名称： </li>
                  		<li class="right_inp"><input type="text" id="name" name="name"  class="input-text" autocomplete="off" <c:if test="${ airport.getName() != null }">value="${airport.getName()}"</c:if><c:if test="${ airport.getName() == null }">value=""</c:if>></li>
                	</ul>
                	<ul class="clearfix">
                  		<li class="left_txt">机场代码： </li>
                  		<li class="right_inp"><input type="text" id="code" name="code" class="input-text" autocomplete="off" <c:if test="${ airport.getName() != null }">value="${airport.getCode()}"</c:if><c:if test="${ airport.getName() == null }">value=""</c:if>></li>
                	</ul>
                	<ul class="clearfix">
                  		<li class="left_txt">查询词： </li>
                  		<li class="right_inp"><input type="text" id="querys" name="querys"  class="input-text" autocomplete="off" <c:if test="${ airport.getName() != null }">value="${airport.getQuerys()}"</c:if><c:if test="${ airport.getName() == null }">value=""</c:if>></li>
                	</ul>
                	<ul class="clearfix sub_box">
                  		<li><button type="button" id="confirm" class="btn size-L btn-success" /><c:if test="${ airport.getName() != null }">修改</c:if><c:if test="${ airport.getName() == null }">添加 + </c:if></button></li>
                	</ul>
        		</div>
        	</div>
       </div>
      </form>
    </div>
  </section>
<script type="text/javascript">
	$("#confirm").bind("click", function () { 
		var id = "${airport.getId()}";
		var cityId = "${cityId}"
		var name = $("#name").val();
		var code = $("#code").val();
		var querys = $("#querys").val();
			
		if(!name) {
			alert("请填写机场名称");
		}
		else if(!code){
			alert("请填写机场代码");
		}
		else{
			$.ajax({
		        cache: true,
		        type: "POST",
		        url: "/airport/save",
		        data:{
		        	id: id,
		        	cityId: cityId,
		        	name: name,
		        	code: code,
		        	querys: querys
		        },
		        success: function(response) {
		            if(response){
		            	if(response == "unlogin"){
		        			location.href = "/";
		        			return;
		        		}
		            	if(response.data){
		            		document.location.href = "/airport/list?cityId="+cityId;
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