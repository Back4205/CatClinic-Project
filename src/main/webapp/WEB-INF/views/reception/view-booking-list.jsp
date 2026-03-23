
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Refund Management | Cat Clinic</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/DashboardAdminStyle.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar-admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header-admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/booking/cancel-booking.css">
</head>
<body>


<div class="admin-layout">
    <jsp:include page="sidebar.jsp"/>


    <div class="admin-main">
        <jsp:include page="header.jsp"/>


        <div class="admin-dashboard">
            <div class="page-container">

                <div class="header-section">
                    <h2 class="main-title">Refund Management</h2>

                    <div class="total-refund-box">
                        <span class="total-label">Monthly Total Refund (${currentMonthName})</span>
                        <div class="total-amount">
                            <fmt:formatNumber value="${totalRefundMonth != null ? totalRefundMonth : 0}" type="number"/>
                            <span class="total-currency">VND</span>
                        </div>
                    </div>
                </div>


                <div class="filter-box">
                    <form action="ViewCancelBookingList" method="get">
                        <input type="text" name="search" value="${search}" placeholder="Search service name...">
                        <select name="status">
                            <option value="ALL" ${status == 'ALL' ? 'selected' : ''}>All Status</option>
                            <option value="PendingCancel" ${status == 'PendingCancel' ? 'selected' : ''}>Pending</option>
                            <option value="CancelRefund" ${status == 'CancelRefund' ? 'selected' : ''}>Refunded</option>
                            <option value="RejectedCancelRefund" ${status == 'RejectedCancelRefund' ? 'selected' : ''}>Rejected</option>
                        </select>
                        <button type="submit">Filter</button>
                    </form>
                </div>

                
                <table class="data-table">
                    <thead>
                    <tr>
                        <th width="20%">Service</th>
                        <th width="12%">Status</th>
                        <th width="25%">Notes / Evidence</th>
                        <th width="15%">Amount</th>
                        <th width="13%" class="text-center">Refund Date</th>
                        <th width="15%" class="text-center">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="b" items="${cancelList}">
                        <tr>
                            <td class="font-bold">${b.nameService}</td>
                            <td>
                                   <span class="badge ${b.status == 'PendingCancel' ? 'st-pending' : (b.status == 'CancelRefund' ? 'st-approved' : 'st-rejected')}">
                                           ${b.status == 'PendingCancel' ? 'Pending' : (b.status == 'CancelRefund' ? 'Refunded' : 'Rejected')}
                                   </span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${b.status == 'CancelRefund'}">
                                        <div class="img-container">
                                            <img src="${b.note}" class="img-preview" onclick="openImage('${b.note}')" alt="Evidence">
                                            <span class="view-hint">Click to view</span>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="note-box">${b.note}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="price-text">
                                <fmt:formatNumber value="${b.priceAtBooking}" type="number"/> <span class="currency">VND</span>
                            </td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${not empty b.refundDate}">
                                           <span class="text-date">
                                               <fmt:formatDate value="${b.refundDate}" pattern="MM/dd/yy"/>
                                           </span>
                                    </c:when>
                                    <c:otherwise><span class="text-empty">—</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <c:if test="${b.status == 'PendingCancel'}">
                                    <div class="action-btns">
                                        <a href="ApproveRefund?bookingID=${b.bookingID}" class="btn-approve">Approve</a>
                                        <a href="RejectRefund?bookingID=${b.bookingID}" class="btn-reject">Reject</a>
                                    </div>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty cancelList}">
                        <tr>
                            <td colspan="6" class="no-data">No refund requests found.</td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>


                <c:if test="${totalPage > 1}">
                    <div class="pagination">
                        <c:forEach begin="1" end="${totalPage}" var="i">
                            <a href="ViewCancelBookingList?page=${i}&search=${search}&status=${status}"
                               class="${i == currentPage ? 'active' : ''}">${i}</a>
                        </c:forEach>
                    </div>
                </c:if>


            </div>
        </div>
    </div>
</div>


<div id="imageModal" class="simple-modal" onclick="this.style.display='none'">
    <img id="modalImg">
</div>


<script>
    function openImage(src){
        document.getElementById("imageModal").style.display = "flex";
        document.getElementById("modalImg").src = src;
    }
</script>


</body>
</html>
