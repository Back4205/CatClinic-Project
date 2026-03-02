<%--<%@page contentType="text/html" pageEncoding="UTF-8"%>--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Visit History | CatClinic</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/base.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/sidebar.css">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/booking-history.css">

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    </head>
    <body>

        <%@include file="header.jsp" %>

        <div class="container">

            <c:set var="activePage" value="history" />
            <%@include file="sidebar.jsp" %>

            <main class="content">

                <div class="page-title">
                    <h2>BOOKING HISTORY</h2>
                    <p>MANAGEMENT LOG FOR MEDICAL AND GROOMING SERVICES.</p>
                </div>

                <div class="stats-container">
                    <div class="stat-card">
                        <div class="stat-icon icon-total"><i class="bi bi-clock-history"></i></div>
                        <div class="stat-info">
                            <h3>${total}</h3>
                            <p>Total Visits</p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon icon-scheduled"><i class="bi bi-calendar-check"></i></div>
                        <div class="stat-info">
                            <h3>${scheduled}</h3>
                            <p>Scheduled</p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon icon-completed"><i class="bi bi-check-circle-fill"></i></div>
                        <div class="stat-info">
                            <h3>${completed}</h3>
                            <p>Completed</p>
                        </div>
                    </div>
                </div>

                <form action="booking-history" method="GET">
                    <div class="filter-bar">

                        <input type="text" name="search" class="search-input" 
                               placeholder="Search cat name or service..." 
                               value="${currentSearch}">

                        <div class="filter-buttons">
                            <button type="submit" name="status" value="ALL" 
                                    class="${empty currentStatus || currentStatus == 'ALL' ? 'active' : ''}">
                                ALL STATUS
                            </button>

                            <button type="submit" name="status" value="Completed"
                                    class="${currentStatus == 'Completed' ? 'active' : ''}">
                                COMPLETED
                            </button>

                            <button type="submit" name="status" value="Confirmed"
                                    class="${currentStatus == 'Confirmed' ? 'active' : ''}">
                                UPCOMING
                            </button>

                            <button type="submit" name="status" value="Cancelled"
                                    class="${currentStatus == 'Cancelled' ? 'active' : ''}">
                                CANCELLED
                            </button>
                        </div>
                    </div>
                </form>

                <div class="booking-list">

                    <c:forEach var="b" items="${bookingList}">
                        <div class="booking-item">
                            <div class="cat-info">
                                <strong>${b.catName}</strong>
                                <span>${b.catBreed}</span>
                            </div>

                            <div class="datetime">
                                <div class="date-row">
                                    <span class="date-label">Begin</span>
                                    <span>${b.appointmentDate}</span>
                                    <span style="font-size: 11px; color: #999; margin-left: 5px;">
                                        (${b.appointmentTime})
                                    </span>
                                </div>

                                <c:if test="${not empty b.endDate && b.endDate != b.appointmentDate}">
                                    <div class="date-row">
                                        <span class="date-label">End</span>
                                        <span>${b.endDate}</span>
                                    </div>
                                </c:if>
                            </div>

                            <div class="service-type">
                                <i class="bi ${b.serviceType == 'Spa' ? 'bi-scissors' : 'bi-capsule'}"></i>
                                ${b.serviceName}
                            </div>

                            <div class="price">
                                $${b.price}
                            </div>

                            <div>
                                <span class="status-badge
                                      ${b.status == 'Completed' || b.status == 'Done' ? 'status-completed' : ''}
                                      ${b.status == 'Confirmed' || b.status == 'Upcoming' || b.status == 'Pending' ? 'status-upcoming' : ''}
                                      ${b.status == 'Cancelled' ? 'status-cancelled' : ''}
                                      ${b.status == 'In Progress' ? 'status-inprogress' : ''}">
                                    ${b.status}
                                </span>
                            </div>

                            <div style="text-align: right;">
                                <a href="#" class="btn-view">Details</a>
                            </div>
                        </div>
                    </c:forEach>

                    <c:if test="${empty bookingList}">
                        <div class="empty-state">
                            <i class="bi bi-inbox"></i>
                            <p>No booking history found.</p>
                        </div>
                    </c:if>

                </div>

            </main>
        </div>
        <footer style="background: #ffffff; border-top: 1px solid #e5e7eb; padding: 25px 0; text-align: center; color: #64748b; font-size: 14px; margin-top: auto;">
            <div class="footer-content">
                &copy; 2026 CatClinic. All rights reserved.
            </div>
        </footer>
    </body>
</html>