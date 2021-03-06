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
          <div class="pd-5 clearfix">
            <a href="/leg/edit?depart_city_code=null&arrival_city_code=null" type="button" class="btn btn-success flaot_r">添加 + </a>
          </div>
          <div class="mt-5">
            <table class="table table-border table-bordered table-bg table-hover table-sort" id="table_OrderList">
              <thead>
                <tr class="text-c">
                  <th align="center" width="40"></th>
                  <th align="center">出发地</th>
                  <th align="center">到达地</th>
                  <th align="center">区域</th>
                  <th align="center" width="200">操作</th>
                </tr>
              </thead>
              <tbody>
              	<% int len = 1; %>
              	<c:forEach var="leg" items="${legInfo}">
                  <tr class="text-c">
                  	<td><%=len%></td>
                    <td>${leg.getDepartCity()}(${leg.getDepartCityCode()})</td>
                    <td>${leg.getArrivalCity()}(${leg.getArrivalCityCode()})</td>
                    <td>${leg.getArea()}</td>
                    <td>
                      <button type="button" class="btn btn-default" data-toggle="modal" data-target="#myModal" onclick="delLeg('${leg.getDepartCityCode()}', '${leg.getArrivalCityCode()}')">删除</button>
                      <button type="button" class="btn btn-secondary" onclick="editLeg('${leg.getDepartCityCode()}', '${leg.getArrivalCityCode()}')">修改</button>
                    </td>
                  </tr>
                  <% len++; %>
                </c:forEach>
              </tbody>
            </table>
            <div id="Pagination" class="msdn"></div>
          </div>
        </div>
      </form>
    </div>
  </section>
<script type="text/javascript">
  $('#myModal').modal("toggle");
  function delLeg(departCode, arrivalCode){
	  var queren = confirm("确定删除？");
	  if(queren == true){
		  $.ajax({
	        cache: true,
	        type: "POST",
	        url: "/leg/delete",
	        data:{
	        	depart_city_code: departCode,
	        	arrival_city_code: arrivalCode
	        },
	        success: function(response) {
	        	if(response){
	        		if(response == "unlogin"){
	        			location.href = "/";
	        			return;
	        		}
	        		document.location.reload();
	        	}
	        	else{
	        		alert("删除失败！");
	        	}
	        }
	    });
	  }
  }
  
  function editLeg(departCode, arrivalCode){
	  document.location.href = "/leg/edit?depart_city_code="+departCode+"&arrival_city_code=" + arrivalCode;
  }
</script>
  <script type="text/javascript" src="/js/select_pop.js"></script>
  <script type="text/javascript" src="/js/left_menu.js"></script>
  <script type="text/javascript" src="/js/left_fold.js"></script>
  <script type="text/javascript" src="/js/jquery.linq.min.js"></script>
</body>
</html>