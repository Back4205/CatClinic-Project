<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Counter Check-out | CatClinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reception-checkout.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

<%@include file="header.jsp" %>

<div class="app-container">
    <c:set var="activePage" value="CheckOut" scope="request"/>
    <%@include file="sidebar.jsp" %>

    <main class="main-content">
        <div class="hub-header">
            <h2>Receptionist Hub</h2>
            <p>Welcome back, ${sessionScope.acc.fullName}.</p>
        </div>

        <div class="table-section" id="checkout-queue-table">
            <div class="table-header">
                <h3>Check-out Queue</h3>
            </div>

            <form action="${pageContext.request.contextPath}/reception/checkout-queue" method="GET">
                <div class="table-toolbar">
                    <div class="table-search">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        <input type="text" name="search" value="${currentSearch}" placeholder="Search cat, phone...">
                    </div>
                    <div class="table-datepicker">
                        <input type="date" name="dateFilter" id="dateFilter" value="${currentDate}">
                    </div>
                    <button type="submit" class="btn-search-orange">Search</button>
                </div>
            </form>

            <table class="modern-table">
                <thead>
                <tr>
                    <th>PATIENT</th>
                    <th class="text-center">CHECK-IN TIME</th>
                    <th class="text-center">ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${checkoutList}" var="b">
                    <tr>
                        <td>
                            <div class="patient-cell">
                                <div class="cat-avatar">🐱</div>
                                <div class="patient-info">
                                    <p class="patient-name"><strong>${b.catName}</strong></p>
                                    <p class="patient-id">Owner: ${b.ownerName} • <span class="phone-highlight">${b.ownerPhone}</span></p>
                                </div>
                            </div>
                        </td>
                        <td class="text-center">
                            <span class="time-text">
                                <fmt:formatDate value="${b.checkInTime}" pattern="HH:mm - dd/MM"/>
                            </span>
                        </td>
                        <td class="text-center">
                            <button type="button" onclick="openCheckoutModal(${b.bookingID}, '${b.catName}')" class="btn-checkout-table">
                                Check Out
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty checkoutList}">
                    <tr><td colspan="3" class="text-center" style="padding: 40px;">Hôm nay chưa có bé nào cần Check-out!</td></tr>
                </c:if>
                </tbody>
            </table>

            <div class="pagination-container">
                <ul class="pagination">
                    <c:if test="${tag > 1}">
                        <li class="page-item"><a href="checkout-queue?index=${tag - 1}&search=${currentSearch}&dateFilter=${currentDate}#checkout-queue-table">&laquo;</a></li>
                    </c:if>
                    <c:forEach begin="1" end="${endP}" var="i">
                        <li class="page-item ${tag == i ? 'active' : ''}">
                            <a href="checkout-queue?index=${i}&search=${currentSearch}&dateFilter=${currentDate}#checkout-queue-table">${i}</a>
                        </li>
                    </c:forEach>
                    <c:if test="${tag < endP}">
                        <li class="page-item"><a href="checkout-queue?index=${tag + 1}&search=${currentSearch}&dateFilter=${currentDate}#checkout-queue-table">&raquo;</a></li>
                    </c:if>
                </ul>
            </div>
        </div>
    </main>
</div>

<div id="checkoutModal" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header-orange">
            <h3>Finalize Check-Out</h3>
            <p id="modalCatName" style="margin-top: 5px; opacity: 0.9;"></p>
        </div>
        <div class="modal-body">
            <label>RELEASE CONDITION:</label>
            <textarea id="checkoutNote" placeholder="Ví dụ: Bé khỏe mạnh, đã thanh toán..."></textarea>
            <div class="modal-footer">
                <button type="button" onclick="closeModal()" class="btn-modal-cancel">Cancel</button>
                <button type="button" id="confirmBtn" class="btn-modal-confirm">Confirm & Pay</button>

            </div>
        </div>
    </div>
</div>

<script>
    function openCheckoutModal(id, name) {
        document.getElementById('modalCatName').innerText = "Patient: " + name;
        document.getElementById('checkoutModal').style.display = 'flex';

        document.getElementById('confirmBtn').onclick = function() {
            var note = document.getElementById('checkoutNote').value;
            if (!note.trim()) { alert("Vui lòng nhập tình trạng!"); return; }
            window.location.href = "${pageContext.request.contextPath}/reception/checkout?id=" + id + "&condition=" + encodeURIComponent(note);
        };
    }
    function closeModal() { document.getElementById('checkoutModal').style.display = 'none'; }
    window.onclick = function(event) { if (event.target == document.getElementById('checkoutModal')) closeModal(); }
</script>
<script>
    window.addEventListener('load', function() {
        const urlParams = new URLSearchParams(window.location.search);
        const status = urlParams.get('status');

        if (status === 'success') {
            alert("✅ Check-out thành công! Bé đã hoàn tất quy trình nội trú.");

            const newUrl = window.location.pathname + window.location.hash;
            window.history.replaceState({}, document.title, newUrl);
        } else if (status === 'error') {
            alert("❌ Có lỗi xảy ra trong quá trình Check-out. Vui lòng thử lại!");
            const newUrl = window.location.pathname + window.location.hash;
            window.history.replaceState({}, document.title, newUrl);
        }
    });
</script>
<%@include file="footer.jsp" %>
</body>
</html>