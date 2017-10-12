<%@ page language="java" contentType="text/html; charset=UTF-8"  
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<table class="table table-border table-bordered table-bg table-hover table-sort search_flight_table" id="table_OrderList">
  <thead>
    <tr class="text-c">
      <th align="center"></th>
      <th align="center" style="width: 60px;">航班</th>
      <th align="center" style="width: 120px;">来源</th>
      <th align="center">出发日期</th>
      <c:if test="${flightsDetails.size() > 0 &&  flightsDetails.get(0).getRound()}">
        <th align="center">返程日期</th>
      </c:if>
      <th align="center">直飞/中转</th>
      <th align="center">航司代码</th>
      <th align="center">舱位等级</th>
      <th align="center">人民币售价</th>
      <th align="center">原币种</th>
      <th align="center">原币售价</th>
      <th align="center">显示票价</th>
      <th align="center">显示税费</th>
      <th align="center">预定费用</th>
      <!-- <th align="center">显示燃油费</th>
      <th align="center">留学生特价</th> -->
      <th align="center">中转城市</th>
    </tr>
  </thead>
  <c:if test="${ fn:length(flightsDetails) == 0 }">
  	<tbody>
      <tr class="nodata" style="" id="trnodata">
        <td class="text-c" colspan="17" id="tdnodata">没有搜索到信息，请重新输入查询条件！
        </td>
      </tr>
  	</tbody>
  </c:if> 
  <c:if test="${ fn:length(flightsDetails) > 0 }">
	  <tbody id="DataDivFare">
	  	<% int len = 1; %>
	  	<c:forEach var="flight" items="${flightsDetails}">
		    <tr class="text-c">
		      <td id="tdno" data-title="数字" class="tab_title"><%=len%></td>
		      <td id="tdFlighInfo" data-title="航班"><a id="tdatdFlighInfo" href="#X">${flight.getAirLine()}</a></td>
		      <td id="tdDataSource" data-title="来源">${flight.getSource()}</td>
		      <td id="tdDeptureDate" data-title="出发日期">${flight.getGoDate()}</td>
		      <c:if test="${flight.getRound()}">
		      	<td id="tdDeptureDate" data-title="返程日期">${flight.getBackDate()}</td>
		      </c:if>
		      <td id="tdStopType" data-title="直飞/中转">${flight.getTransferOrDirect()}</td>
		      <td id="tdCarrierCode" data-title="航司代码">${flight.getAirlineCodes()}</td>
		      <td id="tdCabinClass" data-title="舱位等级">${flight.getSeatType()}</td>
		      <td id="tdRMBPrice" data-title="人民币售价">${flight.getTotalPrice()}</td>
		      <td id="tdBasePriceCode" data-title="原币种">${flight.getCurrency()}</td>
		      <td id="tdBasePrice" data-title="原币售价">${flight.getOriginPrice()}</td>
		      <td id="tdShowPrice" data-title="显示票价">${flight.getFare()}</td>
		      <td id="tdShowTax" data-title="显示税费">${flight.getTax()}</td>
		      <td id="tdOrderPrice" data-title="预定费用">${flight.getTotalPrice()}</td>
		      <%-- <td id="tdShowFuleTax" data-title="显示燃油费">${flight.getFuelPrice()}</td>
		      <td id="tdLXSPrice" data-title="留学生特价">${flight.specialPrice()}</td> --%>
		      <td id="tdStopCity" data-title="中转城市">${flight.getTransferCities()}</td>
		    </tr>
		    <% len++; %>
		</c:forEach>
	  </tbody>
	</c:if>
</table>