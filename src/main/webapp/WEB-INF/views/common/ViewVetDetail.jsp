<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>Veterinarian Profile</title>
    
    <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ViewVetDetail.css">
</head>

<body>

    <!-- HEADER -->
    <jsp:include page="headerSelection.jsp"/>

    <!-- PROFILE SECTION -->
<div class="vet-profile">

    <div class="vet-card">

        <!-- LEFT -->
        <div class="vet-left">

            <span class="vet-badge">VETERINARIAN PROFILE</span>

            <h1 class="vet-name">${v.fullName}</h1>

            <p class="vet-degree">${v.degree}</p>

            <div class="vet-info">
                <p><strong>Experience:</strong> ${v.experienceYear}+ Years</p>

                <p>
                    <strong>Gender:</strong>
                    <c:choose>
                        <c:when test="${v.male}">
                            👨 Male
                        </c:when>
                        <c:otherwise>
                            👩 Female
                        </c:otherwise>
                    </c:choose>
                </p>

                <p><strong>Email:</strong> ${v.email}</p>
                <p><strong>Phone:</strong> ${v.phone}</p>
            </div>

            <div class="vet-bio">
                ${v.bio}
            </div>

            <a href="Booking" class="vet-btn">
                Book Appointment
            </a>

        </div>

        <!-- RIGHT -->
        <div class="vet-right">
            <img src="${v.image}">
        </div>
    </div>

</div>

    <!-- FOOTER -->
    <jsp:include page="footer_1.jsp"/>

</body>
</html>