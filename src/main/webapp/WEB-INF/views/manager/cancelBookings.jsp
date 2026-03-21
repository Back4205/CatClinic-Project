<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/slidebar-admin.css">
<link rel="stylesheet"
href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

<!DOCTYPE html>
<html>
<head>
    <title>Refund Requests</title>
</head>
<body>

<jsp:include page="slidebar.jsp" />

<div class="main-content">
    <div class="container">
        <h2>Pending Refund Requests</h2>

        <table>
            <thead>
                <tr>
                    <th>Booking ID</th>
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
                            <span class="status pending">
                                ${b.status}
                            </span>
                        </td>
                        <td>${b.note}</td>
                        <td>$${b.priceAtBooking}</td>
                        <td>
                            <a class="btn approve"
                               href="ApproveRefund?bookingID=${b.bookingID}">
                                Approve
                            </a>

                            <a class="btn reject"
                               href="RejectRefund?bookingID=${b.bookingID}">
                                Reject
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>

        </table>
    </div>
</div>

</body>
</html>