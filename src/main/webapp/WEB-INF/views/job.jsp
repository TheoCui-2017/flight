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
	<title>任务管理</title>
</head> 
<body>
  <jsp:include page="share.jsp" />
  <section class="Hui-article-box">
    <div id="iframe_box" class="Hui-article">
      <form method="post" id="form1">
        <nav class="breadcrumb">
          <i class="Hui-iconfont"></i> 首页
          <span class="c-gray en">&gt;</span> 实时数据分析
          <span class="c-gray en">&gt;</span>任务管理
        </nav>
        <div class="pd-20">
          <div class="mt-5">
            <div class="form_li clearfix">
              <ul class="clearfix">
                <li class="left_txt">OTA列表：</li>
                <li class="left_txt" style="width:260px;">
                  <span class="select-box inline">
                    <select id="source" class="select" style="width: 208px; margin-top:0px; vertical-align:top;">
                      <option value="priceline" <c:if test="${jobInfo.source.equals('priceline')}">selected="selected"</c:if>>priceline</option>
                      <option value="orbitz" <c:if test="${ jobInfo.source.equals('orbitz')}">selected="selected"</c:if>>orbitz</option>
                      <option value="expedia" <c:if test="${ jobInfo.source.equals('expedia')}">selected="selected"</c:if>>expedia</option>
                    </select>
                  </span>
                </li>
                <li class="left_txt"><input type="checkbox" id="grab" name="ifGrab" <c:if test="${jobInfo.status == 1}">checked="checked"</c:if>/><label for="grab"> 任务激活</label></li>
              </ul>
              <ul class="clearfix">

              </ul>
              <div class="checked_after">
                <ul class="clearfix">
                  <li class="left_txt">并发数 ： </li>
                  <li class="right_inp"><input type="text" class="input-text" id="thread" value="${jobInfo.threads}"/></li>
                </ul>
                <ul class="clearfix">
                  <li class="left_txt">抓取间隔：</li>
                  <li class="right_inp"><input type="text" class="input-text" id="duration" value="${jobInfo.duration}" /><div class="timebox">小时</div></li>
                </ul>
                <ul class="clearfix">
                  <li class="left_txt">抓取天数：</li>
                  <li class="right_inp"><input type="text" class="input-text" id="days" value="${jobInfo.days}" /></li>
                </ul>
                <ul class="clearfix">
                	<li class="left_txt">采集IP：</li>
                	<span class="inquiry_li" style="padding-left:0px;">
       					<div class="g-down" style="position:relative;z-index:9999;">
       						<div class="target" style="width:138px;">IP列表 <i class="g-ico g-ico-soliddrop"></i></div>
       						<div class="list" style="width:150px;">
       							<ul class="airline tempclass" id="ul_FlightCarrier0" style="width:150px;">
                  					<li id="li_FlightCarrier0">
                    					<label class="fl width110">
                      	  				<input type="checkbox" class="inp_chk" id="chb_CarrierCode" name="ips" value="192.168.6.241" <c:if test="${ jobInfo.ips.indexOf('192.168.6.241') != -1}">checked="checked"</c:if>><span id="sp_TCarrierName">192.168.6.241</span></label>
                  					</li>
                  					<li id="li_FlightCarrier1">
                   						<label class="fl width110">
                        				<input type="checkbox" class="inp_chk" id="chb_CarrierCode" name="ips" value="192.168.9.60" <c:if test="${ jobInfo.ips.indexOf('192.168.9.60') != -1}">checked="checked"</c:if>><span id="sp_TCarrierName">192.168.9.60</span></label>
                  					</li>
                  					<li id="li_FlightCarrier2">
                    					<label class="fl width110">
                        				<input type="checkbox" class="inp_chk" id="chb_CarrierCode" name="ips" value="192.168.5.53" <c:if test="${ jobInfo.ips.indexOf('192.168.5.53') != -1}">checked="checked"</c:if>><span id="sp_TCarrierName">192.168.5.53</span></label>
                  					</li>
               		 			</ul>
               		 		</div>
               		 	</div>
               		</span>	
                </ul>
                <ul class="clearfix">
                  <li class="left_txt">有效期：</li>
                  <li class="right_inp"><input type="text" id="txtDeptureDate" name="txtDeptureDate" class="input-text" OnClick="WdatePicker()" value="${jobInfo.start}">至<input type="text" id="txtArriveDate" name="txtArriveDate" class="input-text" OnClick="WdatePicker()" value="${jobInfo.end}"></li>
                </ul>
                <ul class="clearfix">
                	<span class="inquiry_li">
              			<li class="left_txt">往返维度：</li>
              			<li class="right_inp">
                			<span  class="show_dimension">
                 				<input type="checkbox" name="time_dimension" id="time_7" value="7" <c:if test="${ jobInfo.interval_days.indexOf('7') != -1}">checked="checked"</c:if>><label for="time_month">7</label>
		            		</span>
		           	 		<span  class="show_dimension">
		                		<input type="checkbox" name="time_dimension" id="time_14" value="14" <c:if test="${ jobInfo.interval_days.indexOf('14') != -1}">checked="checked"</c:if>><label for="time_week">14</label>
		            		</span>
		            		<span  class="show_dimension">
		                		<input type="checkbox" name="time_dimension" id="time_30" value="30" <c:if test="${ jobInfo.interval_days.indexOf('30') != -1}">checked="checked"</c:if>><label for="time_day">30</label>
		            		</span>
		            	</li>
            		</span>
            	</ul>
                <ul class="clearfix">
                  <li class="right_inp">
                    <!-- begin -->
                    <div class="member_opreat clearfix">
                      <div class="mem_group belong">
                        <h5>请添加抓取航段</h5>
                        <select name="left" size="10" id="select_left" multiple>
                        	<c:if test="${jobInfo.leftLegs.size()>0}">
                           <c:forEach begin="0" end="${jobInfo.leftLegs.size()-1}" var="index">
                             <option value="${jobInfo.leftLegs.get(index).getId()}">${jobInfo.leftLegs.get(index).getDepartCity()}(${jobInfo.leftLegs.get(index).getDepartCityCode()}) - ${jobInfo.leftLegs.get(index).getArrivalCity()}(${jobInfo.leftLegs.get(index).getArrivalCityCode()})</option>
                           </c:forEach>
                        </c:if>
                        </select>
                      </div>
                      <div class="arr">
                        <div class="ico">
                          <button type="button" class="btn" onClick="moveSelected(document.all.left,document.all.right,'add')">添加&gt;&gt;</button>
                          <button type="button" class="btn" onClick="moveSelected(document.all.right,document.all.left,'del')">&lt;&lt;删除</button>
                        </div>
                      </div>
                      <div class="mem_group alternative">
                        <h5>已添加抓取航段</h5>
                        <select name="right" size="10" id="select_right" multiple>
                        <c:if test="${jobInfo.selectLegs.size()>0}">
                           <c:forEach begin="0" end="${jobInfo.selectLegs.size()-1}" var="index">
                             <option value="${jobInfo.selectLegs.get(index).getId()}">${jobInfo.selectLegs.get(index).getDepartCity()}(${jobInfo.selectLegs.get(index).getDepartCityCode()}) - ${jobInfo.selectLegs.get(index).getArrivalCity()}(${jobInfo.selectLegs.get(index).getArrivalCityCode()})</option>
                           </c:forEach>
                        </c:if>
                        </select>	
                      </div>
                    </div>
                    <!-- end -->
                  </li>
                </ul>
                <ul class="clearfix sub_box">
                  <li><button type="button" class="btn size-L btn-success" id="saveInfo"/>提交设置</button></li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </form>
    </div>
  </section>
<script type="application/javascript">  
  $(document).ready(function(){	
	if(${ jobInfo.status } == 1){
		$('.input-text').attr("disabled",false);
        $('.checked_after').show();
	}
	else{
        //未选中不可编辑
       $('.input-text').attr("disabled",true);
       $('.checked_after').hide();
	}	
    $('#grab').click(function(){
      var source = $("#source").val();
      var status = 0;
      if(this.checked){
    	status = 1;
      }
      // ajax 请求
      $.ajax({
        cache: true,
        type: "POST",
        url: "/job/update",
        data:{
        	source: source,
        	status: status
        },
        success: function(response) {
        	if(response){
        		if(response == "unlogin"){
        			location.href = "/";
        			return;
        		}
        		if(status == 1){
        			$('.input-text').attr("disabled",false);
        	        $('.checked_after').show();
        		}
        		else{
        			$('.input-text').attr("disabled",true);
        	        $('.checked_after').hide();
        		}
        	}
        	else{
        		alert("删除失败！");
        	}
        }
      });
    });
    
    $("#source").change(function(){
    	var source = $("#source").val();
    	document.location.href="/job/info?ota="+source;
    });
    
    $("#saveInfo").click(function(){
    	var source = $("#source").val();
    	var thread = $("#thread").val();
    	var duration = $("#duration").val();
    	var days = $("#days").val();
    	var times = [];
    	$("input:checkbox[name='time_dimension']:checked").each(function() {
    		times.push($(this).val());
		});
    	
    	var ips = [];
    	$("input:checkbox[name='ips']:checked").each(function() {
    		ips.push($(this).val());
		});
    	
    	var start = $("#txtDeptureDate").val();
    	var end = $("#txtArriveDate").val();
    	
    	if(!thread){
    		alert("请填写并发数");
    	}
    	else if(!duration){
    		alert("请填写抓取间隔");
    	}
    	else if(!days){
    		alert("请填写抓取天数");
    	}
    	else if(ips.length == 0){
    		alert("请选择采集ips");
    	}
    	else if(times.length == 0){
    		alert("请选择往返维度");
    	}
    	else{
    		// ajax请求
    		$.ajax({
    			cache: true,
    			type: "POST",
    			url: "/job/save",
    			data:{
    				source: source,
    				thread: thread,
    				duration: duration,
    				days: days,
    				interval_days: times.toString(),
    				ips: ips.toString(),
    				start: start,
    				end: end
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
  <script type="text/javascript" src="/js/QueryInterCityNew.js"></script>
  <script type="text/javascript" src="/js/jquery.linq.min.js"></script>
  <script type="text/javascript" src="/js/WdatePicker.js"></script>
</body>
</html>