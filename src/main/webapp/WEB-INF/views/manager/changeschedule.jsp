<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>New Request</title>
        <link href="css/ChangeVetScheduleStyle.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div class="request-card">
            <a href="viewschedule" class="btn-back">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <line x1="19" y1="12" x2="5" y2="12"></line>
                <polyline points="12 19 5 12 12 5"></polyline>
                </svg>
                Back
            </a>
            <div class="header">
            Create a vacation schedule
            </div>
            <form action="changvetschedule" method="post" onsubmit="return validateForm()">
                <input type="hidden" name="VetID" value="${VetID}">
                <label>REQUEST TYPE</label>
                <select name="requestType" id="requestType">
                    <option value="single">Single Day</option>
                    <option value="range">Date Range</option>
                </select>

                <div id="singleBox">
                    <label>DATE</label>
                    <input type="date" name="requestDate">
                    <label>TIME SLOT</label>
                    <div class="slot-container">
                        <c:forEach var="s" items="${slots}">
                            <label class="slot-card">

                                <input type="checkbox" name="slot" value="${s.slotID}">

                                <div class="slot-info">
                                    <span class="slot-title">Slot ${s.slotID}</span>
                                    <span class="slot-time">
                                        ${s.startTime} - ${s.endTime}
                                    </span>
                                </div>

                            </label>
                        </c:forEach>
                    </div>
                </div>
                <div id="rangeBox" style="display:none;">
                    <label>FROM DATE</label>
                    <input type="date" name="fromDate">
                    <label>TO DATE</label>
                    <input type="date" name="toDate">

                </div>
                <button type="submit">Submit Request</button>
            </form>
        </div>

        <script>

            const typeSelect = document.getElementById("requestType");
            const singleBox = document.getElementById("singleBox");
            const rangeBox = document.getElementById("rangeBox");

            typeSelect.addEventListener("change", function () {

                if (this.value === "range") {
                    singleBox.style.display = "none";
                    rangeBox.style.display = "block";
                } else {
                    singleBox.style.display = "block";
                    rangeBox.style.display = "none";
                }

            });

            function validateForm() {

                const type = document.getElementById("requestType").value;

                if (type === "single") {

                    const date = document.querySelector("input[name='requestDate']").value;
                    const slots = document.querySelectorAll("input[name='slot']:checked");

                    if (date === "") {
                        alert("Please select a date");
                        return false;
                    }

                    if (slots.length === 0) {
                        alert("Please select at least 1 time slot");
                        return false;
                    }
                }

                if (type === "range") {

                    const from = document.querySelector("input[name='fromDate']").value;
                    const to = document.querySelector("input[name='toDate']").value;

                    if (from === "" || to === "") {
                        alert("Please select From Date and To Date");
                        return false;
                    }

                    if (from > to) {
                        alert("From Date must be before To Date");
                        return false;
                    }
                }

                // hỏi xác nhận
                return confirm("Are you sure you want to submit this request?");

            }
            

        </script>

    </body>
</html>