<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <title>Invoice Detail | Cat Clinic</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/receptiondashboard-style.css">

  <style>
    /* ===== LAYOUT CORE ===== */
    .app-container {
      display: flex;
      min-height: calc(100vh - 70px);
      background-color: #f9fafb;
    }

    .main-content {
      flex: 1;
      padding: 30px;
      box-sizing: border-box;
    }

    /* ===== INVOICE DESIGN ===== */
    .invoice-wrapper {
      background: white;
      border-radius: 16px;
      padding: 30px;
      border: 1px solid #edf2f7;
      box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
    }

    .invoice-inner-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 2px solid #f7fafc;
      padding-bottom: 25px;
      margin-bottom: 30px;
    }

    .brand { display: flex; align-items: center; gap: 15px; }

    .logo-box {
      width: 54px; height: 54px;
      background: #fff5eb; color: #ff6b00;
      display: flex; align-items: center; justify-content: center;
      border-radius: 12px; font-size: 24px;
    }

    .clinic-name { margin: 0; font-size: 20px; color: #1a202c; }
    .sub-text { margin: 0; font-size: 13px; color: #718096; }

    .status-badge {
      background: #fffaf0; color: #dd6b20;
      padding: 6px 16px; border-radius: 99px;
      font-size: 12px; font-weight: 700; border: 1px solid #feebc8;
    }

    .invoice-grid {
      display: grid;
      grid-template-columns: 2.2fr 1fr;
      gap: 30px;
    }

    .invoice-card {
      background: #ffffff;
      border: 1px solid #e2e8f0;
      border-radius: 12px;
      padding: 24px;
      margin-bottom: 24px;
    }

    .card-title {
      font-size: 16px; font-weight: 700; color: #2d3748;
      margin-bottom: 20px; display: flex; align-items: center; gap: 10px;
    }

    /* Customer Info */
    .customer-info-grid {
      display: grid; grid-template-columns: 1fr 1fr; gap: 20px;
    }
    .info-group label { font-size: 11px; color: #a0aec0; text-transform: uppercase; }
    .info-group p { margin: 4px 0 0 0; font-weight: 600; color: #2d3748; }
    .text-orange { color: #ff6b00 !important; }

    /* Table */
    .billing-table { width: 100%; border-collapse: collapse; }
    .billing-table th { text-align: left; background: #f8fafc; padding: 12px; font-size: 13px; border-bottom: 2px solid #e2e8f0; }
    .billing-table td { padding: 14px 12px; border-bottom: 1px solid #f1f5f9; font-size: 14px; }

    .tag { padding: 3px 8px; border-radius: 6px; font-size: 10px; font-weight: 700; }
    .tag-blue { background: #e0f2fe; color: #0369a1; }
    .tag-purple { background: #f3f0ff; color: #6d28d9; }

    .invoice-total {
      margin-top: 20px; padding: 20px;
      background: #fff7ed; border-radius: 10px;
      display: flex; justify-content: space-between; align-items: center;
      border: 1px dashed #fed7aa;
    }
    .total-price { color: #ff6b00; font-size: 24px; font-weight: 800; }

    /* Payment Methods - Cải tiến hiệu ứng click */
    .payment-methods { display: flex; gap: 12px; margin-bottom: 20px; }
    .method-btn {
      flex: 1; border: 2px solid #e2e8f0; padding: 16px;
      text-align: center; border-radius: 12px;
      cursor: pointer; transition: all 0.2s ease;
      user-select: none;
    }
    .method-btn:hover { border-color: #cbd5e0; }
    .method-btn.active {
      border-color: #ff6b00; background: #fffaf0; color: #ff6b00;
      box-shadow: 0 4px 12px rgba(255, 107, 0, 0.1);
    }
    .method-btn i { display: block; font-size: 20px; margin-bottom: 8px; }

    /* Buttons */
    .btn-confirm {
      width: 100%; padding: 16px; background: #ff6b00; color: white;
      border: none; border-radius: 10px; font-weight: 700;
      cursor: pointer; font-size: 15px; transition: 0.2s;
    }
    .btn-confirm:hover { background: #e66000; transform: translateY(-1px); }

    .btn-print {
      width: 100%; padding: 12px; margin-top: 12px;
      border: 1px solid #cbd5e0; border-radius: 10px;
      background: white; font-weight: 600; cursor: pointer;
    }
    .invoice-total-box{
      margin-top:20px;
      padding:20px 25px;
      background:#fff7ed;
      border-radius:12px;
      border:1px dashed #fed7aa;
    }

    .total-row{
      display:flex;
      justify-content:space-between;
      align-items:center;
      margin-bottom:10px;
      font-size:15px;
    }

    .total-row .label{
      font-weight:600;
      color:#9a3412;
    }

    .total-row .value{
      font-weight:700;
      color:#1f2937;
    }

    .total-final{
      border-top:1px dashed #fed7aa;
      padding-top:12px;
      margin-top:10px;
    }

    .total-final .value{
      font-size:20px;
      color:#ff6b00;
    }

    .invoice-total div{
      display:flex;
      justify-content:space-between;
      margin-bottom:8px;
    }

    .btn-back { display: block; text-align: center; margin-top: 15px; font-size: 14px; color: #718096; text-decoration: none; }
  </style>
</head>

<body>

<%@include file="header.jsp"%>

<div class="app-container">
  <c:set var="activePage" value="billing" scope="request"/>
  <%@include file="sidebar.jsp"%>

  <main class="main-content">
    <div class="hub-header">
      <h2>Invoice Detail</h2>
    </div>

    <div class="invoice-wrapper">
      <div class="invoice-inner-header">
        <div class="brand">
          <div class="logo-box"><i class="fa-solid fa-file-invoice-dollar"></i></div>
          <div>
            <h3 class="clinic-name">CAT CLINIC</h3>
            <p class="sub-text">Veterinary Service Invoice</p>
          </div>
        </div>
        <div>
          <span class="status-badge">${inforInvoiceDetail.invoiceStatus}</span>
          <p class="create-date" style="font-size: 12px; color: #a0aec0; margin-top: 8px;">Created Date:<fmt:formatDate value="${inforInvoiceDetail.createdDate}" pattern="yyyy-MM-dd"/> </p>
        </div>
      </div>

      <div class="invoice-grid">
        <div class="invoice-left-column">
          <div class="invoice-card">
            <h3 class="card-title"><i class="fa-solid fa-user-tag"></i>Customer Information</h3>
            <div class="customer-info-grid">
              <div class="info-group"><label>Customer</label><p> ${inforInvoiceDetail.customerName}</p></div>
              <div class="info-group"><label>Phone Number</label><p>${inforInvoiceDetail.phone}</p></div>
              <div class="info-group"><label>Cat</label><p>${inforInvoiceDetail.petName}</p></div>
              <div class="info-group"><label>Booking ID</label><p class="text-orange">${inforInvoiceDetail.bookingCode}</p></div>
            </div>
          </div>

          <div class="invoice-card">
            <h3 class="card-title"><i class="fa-solid fa-receipt"></i>Service Details</h3>
            <table class="billing-table">
              <thead>
              <tr>
                <th>No</th>
                <th>Item</th>
                <th>Quantity</th>
                <th>Price</th>
                <th>Amount</th></tr>
              </thead>
              <tbody>
              <c:forEach var="service" items="${listServiceUse}">
              <tr>
                <td>${service.id}</td>
                <td>${service.itemName}</td>
                <td>${service.quantity}</td>
                <td> <fmt:formatNumber value="${service.price}" type="number" groupingUsed="true"/></td>
                <td> <fmt:formatNumber value="${service.total}" type="number" groupingUsed="true"/></td></tr>
              </c:forEach>
              </tbody>

            </table>
            <div class="invoice-total-box">

              <div class="total-row">
                <span class="label">Total Service</span>
                <span class="value">
      <fmt:formatNumber value="${totalServiceAmount}" type="number" groupingUsed="true"/> VND
    </span>
              </div>

              <div class="total-row">
                <span class="label">Deposit</span>
                <span class="value">
      <fmt:formatNumber value="${deposit}" type="number" groupingUsed="true"/> VND
    </span>
              </div>

              <div class="total-row">
                <span class="label">Paid</span>
                <span class="value">
      <fmt:formatNumber value="${hasPaid - deposit}" type="number" groupingUsed="true"/> VND
    </span>
              </div>

              <div class="total-row total-final">
                <span class="label">Remaining</span>

                <span class="value">
      <c:choose>


        <c:when test="${inforInvoiceDetail.invoiceStatus == 'Paid'}">
          0 VND
        </c:when>


        <c:otherwise>
          <fmt:formatNumber value="${totalServiceAmount - hasPaid}" type="number" groupingUsed="true"/> VND
        </c:otherwise>

      </c:choose>
    </span>

              </div>

            </div>
          </div>
        </div>

        <div class="invoice-right-column">
          <div class="invoice-card">
            <h3 class="card-title"><i class="fa-solid fa-credit-card"></i>Payment</h3>

            <c:if test="${inforInvoiceDetail.invoiceStatus != 'Paid'}">
              <p style="font-size: 13px; color: #718096; margin-bottom: 12px;">Select Payment Method</p>

              <form action="${pageContext.request.contextPath}/reception/invoice_detail" method="POST" id="paymentForm">
                <input type="hidden" name="paymentMethod" id="selectedMethod" value="Cash">
                <input type="hidden" name="bookingID" value="${inforInvoiceDetail.bookingCode}">

                <div class="payment-methods">
                  <div class="method-btn active" data-value="Cash">
                    <i class="fa-solid fa-money-bill-wave"></i>
                    <span>Cash</span>
                  </div>
                  <div class="method-btn" data-value="Bank Transfer">
                    <i class="fa-solid fa-mobile-screen"></i>
                    <span>Bank Transfer</span>
                  </div>
                </div>

                <button type="submit" class="btn-confirm">Confirm Payment</button>
              </form>
            </c:if>
            <a href="${pageContext.request.contextPath}/reception/billing-bookings" class="btn-back">
              <i class="fa-solid fa-arrow-left-long"></i> Back to Billing Booking
            </a>

          </div>
        </div>
      </div>
    </div>
  </main>
</div>

<script>
  document.addEventListener('DOMContentLoaded', function() {
    const methodBtns = document.querySelectorAll('.method-btn');
    const hiddenInput = document.getElementById('selectedMethod');

    methodBtns.forEach(btn => {
      btn.addEventListener('click', function() {
        // 1. Xóa class active ở tất cả các nút
        methodBtns.forEach(b => b.classList.remove('active'));

        // 2. Thêm class active cho nút vừa chọn
        this.classList.add('active');

        // 3. Cập nhật giá trị vào input ẩn để gửi đi
        const value = this.getAttribute('data-value');
        hiddenInput.value = value;

        console.log("Đã chọn phương thức:", value);
      });
    });
  });
</script>

</body>
</html>