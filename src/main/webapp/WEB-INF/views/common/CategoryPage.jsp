<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>${category.categoryName}</title>
    <link href="css/CategoryStyle.css" rel="stylesheet" type="text/css"/>
    
</head>
<body>
<jsp:include page="headerSelection.jsp"/>

<!-- ================= SECTION 1: BANNER ================= -->
<section class="banner-section">
    <div class="banner-content">
        <div class="banner-text">
            <h1>${category.categoryName}</h1>
            <p>${category.description}</p>
            <a href="booking.jsp" class="btn-book">Book Appointment</a>
        </div>
        <div class="banner-image">
            <img src="${category.banner}" alt="${category.categoryName}">
        </div>
    </div>
</section>


<!-- ================= SECTION 2: SERVICES ================= -->
<section class="service-section" id="service-section">
    <h2>Our Services</h2>
    <div class="line"></div> 

    <div class="service-container">
        <c:forEach var="s" items="${services}">
            <div class="service-card">
                <div class="service-image">
                    <img src="${s.imgUrl}" alt="${s.nameService}">
                </div>

                <h3>${s.nameService}</h3>
                <p>Price: ${s.price} VND</p>
                <p>Time: ${s.timeService} Minus</p>

                <a href="service?id=${s.serviceID}" class="view-more">
                    More →
                </a>
            </div>
        </c:forEach>
    </div>

    <!-- ===== PAGINATION ===== -->
    <div class="pagination">

    <c:if test="${currentPage > 1}">
        <a href="CategoryController?id=${cid}&page=${currentPage - 1}#service-section" 
           class="page-btn">
            &laquo;
        </a>
    </c:if>

    <c:set var="startPage" value="${currentPage - 1}" />
    <c:set var="endPage" value="${currentPage + 1}" />

    <c:if test="${startPage < 1}">
        <c:set var="startPage" value="1" />
        <c:set var="endPage" value="${startPage + 2}" />
    </c:if>

    <c:if test="${endPage > totalPage}">
        <c:set var="endPage" value="${totalPage}" />
        <c:set var="startPage" value="${totalPage - 2}" />
    </c:if>

    <c:if test="${startPage < 1}">
        <c:set var="startPage" value="1" />
    </c:if>

    <c:forEach begin="${startPage}" end="${endPage}" var="i">
        <a href="CategoryController?id=${cid}&page=${i}#service-section"
           class="page-btn ${i == currentPage ? 'active' : ''}">
            ${i}
        </a>
    </c:forEach>
    <c:if test="${currentPage < totalPage}">
        <a href="CategoryController?id=${cid}&page=${currentPage + 1}#service-section" 
           class="page-btn">
            &raquo;
        </a>
    </c:if>

</div>

</section>


<section class="cta-section">
    <h2>Are you ready to use our services?</h2>
    <a href="booking.jsp" class="btn-cta">Booking</a>
</section>
<jsp:include page="footer_1.jsp"/>

</body>
</html>