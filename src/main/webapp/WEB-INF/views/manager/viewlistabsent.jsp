<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản lý lịch nghỉ</title>
        <link rel="stylesheet" type="text/css" href="style.css">
        <link href="css/ViewListAbsent.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div class="container">
            <form action="absentController" method="GET">
                <input type="hidden" name="VetID" value="${VetID}">

                <div class="card">

                    <div class="filter-header">

                        <div class="header-title">
                            <h2>Bộ lọc lịch nghỉ</h2>
                            <p>Chọn phương thức lọc phù hợp</p>
                        </div>

                        <select name="requestType" id="requestType">
                            <option value="single" ${requestType == 'single' ? 'selected' : ''}>Single Day</option>
                            <option value="range" ${requestType == 'range' ? 'selected' : ''}>Date Range</option>
                        </select>

                        <div class="date-picker-group">

                            <div class="date-single">
                                <label>Ngày</label>
                                <input type="date" name="dateSingle" value="${dateSingle}">
                            </div>

                            <div class="date-range">

                                <div class="date-group">
                                    <label>From</label>
                                    <input type="date" name="dateFrom" value="${dateFrom}">
                                </div>

                                <div style="padding-bottom: 10px; color: #cbd5e0;"> &rsaquo; </div>

                                <div class="date-group">
                                    <label>To</label>
                                    <input type="date" name="dateTo" value="${dateTo}">
                                </div>

                            </div>

                            <button type="submit" class="btn-search">Tìm kiếm</button>

                        </div>

                    </div>

                </div>

            </form>
            <form action="absentController" method="POST" id="formCancel">
                <input type="hidden" name="VetID" value="${VetID}">

                <div class="card list-container">
                    <div class="list-header">
                        <span id="selectedCount" style="margin-left:10px;color:#666;">
                            Đã chọn: 0
                        </span>
                        <div style="display: flex; align-items: center;">
                            <input type="checkbox" id="checkAll" onclick="toggleAll(this)">
                            <label for="checkAll" style="font-weight: bold; margin-left: 10px; cursor: pointer;">
                                Chọn tất cả (${list != null ? list.size() : 0})                            
                            </label>
                        </div>
                        <div style="display: flex; gap: 10px;">
                            <button type="submit" class="btn-cancel-selected">
                                <span>🗑</span> Hủy lịch đã chọn
                            </button>
                            <button type="button" class="btn-cancel-selected" style="color: #666;" onclick="location.reload()">
                                <span>↻</span>
                            </button>
                        </div>
                    </div>
                    <div class="list-content">
                        <c:if test="${list == null}">
                            <div class="empty-state">
                                <span>📅</span>
                                <p>Không có lịch nghỉ nào trong khoảng thời gian chọn</p>
                            </div>
                        </c:if>
                        <c:if test="${not empty list}">
                            <c:forEach var="slot" items="${list}">
                                <div class="list-item">
                                    <div class="col-check">
                                        <input type="checkbox" 
                                               name="selectedIds" 
                                               value="${VetID}|${slot.slotID}|${slot.date}">                                    </div>
                                    <div class="col-time">
                                        <div class="time-text">Slot ${slot.slotID}</div>
                                        <div class="label-muted">${slot.startTime} - ${slot.endTime}</div>
                                    </div>
                                    <div class="col-date">
                                        <div class="time-text">Date: ${slot.date}</div>
                                    </div>

                                    <div class="col-date">
                                        <span class="status-badge status-absent">${slot.status}</span>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>
                    </div>
                </div>
            </form>
             <c:if test="${not empty sessionScope['toast-messenger']}">
                    <div id="toast-message">
                        ${sessionScope['toast-messenger']}
                    </div>
                    <c:remove var="toast-messenger" scope="session"/>
             </c:if>
              
            <c:if test="${not empty sessionScope['toast-messenger-success']}">
                <div id="bubble-overlay">
                    <div id="bubble-box">
                        <div class="bubble-icon">
                            ✔
                        </div>
                        <p class="bubble-text">
                            ${sessionScope['toast-messenger-success']}
                        </p>
                        <button id="bubble-continue-btn">
                            Continue
                        </button>
                    </div>
                </div>
                <c:remove var="toast-messenger-success" scope="session"/>
            </c:if>
        </div>
        <script>

// ===== CHECKBOX SELECT =====

            function updateSelectedCount() {

                const checkboxes = document.querySelectorAll("input[name='selectedIds']");
                const checked = document.querySelectorAll("input[name='selectedIds']:checked").length;
                const total = checkboxes.length;

                const label = document.getElementById("selectedCount");
                const checkAll = document.getElementById("checkAll");

                if (checked === total && total > 0) {
                    label.innerText = "Full list selected";
                    checkAll.checked = true;
                } else {
                    label.innerText = "Đã chọn: " + checked + " / " + total;
                    checkAll.checked = false;
                }
            }

            function toggleAll(source) {

                const checkboxes = document.querySelectorAll("input[name='selectedIds']");

                checkboxes.forEach(cb => {
                    cb.checked = source.checked;
                });

                updateSelectedCount();
            }

            document.querySelectorAll("input[name='selectedIds']").forEach(cb => {
                cb.addEventListener("change", updateSelectedCount);
            });


// ===== DATE FILTER =====

            const requestTypeSelect = document.getElementById("requestType");
            const dateSingle = document.querySelector(".date-single");
            const dateRange = document.querySelector(".date-range");

            function updateDatePicker() {

                if (requestTypeSelect.value === "single") {
                    dateSingle.style.display = "block";
                    dateRange.style.display = "none";
                } else {
                    dateSingle.style.display = "none";
                    dateRange.style.display = "flex";
                }

            }

            updateDatePicker();
            requestTypeSelect.addEventListener("change", updateDatePicker);

            document.getElementById("formCancel").addEventListener("submit", function (e) {
                const checked = document.querySelectorAll("input[name='selectedIds']:checked");

                if (checked.length === 0) {
                    alert("Bạn chưa chọn lịch nghỉ nào!");
                    e.preventDefault();
                    return;
                }

                if (!confirm("Bạn có chắc muốn hủy các lịch nghỉ đã chọn?")) {
                    e.preventDefault();
                }
            });
            document.addEventListener("DOMContentLoaded", function () {
                const btn = document.getElementById("bubble-continue-btn");
                if (btn) {
                    btn.addEventListener("click", function () {
                        window.location.href = "absentController?VetID=${param.VetID}";
                    });
                }

            });
            document.addEventListener("DOMContentLoaded", function () {
                const toast = document.getElementById("toast-message");
                if (toast) {
                    setTimeout(function () {
                        toast.style.opacity = "0";
                        setTimeout(function () {
                            toast.remove();
                        }, 500);
                    }, 5000);
                }
            });
        </script>

    </body>
</html>