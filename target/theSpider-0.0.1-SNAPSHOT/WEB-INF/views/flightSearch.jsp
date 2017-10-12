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
	<title>航班查询</title>
</head> 
<body>
  <jsp:include page="share.jsp" />
  <section class="Hui-article-box">
    <div id="iframe_box" class="Hui-article">
      <form method="post" action="OTAFareAnalysis.aspx" id="form1">
        <nav class="breadcrumb">
          <i class="Hui-iconfont"></i> 首页
          <span class="c-gray en">&gt;</span> 实时数据分析
          <span class="c-gray en">&gt;</span>航班查询
        </nav>
        <div class="pd-20 search_flight_box">
          <div class="pd-5">
          	<span class="inquiry_li padding_l0">
              <span class="select-box inline">
              	<select id="slt_FlightTrip" class="select" style="width: 60px;">
                  <option value="OW">单程</option>
                  <option value="RT">往返</option>
                </select>
              </span>
            </span>
            <span class="inquiry_li">
              出发城市： <input type="text" id="txtDepartCity" placeHolder="中文/拼音" name="txtDepartCity" style="color: rgb(0, 0, 0);" class="input-text" autocomplete="off">
			</span>
			<span class="inquiry_li">
              到达城市： <input type="text" id="txtDestCity" placeHolder="中文/拼音" name="txtDestCity" style="color: rgb(0, 0, 0);" class="input-text" autocomplete="off">
            </span>
            <span class="inquiry_li">
              出发日期：
              <input type="text" id="txtDeptureDate" name="txtDeptureDate" class="input-text" OnClick="WdatePicker()" value="">
            </span>
            <span class="inquiry_li">
              返程日期：
              <input type="text" id="txtArriveDate" name="txtArriveDate" class="input-text" OnClick="WdatePicker()">
            </span>
          </div>
          <div class="pd-5 pad_l60"">
          <span class="inquiry_li">
            <span>舱位等级：</span>
            <span class="select-box inline">
              <select id="slt_CabinCode" class="select" style="width: 108px;">
                <option value="1">经济舱</option>
                <option value="2">公务舱</option>
                <option value="3">头等舱</option>
              </select>
            </span>
            </span>
            <span class="inquiry_li">
            <div class="g-down">
              <div class="target">数据来源 <i class="g-ico g-ico-soliddrop"></i></div>
              <div class="list" style="width:121px;">
                <ul class="airline tempclass" id="ul_FlightCarrier0">
                  <li id="li_FlightCarrier0">
                    <label class="fl width110">
                        <input type="checkbox" class="inp_chk" id="chb_CarrierCode" name="source" value="www.expedia.com"><span id="sp_TCarrierName">expedia</span></label>
                  </li>
                  <li id="li_FlightCarrier1">
                    <label class="fl width110">
                        <input type="checkbox" class="inp_chk" id="chb_CarrierCode" name="source" value="www.orbitz.com"><span id="sp_TCarrierName">orbitz</span></label>
                  </li>
                  <li id="li_FlightCarrier2">
                    <label class="fl width110">
                        <input type="checkbox" class="inp_chk" id="chb_CarrierCode" name="source" value="www.priceline.com"><span id="sp_TCarrierName">priceline</span></label>
                  </li>
                </ul>
              </div>
            </div>
            </span>
            <span class="inquiry_li">
            显示：
            <span class="select-box inline">
              <select id="ddlDataSize" class="select" style="width: 108px;">
                <option value="10">10条</option>
                <option value="30">30条</option>
              </select>
            </span>
            </span>
            <span class="inquiry_li  padding_l0">
              <span name="btnSearch" id="btnSearch" class="btn btn-success radius  ml-10"><i class="Hui-iconfont"></i> 查询</span>
              <span name="btnRealTimeSearch" id="btnRealTimeSearch" class="btn btn-success radius  ml-10"><i class="Hui-iconfont"></i> 实时查询</span>
          	</span>
          </div>
          <div class="mt-5">
          </div>
          <div class="isloading" style="display:none;color: #185922;padding: auto;width: 100%;text-align: center;font-size: 16px;">  
        	<span stype="">  
            	<img src="/images/loading.gif" alt="数据正在加载..."></span><span class="spnContent">数据正在加载...</span>  
    		</div>
        </div>
      </form>
    </div>
  </section>
  <script type="text/javascript" src="/js/select_pop.js"></script>
  <script type="text/javascript" src="/js/left_menu.js"></script>
  <script type="text/javascript" src="/js/left_fold.js"></script>
  <script type="text/javascript" src="/js/QueryInterCityNew.js"></script>
  <script type="text/javascript" src="/js/jquery.linq.min.js"></script>
  <script type="text/javascript" src="/js/WdatePicker.js"></script>
  
</body>

<script type="text/javascript"> 
$(function () { 
	$("#btnSearch").bind("click", function () { 
		$(".mt-5").html('');
		var round_str = $("#slt_FlightTrip").val();
		var round = false;
		if(round_str == "RT"){
			round = true;
		}
		var departure_city_str = $("#txtDepartCity").val();
		var arrival_city_str = $("#txtDestCity").val();
		var go_date = $("#txtDeptureDate").val();
		var back_date = $("#txtArriveDate").val();
		var seat_type = $("#slt_CabinCode").val();
		var size = $("#ddlDataSize").val();
		var sources =[];  
		$("input:checkbox[name='source']:checked").each(function() {
			sources.push($(this).val());
		});
		if(sources.length == 0 ){
			$("input:checkbox[name='source']").each(function() {
				sources.push($(this).val());
			});
		}
		
		if(!departure_city_str) {
			alert("请选择出发城市");
		}
		else if(!arrival_city_str){
			alert("请选择到达城市");
		}
		else if(!go_date){
			alert("请选择出发日期");
		}
		else if(round && !back_date){
			alert("请选择返程日期");
		}
		else{
			var departure_city = departure_city_str;
			var arrival_city = arrival_city_str;
			if(departure_city_str.length > 3){
				departure_city = departure_city_str.split('(')[1].split(')')[0];
				arrival_city = arrival_city_str.split('(')[1].split(')')[0];
			}
			$(".isloading").show();
			$.ajax({
		        cache: true,
		        type: "POST",
		        url: "/flight/searchDetail",
		        data:{
		        	round: round,
		        	departure_city:departure_city,
		        	arrival_city:arrival_city,
		        	go_date: go_date,
		        	back_date: back_date,
		        	seat_type:seat_type,
		        	sources:sources.toString(),
		        	size: size
		        },
		        success: function(response) {
		            if(response){
		            	if(response == "unlogin"){
		        			location.href = "/";
		        			return;
		        		}
		            	$(".isloading").hide();
		            	$(".mt-5").html('');
		                $(".mt-5").html(response).show();
		            }else{
		            	$(".isloading").hide();
		                //alert(response.error);
		            }
		        }
		    });
		}
	});
	
	
	$("#btnRealTimeSearch").bind("click", function () { 
		$(".mt-5").html('');
		var round_str = $("#slt_FlightTrip").val();
		var round = false;
		if(round_str == "RT"){
			round = true;
		}
		var departure_city_str = $("#txtDepartCity").val();
		var arrival_city_str = $("#txtDestCity").val();
		var go_date = $("#txtDeptureDate").val();
		var back_date = $("#txtArriveDate").val();
		var seat_type = $("#slt_CabinCode").val();
		var size = $("#ddlDataSize").val();
		var sources =[];  
		$("input:checkbox[name='source']:checked").each(function() {
			sources.push($(this).val());
		});
		if(sources.length == 0 ){
			$("input:checkbox[name='source']").each(function() {
				sources.push($(this).val());
			});
		}
		
		if(!departure_city_str) {
			alert("请选择出发城市");
		}
		else if(!arrival_city_str){
			alert("请选择到达城市");
		}
		else if(!go_date){
			alert("请选择出发日期");
		}
		else if(round && !back_date){
			alert("请选择返程日期");
		}
		else{
			var departure_city = departure_city_str;
			var arrival_city = arrival_city_str;
			if(departure_city_str.length > 3){
				departure_city = departure_city_str.split('(')[1].split(')')[0];
				arrival_city = arrival_city_str.split('(')[1].split(')')[0];
			}
			$(".isloading").show();
			$.ajax({
		        cache: true,
		        type: "POST",
		        url: "/flight/searchNow",
		        data:{
		        	round: round,
		        	departure_city:departure_city,
		        	arrival_city:arrival_city,
		        	go_date:go_date,
		        	back_date: back_date,
		        	seat_type:seat_type,
		        	sources:sources.toString(),
		        	size: size
		        },
		        success: function(response) {
		            if(response){
		            	if(response == "unlogin"){
		        			location.href = "/";
		        			return;
		        		}
		            	$(".isloading").hide();
		            	$(".mt-5").html('');
		                $(".mt-5").html(response).show();
		            }else{
		            	$(".isloading").hide();
		                //alert(response.error);
		            }
		        }
		    });
		}
	});
});  
</script> 
</html>