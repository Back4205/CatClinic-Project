<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>

        <title>Refund Requests</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar-admin.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/booking/cancel-booking.css">

        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    </head>

    <body>

        <!-- Sidebar -->
        <jsp:include page="sidebar.jsp"/>

        <!-- Main content -->
        <div class="main-content">

            <div class="page-wrapper">

                <h2 class="page-title">
                    <i class="fa-solid fa-rotate-left"></i>
                    Pending Cancel & Refund Requests
                </h2>

                <div class="table-container">

                    <table class="refund-table">

                        <thead>
                            <tr>
                                <th>Booking ID</th>
                                <th>Service</th>
                                <th>Status</th>
                                <th>Note</th>
                                <th>Price</th>
                                <th>Actions</th>
                            </tr>
                        </thead>

                        <tbody>

                            <c:forEach var="b" items="${cancelList}">

                                <tr>

                                    <td>${b.bookingID}</td>

                                    <td>
                                        <i class="fa-solid fa-stethoscope service-icon"></i>
                                        ${b.nameService}
                                    </td>

                                    <td>

                                        <span class="status
                                              ${b.status == 'PendingCancelRefund' ? 'pending' : ''}
                                              ${b.status == 'CancelRefund' ? 'approved' : ''}
                                              ${b.status == 'RejectedCancelRefund' ? 'rejected' : ''}">

                                            ${b.status}

                                        </span>

                                    </td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${b.status == 'CancelRefund'}">

                                                <div class="refund-img">
                                                    <img src="${b.note}" 
                                                         alt="Refund proof"
                                                         class="refund-thumb"
                                                         onclick="openImage('${b.note}')">
                                                </div>

                                            </c:when>

                                            <c:otherwise>

                                                <span class="note-text">${b.note}</span>

                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td class="price">
                                        ${b.priceAtBooking} VND
                                    </td>

                                    <td>

                                        <c:if test="${b.status == 'PendingCancelRefund'}">

                                            <div class="action-buttons">

                                                <a class="btn approve"
                                                   href="${pageContext.request.contextPath}/ApproveRefund?bookingID=${b.bookingID}">
                                                    <i class="fa-solid fa-check"></i> Approve
                                                </a>

                                                <a class="btn reject"
                                                   href="${pageContext.request.contextPath}/RejectRefund?bookingID=${b.bookingID}">
                                                    <i class="fa-solid fa-xmark"></i> Reject
                                                </a>

                                            </div>

                                        </c:if>

                                    </td>

                                </tr>

                            </c:forEach>

                        </tbody>

                    </table>

                </div>

            </div>

        </div>
        <!-- Image Modal -->

        <div id="imageModal" class="modal">

            <span class="close" onclick="closeImage()">&times;</span>

            <img class="modal-content" id="modalImg">

        </div>
        <script>

function openImage(src){
    document.getElementById("imageModal").style.display = "block";
    document.getElementById("modalImg").src = src;
}

function closeImage(){
    document.getElementById("imageModal").style.display = "none";
}

        </script>
    </body>
</html>