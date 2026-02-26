<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Kết quả thanh toán | CatClinic</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">

    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f7fa;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .result-card {
            background: white;
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.05);
            text-align: center;
            max-width: 450px;
            width: 90%;
        }

        .success-icon { color: #22a06b; }
        .error-icon { color: #dc2626; }

        .btn {
            display: inline-block;
            padding: 14px 28px;
            background: #ff6600;
            color: white;
            text-decoration: none;
            border-radius: 8px;
            margin-top: 25px;
            font-weight: 600;
        }

        .btn:hover {
            background: #e65c00;
        }

        h2 { margin-top: 20px; }
        p { color: #666; line-height: 1.5; }
    </style>
</head>
<body>

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

        <c:when test="${status eq 'invalid_signature'}">
            <i class="bi bi-shield-lock-fill error-icon" style="font-size: 64px;"></i>
            <h2 style="color: #dc2626;">Lỗi xác thực</h2>
            <p>Chữ ký dữ liệu không hợp lệ. Vui lòng liên hệ hỗ trợ nếu bạn đã bị trừ tiền.</p>
        </c:when>

        <c:when test="${status eq 'invoice_not_found'}">
            <i class="bi bi-file-earmark-x-fill error-icon" style="font-size: 64px;"></i>
            <h2 style="color: #dc2626;">Không tìm thấy hóa đơn</h2>
            <p>Không tìm thấy hóa đơn tương ứng với giao dịch này.</p>
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

</body>
</html>