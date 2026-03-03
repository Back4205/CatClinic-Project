<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Counter Cancellation</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="${pageContext.request.contextPath}/css/receptiondashboard-style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/counter-cancellation-style.css" rel="stylesheet">
</head>
<body>
<c:set var="activePage" value="counter-cancellation" scope="request" />
<jsp:include page="sidebar.jsp" />

<main class="main-wrapper">
    <header class="top-header">
        <form action="${pageContext.request.contextPath}/reception/counter-cancellation" method="GET" class="search-box">
            <i class="fa-solid fa-magnifying-glass"></i>
            <input type="text" name="search" value="${searchKeyword}" placeholder="Nhập SĐT, Tên khách, hoặc Tên mèo để tìm...">
            <button type="submit" style="display: none;"></button>
        </form>
    </header>

    <div class="dashboard-content">
        <div class="page-title">
            <h2>Counter Cancellation</h2>
            <p>Cancel the medical appointment at the customer's request.</p>
        </div>

        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th width="30%">PATIENT INFO</th>
                    <th width="20%">DATE & TIME</th>
                    <th width="20%">STATUS</th>
                    <th width="30%" style="text-align: right;">ACTION</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${cancelList}" var="c">
                    <tr>
                        <td>
                            <div class="td-patient">
                                <div class="cat-avatar" style="background: #fef2f2; color: #ef4444;"><i class="fa-solid fa-cat"></i></div>
                                <div class="patient-info">
                                    <h4>${c.catName}</h4>
                                    <span>${c.customerName} - ${c.phone}</span>
                                </div>
                            </div>
                        </td>
                        <td>${c.appointmentDate} | ${c.appointmentTime}</td>
                        <td><span class="badge bg-pending">${c.status}</span></td>
                        <td style="text-align: right;">
                            <a href="${pageContext.request.contextPath}/reception/counter-cancellation?action=cancel&id=${c.bookingID}&search=${searchKeyword}"
                               class="btn-action btn-danger" onclick="return confirm('Are you sure you want to CANCEL this appointment? This action cannot be undone!');">
                                <i class="fa-solid fa-trash"></i> Cancel Booking
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty cancelList}">
                    <tr><td colspan="4" class="empty-state">No appointment found that can be canceled!</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</main>
</body>
</html>