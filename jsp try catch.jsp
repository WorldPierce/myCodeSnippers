<c:catch var="dateException">
    <fmt:parseDate var="rejectDate" value="${data.rejectDate}" pattern="yyyy-MM-dd"/>
<c:set var="rejectDate"><fmt:formatDate pattern="MM/dd/yyyy"  value="${rejectDate}"/></c:set>
           </c:catch>
                     <c:if test="${not empty dateException}">
            <c:set var="rejectDate" value="${data.rejectDate}"/>
       </c:if>
	                                   <%--  <fmt:parseDate value="${data.rejectDate}" pattern="yyyy-mm-dd" var="myDate"/> --%>
           <td>${rejectDate}</td>
	                                    <%-- <td><c:out value="${data.rejectDate}"/></td> --%>        