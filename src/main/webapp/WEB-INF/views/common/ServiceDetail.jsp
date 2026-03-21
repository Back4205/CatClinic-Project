<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${service.nameService}</title>
    <link rel="stylesheet" href="css/serviceDetail.css">
</head>
<body>
<jsp:include page="headerSelection.jsp"/>
<section class="service-detail-section">
    <div class="detail-container">

        <!-- LEFT CONTENT -->
        <div class="detail-content">

            <div class="detail-text">
                <span class="tag">SERVICE DETAIL</span>

                <h1>${service.nameService}</h1>

                <p class="short-desc">
                    ${service.description}
                </p>

                <div class="info">
                    <p><strong>Giá:</strong> ${service.price} VND</p>
                    <p><strong>Thời gian:</strong> ${service.timeService} phút</p>
                </div>
            </div>

            <a href="${pageContext.request.contextPath}/Booking?categoryID=${service.categoryID}&serviceID=${service.serviceID}" class="btn-booking">
                Booking
            </a>

        </div>

        <!-- RIGHT IMAGE -->
        <div class="detail-image">
            <img src="${service.imgUrl}" alt="${service.nameService}">
        </div>

    </div>
</section>
<jsp:include page="footer_1.jsp"/>
</body>
</html>