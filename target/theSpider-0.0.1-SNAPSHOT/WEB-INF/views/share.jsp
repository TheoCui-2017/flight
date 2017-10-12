 <%@ page language="java" contentType="text/html; charset=UTF-8"  
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

 <header class="Hui-header cl">
     <a class="Hui-logo l" title="OTA数据管理平台" href="/">OTA数据管理平台</a> <a class="Hui-logo-m l" href="/" title="OTA数据管理平台"></a><span class="OTA数据管理平台"></span>
     <ul class="Hui-userbar">
         <li>超级管理员</li>
         <li class="dropDown">
         	<span id="LoginName"><%=request.getSession().getAttribute("username") %></span>
        </li>
        <!--  <li class="dropDown"><a href="javascript:void(0);" class="dropDown_A"><span id="LoginName"><%=request.getSession().getAttribute("username") %></span><i class="Hui-iconfont"></i></a>
             <ul class="dropDown-menu radius box-shadow">
                 <li style="display: none;"><a href="javascript:void(0);">个人信息</a></li>
                 <li><a href="javascript:void(0);" id="btnUpdatePsd">修改密码</a></li>
                 <li><a href="/ExitSys.aspx">退出</a></li>
             </ul> 
         </li> -->
     </ul>
 </header>
 <aside class="Hui-aside">
   <div class="menu_dropdown bk_2">
     <dl id="SMenu_M_FX_01_01" style="" class="TempList">
         <dt id="SMenudt" <% if( (request.getRequestURI().toString().contains("report.jsp") == false) && (request.getRequestURI().toString().contains("user.jsp") == false) ) { %>class="selected"<% } %>>
           <i class="Hui-iconfont" id="SMenusi"></i> <a id="SMenus" href="javascript:void(0)">实时数据分析</a><i class="Hui-iconfont menu_dropdown-arrow" id="SMenuei"></i>
         </dt>
         <dd id="TMenudd" <% if((request.getRequestURI().toString().contains("report.jsp") == false) && (request.getRequestURI().toString().contains("user.jsp") == false)) { %>style="display: block;"<% } %>>
             <ul id="TMenuul">
                 <li><a href="/flight/search">航班查询</a></li>
                 <li><a href="/finance/list">汇率管理</a></li>
                 <li><a href="/leg/list">航段管理</a></li>
                 <li><a href="/city/list">三字码管理</a></li>
                 <li><a href="/job/info?ota=priceline">爬虫任务管理</a></li>
             </ul>
         </dd>
     </dl>
     <dl id="SMenu_M_FX_01_02" style="" class="TempList">
         <dt id="SMenudt" <% if(request.getRequestURI().toString().contains("report.jsp")) { %>class="selected"<% } %>>
           <i class="Hui-iconfont" id="SMenusi"></i> <a id="SMenus" href="javascript:void(0)">报表数据分析</a><i class="Hui-iconfont menu_dropdown-arrow" id="SMenuei"></i>
         </dt>
         <dd id="TMenuda"  <% if(request.getRequestURI().toString().contains("report.jsp")) { %>style="display: block;"<% } %>>
             <ul id="TMenuul">
                 <li><a href="/report/leg/trend">指定航段价格趋势监控</a></li>
                 <li><a href="/report/area/trend">区域市场价格趋势监控</a></li>
                 <li><a href="/report/excel/trend">指定航线价格监控</a></li>
             </ul>
         </dd>
     </dl>
     <dl id="SMenu_M_FX_01_03" style="" class="TempList">
         <dt id="SMenudt" <% if(request.getRequestURI().toString().contains("user.jsp")) { %>class="selected"<% } %>>
           <i class="Hui-iconfont" id="SMenusi"></i> <a id="SMenus" href="javascript:void(0)">用户管理</a><i class="Hui-iconfont menu_dropdown-arrow" id="SMenuei"></i>
         </dt>
         <dd id="TMenuda"  <% if(request.getRequestURI().toString().contains("user.jsp")) { %>style="display: block;"<% } %>>
             <ul id="TMenuul">
                 <li><a href="/user/privilege">用户权限管理</a></li>
             </ul>
         </dd>
     </dl>
   </div>
 </aside>
 <div class="dislpayArrow">
 	<a class="pngfix" href="javascript:void(0);" onclick="displaynavbar(this)"></a>
 </div>