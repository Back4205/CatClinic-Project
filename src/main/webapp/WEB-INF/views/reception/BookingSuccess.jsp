<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đặt lịch thành công | CatClinic</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">

    <style>
        /* 1. Đảm bảo body chiếm toàn bộ chiều cao màn hình */
        html, body {
            height: 100%;
            margin: 0;
        }

        body {
            background-color: #f9fafb;
            font-family: 'Inter', system-ui, -apple-system, sans-serif;
            display: flex;
            flex-direction: column; /* Xếp Header - Content - Footer theo cột */
        }

        /* 2. Căn giữa tuyệt đối cho main-content */
        .main-content {
            flex: 1; /* Chiếm toàn bộ khoảng trống còn lại giữa header và footer */
            display: flex;
            align-items: center;      /* Căn giữa theo chiều dọc */
            justify-content: center;   /* Căn giữa theo chiều ngang */
            padding: 20px;
        }

        .success-card {
            max-width: 500px;
            width: 100%;
            text-align: center;
            padding: 40px;
            background: #ffffff;
            border-radius: 16px;
            box-shadow: 0 10px 25px -5px rgba(0,0,0,0.05);
            animation: fadeIn 0.6s ease-out;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .success-icon-wrapper {
            width: 80px;
            height: 80px;
            background: #ecfdf5;
            color: #10b981;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 24px;
        }

        .success-icon-wrapper i { font-size: 40px; }

        h2 { color: #064e3b; font-size: 26px; font-weight: 800; margin-bottom: 12px; }
        p { color: #64748b; line-height: 1.6; margin-bottom: 24px; }

        .booking-details {
            background: #f8fafc;
            border: 1px dashed #cbd5e1;
            padding: 16px;
            border-radius: 12px;
            margin-bottom: 30px;
        }

        .label { display: block; color: #94a3b8; font-size: 12px; text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 4px; }
        .value { color: #1e293b; font-size: 24px; font-weight: 800; }

        .actions { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }

        .btn {
            padding: 12px 16px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            font-size: 14px;
            transition: 0.2s;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
        }

        .btn-primary { background: #ff6600; color: white; border: none; }
        .btn-primary:hover { background: #e65c00; }

        .btn-secondary { background: #f1f5f9; color: #475569; }
        .btn-secondary:hover { background: #e2e8f0; }

        footer {
            background: #ffffff;
            border-top: 1px solid #e5e7eb;
            padding: 20px 0;
            text-align: center;
            color: #64748b;
            font-size: 14px;
        }
    </style>
</head>
<body>
<%@include file="header.jsp" %>

<div class="main-content">
    <div class="success-card">
        <div class="success-icon-wrapper">
            <i class="bi bi-check-lg"></i>
        </div>

        <h2>Appointment booked successfully!</h2>
        <p>Great! Your appointment has been recorded in the system. We will contact you shortly to confirm.</p>

        <div class="booking-details">
            <span class="label">ID your Booking</span>
            <span class="value">${bookingID}</span>
        </div>

        <div class="actions">
            <a href="${pageContext.request.contextPath}/reception/counter-booking" class="btn btn-secondary">
                <i class="fa-solid fa-plus"></i> Create New Booking
            </a>
            <a href="${pageContext.request.contextPath}/booking-history" class="btn btn-primary">
                <i class="fa-solid fa-calendar-check"></i> Booking history
            </a>
        </div>
    </div>
</div>

<footer>
    <div class="footer-content">
        &copy; 2026 CatClinic. All rights reserved.
    </div>
</footer>
</body>
</html>