<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Dịch Vụ GAIA</title>
        <link href="css/ViewAllService.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <jsp:include page="headerSelection.jsp"/>

        <!-- ================= CARE SECTION ================= -->
        <section class="care-section">
            <div class="care-content">
                <h2>Dedicated Care Service</h2>
                <p>
                   Your pet will always receive attentive and thoughtful care when you come to PetClinic!
                </p>
                <a href="contact" class="care-btn">Contact Us for Consultation</a>
            </div>
        </section>


        <!-- ================= SERVICES LOAD DATABASE ================= -->
        <section class="services" id="services">
            <h2 class="section-title">Our Services</h2>
            <div class="line"></div>

            <div class="service-container">

                <c:forEach var="s" items="${listService}">

                    <c:if test="${s.active}">

                        <div class="service-item">

                            <div class="image-box">
                                <img src="${s.imgUrl}" alt="${s.nameService}">
                            </div>

                            <h3>${s.nameService}</h3>

                            <p class="price">
                                Price: ${s.price} VND
                            </p>

                            <p class="time">
                                Time: ${s.timeService} phút
                            </p>

                            <a href="service?id=${s.serviceID}" class="read-more">
                                More →
                            </a>

                        </div>

                    </c:if>

                </c:forEach>

            </div>
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="ViewAllService?page=${currentPage - 1}#services">«</a>
                </c:if>
                <c:set var="startPage" value="${currentPage - 1}" />
                <c:set var="endPage" value="${currentPage + 1}" />
                <c:if test="${startPage < 1}">
                    <c:set var="startPage" value="1"/>
                    <c:set var="endPage" value="${totalPage >= 3 ? 3 : totalPage}"/>
                </c:if>
                <c:if test="${endPage > totalPage}">
                    <c:set var="endPage" value="${totalPage}"/>
                    <c:set var="startPage" value="${totalPage - 2}"/>
                </c:if>

                <c:if test="${startPage < 1}">
                    <c:set var="startPage" value="1"/>
                </c:if>
                <c:forEach begin="${startPage}" end="${endPage}" var="i">
                    <a href="ViewAllService?page=${i}#services"
                       class="${i == currentPage ? 'active-page' : ''}">
                        ${i}
                    </a>
                </c:forEach>

                <c:if test="${currentPage < totalPage}">
                    <a href="ViewAllService?page=${currentPage + 1}#services">»</a>
                </c:if>
            </div>
        </section>
        <section class="booking-banner">

            <img src="images/dog-left.png" class="dog-left">
            <img src="images/cat-right.png" class="cat-right">

            <div class="booking-content">
                <h2>Are you ready to use our services?</h2>
                <a href="booking" class="booking-btn">
                    Booking
                </a>
            </div>

        </section>
        <jsp:include page="footer_1.jsp"/>

    </body>
</html>