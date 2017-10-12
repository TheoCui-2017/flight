<%@ page language="java" contentType="text/html; charset=UTF-8"  import="java.util.*"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<table class="table table-border table-bordered table-bg table-hover table-sort search_flight_table" id="table_OrderList">
	<thead>
		<% int len = 1;%>
		<c:forEach var="result" items="${DataRes}">
			<tr class="text-c">
				<th align="center">周</th>
				<th align="center">始发</th>
				<th align="center">到达</th>
				<c:forEach var="res" items="${result}">
      			<th align="center">${res.key}</th>
      			</c:forEach>
		    </tr>
		    <c:forEach var="i" begin="0" end="${d_asize-1}">
		    <tr class="text-c">
		    	<th align="center"><%=len %></th>
				<th align="center">${d_as.get(i).split(",")[0]}</th>
				<th align="center">${d_as.get(i).split(",")[1]}</th>
				<c:forEach var="res" items="${result}">
      				<th align="center">${res.value[i]}</th>
      			</c:forEach>
      		</tr>
			</c:forEach>
		<%len++;%>
		</c:forEach>
	</thead>
</table>