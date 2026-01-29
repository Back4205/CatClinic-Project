    <%--
      Created by IntelliJ IDEA.
      User: ADMIN
      Date: 1/27/2026
      Time: 12:37 PM
      To change this template use File | Settings | File Templates.
    --%>
    <%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <style>
        table {
            border-collapse: collapse;
            width: 100%;
            margin-bottom: 20px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 8px 12px;
        }

        th {
            background-color: #f2f2f2;
            text-align: left;
        }

        td.center {
            text-align: center;
        }

        td.right {
            text-align: right;
        }

        h2, h3 {
            margin-top: 20px;
        }
    </style>

    <h2>Medical Detail</h2>

    <c:if test="${empty medicalDetail}">
        <p style="color:red;">No medical detail found.</p>
    </c:if>

    <c:if test="${not empty medicalDetail}">

        <table>
            <tr>
                <th>Doctor</th>
                <td>${medicalDetail.doctorName}</td>
            </tr>
            <tr>
                <th>Clinical Note</th>
                <td>${medicalDetail.clinicalNote}</td>
            </tr>
        </table>

        <h3>Services</h3>
        <table>
            <tr>
                <th>Service Name</th>
                <th>Time</th>
                <th>Description</th>
                <th class="right">Price</th>
            </tr>

            <c:forEach items="${serviceList}" var="s">
                <tr>
                    <td>${s.nameService}</td>
                    <td class="center">${s.timeService}</td>
                    <td>${s.description}</td>
                    <td class="right">${s.price}</td>
                </tr>
            </c:forEach>
        </table>

        <h3>Drugs</h3>
        <table>
            <tr>
                <th>Drug Name</th>
                <th>Instruction</th>
                <th class="center">Unit</th>
                <th class="center">Quantity</th>
            </tr>

            <c:forEach items="${drugList}" var="d">
                <tr>
                    <td>${d.drugName}</td>
                    <td>${d.instruction}</td>
                    <td class="center">${d.unit}</td>
                    <td class="center">${d.quantity}</td>
                </tr>
            </c:forEach>
        </table>

    </c:if>

    <button type="button"
            onclick="window.location.href='${pageContext.request.contextPath}/cats'">
        Back to Cat List
    </button>
