<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>BilingBooking</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link href="${pageContext.request.contextPath}/css/receptiondashboard-style.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/view-booking-list.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Invoice/Billing_booking.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>



<body>

<%@include file="header.jsp" %>

<div class="app-container">

    <c:set var="activePage" value="billing" scope="request"/>
    <%@include file="sidebar.jsp" %>

    <main class="main-content">

        <div class="hub-header">
            <h2>Billing & Payment</h2>
            <p>Receptionist: ${sessionScope.acc.fullName}. Manage customer invoices and process transactions here.</p>
        </div>


        <div class="table-section">

            <div class="table-header">
                <h3>View Invoice List</h3>
                <p>Overview of today's patient flow</p>
            </div>

            <form action="${pageContext.request.contextPath}/reception/billing-bookings" method="GET" id="filterForm">

                <div class="table-toolbar">

                    <div style="display:flex;gap:15px;align-items:center;flex-wrap:wrap;">

                        <div class="table-search">
                            <input type="hidden" name="page" value="1">
                            <i class="fa-solid fa-magnifying-glass"></i>

                            <input type="text"
                                   name="search"
                                   value="${currentSearch}"
                                   placeholder="Search cat, phone or owner...">
                        </div>

                        <div class="date-filter-wrapper">
                            <span class="date-label">
                                <i class="fa-regular fa-calendar-check"></i> End date:
                            </span>
                            <input type="date"
                                   name="filterDate"
                                   value="${currentDate}"
                                   class="date-input-custom"
                                   onchange="document.getElementById('filterForm').submit()">
                        </div>

                    </div>

                    <div class="filter-bar">

                        <div class="filter-buttons">

                            <button type="submit"
                                    name="status"
                                    value="ALL"
                                    class="${empty currentStatus || currentStatus == 'ALL' ? 'active' : ''}">
                                ALL
                            </button>

                            <button type="submit"
                                    name="status"
                                    value="Paid"
                                    class="${currentStatus == 'Paid' ? 'active' : ''}">
                                Paid
                            </button>

                            <button type="submit"
                                    name="status"
                                    value="UnPaid"
                                    class="${currentStatus == 'UnPaid' ? 'active' : ''}">
                                UnPaid
                            </button>

                            <button type="submit"
                                    name="status"
                                    value="Partial"
                                    class="${currentStatus == 'Partial' ? 'active' : ''}">
                                Partial
                            </button>

                        </div>

                    </div>

                </div>

            </form>

            <table class="modern-table">

                <thead>
                <tr>
                    <th>Image</th>
                    <th>Patient</th>
                    <th>Check In</th>
                    <th>Check Out</th>
                    <th>Status</th>
                    <th class="col-actions">Actions</th>
                </tr>
                </thead>

                <tbody>

                <c:forEach items="${billingList}" var="b">

                    <tr>
                        <td>
                            <img src="${pageContext.request.contextPath}/${b.getImage()}"
                                 class="pet-img"
                                 alt="Pet">
                        </td>

                        <td>
                            <div class="patient-cell">



                                <div>
                                    <p class="patient-name">${b.catName}</p>
                                    <p class="patient-id">
                                        Owner: ${b.ownerName}
                                    </p>
                                    <p class="patient-id">

                                        Phone: ${b.phone}
                                    </p>
                                </div>

                            </div>
                        </td>

                        <td>

<span class="date-text">
<i class="fa-regular fa-calendar table-icon"></i>

<fmt:formatDate value="${b.checkInTime}" pattern="yyyy-MM-dd"/>
</span>

                            <br>

                            <span class="time-text">
<i class="fa-regular fa-clock table-icon"></i>

<fmt:formatDate value="${b.checkInTime}" pattern="HH:mm"/>
</span>

                        </td>

                        <td>

                            <c:choose>

                                <c:when test="${not empty b.checkOutTime}">

<span class="date-text">
<i class="fa-regular fa-calendar table-icon"></i>

<fmt:formatDate value="${b.checkOutTime}" pattern="yyyy-MM-dd"/>
</span>

                                    <br>

                                    <span class="time-text">
<i class="fa-regular fa-clock table-icon"></i>

<fmt:formatDate value="${b.checkOutTime}" pattern="HH:mm"/>
</span>

                                </c:when>

                                <c:otherwise>

<span class="time-text">
<i class="fa-solid fa-minus table-icon"></i>
Not Yet
</span>

                                </c:otherwise>

                            </c:choose>

                        </td>

                        <td>

<span class="badge ${b.invoiceStatus.toLowerCase()}">
        ${b.invoiceStatus}
</span>

                        </td>

                        <td class="col-actions">

                            <a href="${pageContext.request.contextPath}/reception/invoice_detail?bookingID=${b.bookingID}"
                               class="btn-text action-view">

                                <i class="fa-solid fa-eye"></i> Detail

                            </a>

                        </td>

                    </tr>

                </c:forEach>

                <c:if test="${empty billingList}">
                    <tr>
                        <td colspan="5" class="empty-message">
                            No invoices found.
                        </td>
                    </tr>
                </c:if>

                </tbody>

            </table>
            <div class="pagination">

                <c:if test="${totalPage > 1}">

                    <!-- Previous -->
                    <c:if test="${currentPage > 1}">
                        <a class="page-btn"
                           href="${pageContext.request.contextPath}/reception/billing-bookings?page=${currentPage-1}&search=${currentSearch}&status=${currentStatus}&filterDate=${currentDate}">
                            <i class="fa-solid fa-angle-left"></i>
                        </a>
                    </c:if>

                    <!-- Page numbers -->
                    <c:forEach begin="1" end="${totalPage}" var="i">

                        <a class="page-btn ${i == currentPage ? 'active' : ''}"
                           href="${pageContext.request.contextPath}/reception/billing-bookings?page=${i}&search=${currentSearch}&status=${currentStatus}&filterDate=${currentDate}">
                                ${i}
                        </a>

                    </c:forEach>

                    <!-- Next -->
                    <c:if test="${currentPage < totalPage}">
                        <a class="page-btn"
                           href="${pageContext.request.contextPath}/reception/billing-bookings?page=${currentPage+1}&search=${currentSearch}&status=${currentStatus}&filterDate=${currentDate}">
                            <i class="fa-solid fa-angle-right"></i>
                        </a>
                    </c:if>

                </c:if>

            </div>

        </div>

    </main>

</div>

</body>

</html>