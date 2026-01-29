<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 1/27/2026
  Time: 2:42 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2>Medical History</h2>

<table border="1" cellpadding="8" cellspacing="0">
    <tr>
        <th>Date</th>
        <th>Doctor</th>
        <th>Clinical Note</th>
        <th>Action</th>
    </tr>

    <c:forEach items="${medicalHistoryList}" var="h">
        <tr>
            <td>${h.visitDate}</td>
            <td>${h.doctorName}</td>
            <td>${h.clinicalNote}</td>

            <td>
                <c:if test="${not empty h.bookingID}">
                    <a href="${pageContext.request.contextPath}/cats/medical-detail?idBooking=${h.bookingID}">
                        View Detail
                    </a>
                </c:if>

                <c:if test="${empty h.bookingID}">
                    <span style="color: gray;">No Detail</span>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>