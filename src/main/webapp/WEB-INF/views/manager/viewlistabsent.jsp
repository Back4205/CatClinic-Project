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
            </form>
            <form action="absentController" method="POST">
                <div class="card list-container">
                    <div class="list-header">
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
                                        <input type="checkbox" name="selectedIds" value="${slot.slotID}">
                                    </div>
                                    <div class="col-time">
                                        <div class="time-text">Slot ${slot.slotID}</div>
                                        <div class="label-muted">${slot.startTime} - ${slot.endTime}</div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>
                    </div>
                </div>
            </form>
        </div>
        <script>
            function toggleAll(source) {
                var checkboxes = document.getElementsByName('selectedIds');
                for (var i = 0; i < checkboxes.length; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
            const requestTypeSelect = document.getElementById('requestType');
            const dateSingle = document.querySelector('.date-single');
            const dateRange = document.querySelector('.date-range');

            function updateDatePicker() {
                if (requestTypeSelect.value === 'single') {
                    dateSingle.style.display = 'block';
                    dateRange.style.display = 'none';
                } else {
                    dateSingle.style.display = 'none';
                    dateRange.style.display = 'flex';
                }
            }

            updateDatePicker();
            requestTypeSelect.addEventListener('change', updateDatePicker);
        </script>

    </body>
</html>