<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <title>Invoice Detail | Cat Clinic</title>


  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/v4-shims.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">

  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/receptiondashboard-style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Invoice/Invoce_detail.css">
  <script src="${pageContext.request.contextPath}/js/PaymentInvoice/payment_invoice.js"></script>
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
          <div class="logo-box">
            <i class="fa-solid fa-file-invoice-dollar"></i>
          </div>
          <div>
            <h3 class="clinic-name">CAT CLINIC</h3>
            <p class="sub-text">Veterinary Service Invoice</p>
          </div>
        </div>
        <div style="text-align: right;">
          <span class="status-badge ${inforInvoiceDetail.invoiceStatus.toLowerCase()}">${inforInvoiceDetail.invoiceStatus}</span>
          <p class="create-date">Created: <fmt:formatDate value="${inforInvoiceDetail.createdDate}" pattern="yyyy-MM-dd"/></p>
        </div>
      </div>

      <div class="invoice-grid">
        <div class="invoice-left-column">
          <div class="invoice-card">
            <h3 class="card-title">
              <i class="fa-solid fa-user-tag"></i> Customer Information
            </h3>
            <div class="customer-info-grid">
              <div class="info-group">
                <label>Customer</label>
                <p>${inforInvoiceDetail.customerName}</p>
              </div>
              <div class="info-group">
                <label>Phone</label>
                <p>${inforInvoiceDetail.phone}</p>
              </div>
              <div class="info-group">
                <label>Cat</label>
                <p>${inforInvoiceDetail.petName}</p>
              </div>
              <div class="info-group">
                <label>Booking ID</label>
                <p class="text-orange">${inforInvoiceDetail.bookingCode}</p>
              </div>
            </div>
          </div>

          <div class="invoice-card">
            <h3 class="card-title">
              <i class="fa-solid fa-receipt"></i> Service Details
            </h3>
            <table class="billing-table">
              <thead>
              <tr>
                <th>No</th>
                <th>Item</th>
                <th>Qty</th>
                <th>Price</th>
                <th>Amount</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="service" items="${listServiceUse}" varStatus="status">
                <tr>
                  <td>${status.index + 1}</td>
                  <td>${service.itemName}</td>
                  <td>${service.quantity}</td>
                  <td><fmt:formatNumber value="${service.price}" type="number" groupingUsed="true"/> VND</td>
                  <td><fmt:formatNumber value="${service.total}" type="number" groupingUsed="true"/> VND</td>
                </tr>
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
            <h3 class="card-title">
              <i class="fa-solid fa-credit-card"></i> Payment
            </h3>

            <c:if test="${inforInvoiceDetail.invoiceStatus != 'Paid'}">
              <p style="font-size: 13px; color: #718096; margin-bottom: 12px;">
                <i class="fa-regular fa-circle-check"></i> Select Payment Method
              </p>

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

                <button type="submit" class="btn-confirm">
                  <i class="fa-regular fa-circle-check"></i> Confirm Payment
                </button>
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



</body>
<footer style="background: #ffffff; border-top: 1px solid #e5e7eb; padding: 25px 0; text-align: center; color: #64748b; font-size: 14px; margin-top: auto;">
  <div class="footer-content">
    &copy; 2026 CatClinic. All rights reserved.
  </div>
</footer>
</html>