<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Kết quả thanh toán | CatClinic</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/booking/PaymentResult.css">
</head>

<body>
<%@include file="header.jsp" %>

<div class="main-content">
    <div class="result-card">
        <c:set var="status" value="${requestScope.msg}" />
        <c:choose>
            <c:when test="${status eq 'success'}">
                <i class="bi bi-check-circle-fill success-icon" style="font-size: 64px;"></i>
                <h2 style="color: #22a06b;">Thanh toán thành công!</h2>
                <p>Cảm ơn bạn đã tin tưởng CatClinic. Lịch hẹn của bạn đã được xác nhận.</p>
                <p>Mã giao dịch VNPay:</p>
                <h3 style="color:#ff6600">${transactionCode}</h3>
            </c:when>
            <c:when test="${status eq 'failed'}">
                <i class="bi bi-x-circle-fill error-icon" style="font-size: 64px;"></i>
                <h2 style="color: #dc2626;">Thanh toán thất bại</h2>
                <p>Giao dịch không thành công hoặc đã bị hủy bởi người dùng.</p>
            </c:when>
            <c:otherwise>
                <i class="bi bi-exclamation-circle-fill error-icon" style="font-size: 64px;"></i>
                <h2 style="color: #dc2626;">Có lỗi xảy ra</h2>
                <p>Không xác định được trạng thái giao dịch.</p>
            </c:otherwise>
        </c:choose>

        <a href="${pageContext.request.contextPath}/booking-history" class="btn">
            Quay lại lịch sử đặt lịch
        </a>
    </div>
</div>

<footer style="background: #ffffff; border-top: 1px solid #e5e7eb; padding: 25px 0; text-align: center; color: #64748b; font-size: 14px;">
    <div class="footer-content">
        &copy; 2026 CatClinic. All rights reserved.
    </div>
</footer>
</body>
</html>