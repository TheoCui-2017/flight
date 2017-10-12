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
	<title>制定航段价格监控</title>
</head> 
<body>
  <jsp:include page="share.jsp" />
  <section class="Hui-article-box">
    <div id="iframe_box" class="Hui-article">
      <form method="post" action="OTAFareAnalysis.aspx" id="form1">
        <nav class="breadcrumb">
          <i class="Hui-iconfont"></i> 首页
          <span class="c-gray en">&gt;</span> 实时数据分析
          <span class="c-gray en">&gt;</span>指定航线价格监控
        </nav>
        <div class="pd-20 search_flight_box">
          <div class="pd-5">
            <span class="inquiry_li">
             	 出发城市： <input type="text" id="txtDepartCity" placeHolder="中文/拼音" name="txtDepartCity" style="color: rgb(0, 0, 0);" class="input-text" autocomplete="off">
			</span>
			<span class="inquiry_li">
              	到达城市： <input type="text" id="txtDestCity" placeHolder="中文/拼音" name="txtDestCity" style="color: rgb(0, 0, 0);" class="input-text" autocomplete="off">
            </span>
            <span class="inquiry_li">
              		航班日期段：
              		<input type="text" id="startDate" name="startDate" class="input-text" OnClick="WdatePicker()" value="">
            	</span>
            	<span class="inquiry_li">
             	 	至 &nbsp;&nbsp;&nbsp;&nbsp;
              		<input type="text" id="endDate" name="endDate" class="input-text" OnClick="WdatePicker()">
            	</span>
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
              <div class="target" style="width:90px;">停留期： <i class="g-ico g-ico-soliddrop"></i></div>
              <div class="list" style="width:90px;">
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
          </div>
          <div class="form_li clearfix">
          	<div class="checked_after">
          		<ul class="clearfix">
          			<li class="left_txt">ADD列表 ： </li>
                  	<li class="right_inp"><input type="text" id="add" name="add"  class="input-text" placeholder="以英文逗号(,)分隔" autocomplete="off" value=""></li>
          		</ul>
          		<ul class="clearfix">
          			<li class="left_txt">SPA列表 ： </li>
                  	<li class="right_inp"><input type="text" id="spa" name="spa"  class="input-text" placeholder="以英文逗号(,)分隔" autocomplete="off" value=""></li>
          		</ul>
          		<ul class="clearfix">
          			<li class="left_txt">经停 ： </li>
                  	<li class="right_inp"><input type="text" id="txtStopoverCity" name="txtStopoverCity" placeHolder="中文/拼音" class="input-text" autocomplete="off" value=""></li>
          		</ul>
          		<ul class="clearfix sub_box">
                	<li><button type="button" id="btnSearch" name="btnSearch" class="btn size-L btn-success" />查询</button></li>
                </ul>
          	</div>
          </div>
          <div class="mt-5">
          </div>
          <div class="isloading" style="display:none;color: #185922;padding: auto;width: 100%;text-align: center;font-size: 16px;">  
        	<span stype="">  
            	<img src="/images/loading.gif" alt="数据正在加载..."></span><span class="spnContent">数据正在加载...</span>  
    		</div>
        </div>
      </form>
      <div style="width:100%;height:60px;background:rgba(0,0,0,0.2);position:fixed;color:#fff; text-align:right; z-index:9999; bottom:0;left:0;" id="loyer">
  	  	<button type="button" id="download" name="download" class="btn size-L btn-success" style="display:inline-block;width:180px;height:40px; text-decoration:none; line-height:40px;font-size:16px; color:#fff;margin-right:30px;background:#eb8500;margin-top:10px;text-align:center;"/>下载查询结果</button>
  	  </div>
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
	$("#loyer").hide();
	$("#btnSearch").bind("click", function () { 
		var departure_city_str = $("#txtDepartCity").val();
		var arrival_city_str = $("#txtDestCity").val();
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
		var add_list = $("#add").val();
		var spa_list = $("#spa").val();
		var stopover = $("#txtStopoverCity").val();
		if(stopover && stopover.length > 3){
			stopover = stopover.split("(")[1].split(")")[0];
		}
		
		if(!departure_city_str) {
			alert("请选择出发城市");
		}
		else if(!arrival_city_str){
			alert("请选择到达城市");
		}
		else if(!start_date){
			alert("请选择开始日期");
		}
		else if(!end_date){
			alert("请选择结束日期");
		}
		else{
			var departure_city = departure_city_str;
			var arrival_city = arrival_city_str;
			if(departure_city_str.length > 3){
				departure_city = departure_city_str.split('(')[1].split(')')[0];
			}
			if(arrival_city_str.length > 3){
				arrival_city = arrival_city_str.split('(')[1].split(')')[0];
			}
		
			var src = "/report/excel/showExcel?departure_city="+departure_city+"&arrival_city="+arrival_city+"&start_date="+start_date+
			"&end_date="+end_date+"&seat_type="+seat_type+"&stay_days="+stay_days+"&add_list="+add_list+"&spa_list="+spa_list+"&stopover="+stopover;
			$(".isloading").show();
			$("#btnSearch").attr('disabled',"true");
			$("#btnDownload").attr('disabled',"true");
			$.ajax({
		        cache: true,
		        type: "GET",
		        url: src,
		        success: function(response) {
		            if(response){
		            	if(response == "unlogin"){
		        			location.href = "/";
		        			return;
		        		}
		            	$(".isloading").hide();
		            	$("#loyer").show();
		            	$("#btnSearch").removeAttr("disabled");
					    $("#btnDownload").removeAttr("disabled");
		                $(".mt-5").html(response).show();
		            }else{
		            	$(".isloading").hide();
		            	$("#btnSearch").removeAttr("disabled");
					    $("#btnDownload").removeAttr("disabled");
		                //alert(response.error);
		            }
		        }
		    });
		}
	});
	
	$("#download").bind("click", function () { 
		var departure_city_str = $("#txtDepartCity").val();
		var arrival_city_str = $("#txtDestCity").val();
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
		var add_list = $("#add").val();
		var spa_list = $("#spa").val();
		var stopover = $("#txtStopoverCity").val();
		if(stopover && stopover.length > 3){
			stopover = stopover.split("(")[1].split(")")[0];
		}
		if(!departure_city_str) {
			alert("请选择出发城市");
		}
		else if(!arrival_city_str){
			alert("请选择到达城市");
		}
		else if(!start_date){
			alert("请选择开始日期");
		}
		else if(!end_date){
			alert("请选择结束日期");
		}
		else{
			var departure_city = departure_city_str;
			var arrival_city = arrival_city_str;
			if(departure_city_str.length > 3){
				departure_city = departure_city_str.split('(')[1].split(')')[0];
			}
			if(arrival_city_str.length > 3){
				arrival_city = arrival_city_str.split('(')[1].split(')')[0];
			}
		
			var src = "/report/excel/download?departure_city="+departure_city+"&arrival_city="+arrival_city+"&start_date="+start_date+
			"&end_date="+end_date+"&seat_type="+seat_type+"&stay_days="+stay_days+"&add_list="+add_list+"&spa_list="+spa_list+"&stopover="+stopover;
			$(".isloading").show();
			$("#btnSearch").attr('disabled',"true");
			$("#btnDownload").attr('disabled',"true");
			var e = $("<iframe id='iframedownload' style='display:none'></iframe>");
			 e.attr('src', src);
			 e.load(function() {
			     console.debug('start downloading...');
			     $(".isloading").hide();
			     $("#iframedownload").remove();
			     $("#btnSearch").removeAttr("disabled");
			     $("#btnDownload").removeAttr("disabled");
			 });
			 
			 $('body').append(e);
		}
	});
});
</script>
<script>
 function downloadComplete(){
	 $('#frameDiv').html('');
	 $(".isloading").hide();
 }
</script>
</html>