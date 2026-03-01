<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Check-in Patient | Cat Clinic</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Link CSS dùng chung cho cả bộ khung Sidebar + Header -->
    <link href="${pageContext.request.contextPath}/css/receptiondashboard-style.css" rel="stylesheet">
    <!-- Link CSS riêng cho trang Check-in -->
    <link href="${pageContext.request.contextPath}/css/checkin-patient-style.css" rel="stylesheet">
</head>
<body>
<c:set var="activePage" value="check-in" scope="request" />
<jsp:include page="sidebar.jsp" />

<main class="main-wrapper">
    <header class="top-header">
        <div class="search-box">
            <form action="${pageContext.request.contextPath}/reception/check-in" method="GET" class="search-form-checkin">
                <i class="fa-solid fa-magnifying-glass" style="margin-right: 12px; color: #64748b;"></i>
                <input type="text" name="search" placeholder="Tìm kiếm khách hàng check-in hôm nay...">
                <button type="submit" class="btn-hidden"></button>
            </form>
        </div>
    </header>

    <div class="dashboard-content">
        <div class="page-title">
            <h2>Check-in Patient</h2>
            <p>Manage and confirm that the pet has arrived at the clinic today.</p>
        </div>

        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th width="35%">PATIENT INFO</th>
                    <th width="20%">TIME</th>
                    <th width="20%">STATUS</th>
                    <th width="25%" style="text-align: right;">ACTION</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${pendingList}" var="p">
                    <tr>
                        <td>
                            <div class="td-patient">
                                <div class="cat-avatar"><i class="fa-solid fa-cat"></i></div>
                                <div class="patient-info">
                                    <h4>${p.catName}</h4>
                                    <span>${p.customerName} - ${p.phone}</span>
                                </div>
                            </div>
                        </td>
                        <td>
                            <i class="fa-regular fa-clock" style="margin-right: 5px; color: #64748b;"></i>
                                ${p.appointmentTime}
                        </td>
                        <td><span class="badge bg-pending">Pending</span></td>
                        <td style="text-align: right;">
                            <a href="${pageContext.request.contextPath}/reception/check-in?action=confirm&id=${p.bookingID}"
                               class="btn-action" onclick="return confirm('Confirm that ${p.catName} has arrived at the counter?');">
                                <i class="fa-solid fa-check"></i> Check-in Now
                            </a>
                        </td>
                    </tr>
                </c:forEach>

                <!-- Hiển thị khi không có khách nào cần Check-in -->
                <c:if test="${empty pendingList}">
                    <tr>
                        <td colspan="4">
                            <div class="empty-state-checkin">
                                <i class="fa-solid fa-mug-hot"></i>
                                <h3>Excellent!</h3>
                                <p>All customers today have completed check-in.</p>
                            </div>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</main>
</body>
</html>