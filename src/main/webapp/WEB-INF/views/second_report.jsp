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
	<title>指定航段价格趋势监控</title>
</head> 
<body>
  <jsp:include page="share.jsp" />
  <section class="Hui-article-box">
    <div id="iframe_box" class="Hui-article">
      <form method="post" action="OTAFareAnalysis.aspx" id="form1">
        <nav class="breadcrumb">
          <i class="Hui-iconfont"></i> 首页
          <span class="c-gray en">&gt;</span> 报表数据分析
          <span class="c-gray en">&gt;</span>指定航段价格趋势监控
        </nav>
        <div class="pd-20 search_flight_box">
        	<div class="pd-5">
        		<span class="inquiry_li">
            		出发城市： <input type="text" id="txtDepartCity" name="txtDepartCity" style="color: rgb(0, 0, 0);" class="input-text" autocomplete="off">
				</span>
            	<span class="inquiry_li">
            		<span>到达区域：</span>
            		<span class="select-box inline">
              			<select id="txtDestArea" name="txtDestArea" class="select" style="width: 108px;">
                			<option value="亚洲">亚洲</option>
				               	<option value="欧洲">欧洲</option>
				               	<option value="美洲">美洲</option>
				               	<option value="非洲">非洲</option>
				               	<option value="大洋洲">大洋洲</option>
              			</select>
            		</span>
            	</span>
            	<span class="inquiry_li">
              		航班日期段：
              		<input type="text" id="startDate" name="startDate" class="input-text" OnClick="WdatePicker()" value="">
            	</span>
            	<span class="inquiry_li">
             	 	至 &nbsp;&nbsp;&nbsp;&nbsp;
              		<input type="text" id="endDate" name="endDate" class="input-text" OnClick="WdatePicker()">
            	</span>
       		</div>
       		<div class="pd-5">
       			<span class="inquiry_li" style="padding-left:90px;">
       				<div class="g-down" style="position:relative;z-index:9999;">
       					<div class="target">停需期 <i class="g-ico g-ico-soliddrop"></i></div>
       					<div class="list" style="width:120px;">
       						<ul class="airline tempclass" id="ul_FlightCarrier0" style="width:80px;">
                  				<li id="li_FlightCarrier0">
                    				<label class="fl width110">
                        			<input type="checkbox" class="inp_chk" id="chb_CarrierCode" name="stayDays" value="7"><span id="sp_TCarrierName">7</span></label>
                  				</li>
                  				<li id="li_FlightCarrier1">
                   					<label class="fl width110">
                        			<input type="checkbox" class="inp_chk" id="chb_CarrierCode" name="stayDays" value="14"><span id="sp_TCarrierName">14</span></label>
                  				</li>
                  				<li id="li_FlightCarrier2">
                    				<label class="fl width110">
                        			<input type="checkbox" class="inp_chk" id="chb_CarrierCode" name="stayDays" value="30"><span id="sp_TCarrierName">30</span></label>
                  				</li>
               		 		</ul>
               		 	</div>
               		 </div>
               	</span>
            	<span class="inquiry_li" style="margin-left:-7px;">
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
              		<em>显示维度：</em>
                	<span  class="show_dimension">
                  		<input type="radio" name="time_dimension" id="time_month" value="30"><label for="time_month">月</label>
		            </span>
		            <span  class="show_dimension">
		                 <input type="radio" name="time_dimension" id="time_week" value="7"><label for="time_week">周</label>
		            </span>
		            <span  class="show_dimension">
		                 <input type="radio" name="time_dimension" id="time_day" value="1"><label for="time_day">日</label>
		            </span>
            	</span>
            	<span class="inquiry_li  padding_l0">
              		<span name="btnSearch" id="btnSearch" class="btn btn-success radius  ml-10"><i class="Hui-iconfont"></i> 查询</span>
            	</span>
           	</div>
           	<div class="pd-5" id="carrier_lists" style="display:none;padding-left:75px;">
           		<span class="inquiry_li">
               		<div class="g-down">
               			<div class="target">航空公司 <i class="g-ico g-ico-soliddrop"></i></div>
               			<div class="list" id="carrier_list">
               				
               			</div>
               		</div>
               	</span>
           	</div>
          	<div class="mt-5">

          	</div>
          	<div class="isloading" style="display:none;color: #185922;padding: auto;width: 100%;text-align: center;font-size: 16px;">  
        		<span stype="">  
            		<img src="/images/loading.gif" alt="数据正在加载..."></span><span class="spnContent">数据正在加载...</span>  
    			</div>
    		<div style="display:none" id="frameDiv"></div>
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
  <script type="text/javascript" src="/js/d3.js"></script>
  
</body>

<script type="text/javascript"> 
$(function () {
	
	$("#btnSearch").bind("click", function () {
		$("#carrier_lists").hide();
		var departure_str = $("#txtDepartCity").val();
		var arrival_area_str = $("#txtDestArea").val();
		var start_date = $("#startDate").val();
		var end_date = $("#endDate").val();
		var seat_type = $("#slt_CabinCode").val();
		var stay_days =[];  
		$("input:checkbox[name='stayDays']:checked").each(function() {
			stay_days.push($(this).val());
		});
		if(stay_days.length == 0 ){
			$("input:checkbox[name='stayDays']").each(function() {
				stay_days.push($(this).val());
			});
		}
		var carriers = [];
		
		var dimension = $("input:radio[name='time_dimension']:checked").val();
		
		if(!departure_str) {
			alert("请选择始发区域");
		}
		else if(!arrival_area_str){
			alert("请选择到达区域");
		}
		else if(!start_date){
			alert("请选择开始日期");
		}
		else if(!end_date){
			alert("请选择结束日期");
		}
		else if(!dimension){
			alert("请选择显示维度");
		}
		else{
			var departure_city = departure_str;
			var arrival_area = arrival_area_str;
			if(departure_city.length > 3){
				departure_city = departure_city.split('(')[1].split(')')[0];
			}
			$(".mt-5").html('');
			$(".isloading").show();
			$.ajax({
		        cache: true,
		        type: "POST",
		        url: "/report/area/query",
		        data:{
		        	departure_city: departure_city,
		        	arrival_area:arrival_area,
		        	start_date: start_date,
		        	end_date: end_date,
		        	stay_days: stay_days.toString(),
		        	airline_codes: carriers.toString(),
		        	seat_type: seat_type,
		        	dimension: dimension
		        },
		        success: function(response) {
		            if(response){
		            	if(response == "unlogin"){
		        			location.href = "/";
		        			return;
		        		}
		            	$(".isloading").hide();
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