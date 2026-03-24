<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cat Clinic Admin - Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/DashboardAdminStyle.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar-admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header-admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
</head>
<body>
    <div class="admin-layout">
        <jsp:include page="sidebar.jsp"/>

        <div class="admin-main">
            <jsp:include page="header.jsp"/>
            <div class="container">
                <header>
                    <h1>Admin Dashboard</h1>
                    <p style="margin-top: 5px; color: #777; font-size: 14px;">Welcome back, the system is running smoothly.</p>
                </header>

                <div class="stats-grid">
                    <div class="card">
                        <div class="icon-box"><i class="fa-solid fa-paw"></i></div>
                        <div class="info">
                            <p>Active Services</p>
                            <h3>${DASHBOARD_DATA.activeServicesCount}</h3>
                        </div>
                    </div>

                    <div class="card">
                        <div class="icon-box"><i class="fa-solid fa-calendar-xmark"></i></div>
                        <div class="info">
                            <p>Refund Requests</p>
                            <h3 class="status-pending">${DASHBOARD_DATA.pendingCancelCount}</h3>
                        </div>
                    </div>

                    <div class="card">
                        <div class="icon-box"><i class="fa-solid fa-coins"></i></div>
                        <div class="info">
                            <p>Monthly Revenue</p>
                            <h3 class="revenue-value">
                                <fmt:formatNumber value="${DASHBOARD_DATA.monthlyRevenue}" type="currency" currencySymbol="$"/>
                            </h3>
                        </div>
                    </div>
                </div>

                <div class="details-grid">
                    <div class="content-box">
                        <h2><i class="fa-solid fa-users-gear"></i> Personnel Distribution by Role</h2>
                        <table class="role-stats-table">
                            <thead>
                                <tr>
                                    <th>User Role</th>
                                    <th style="text-align: right;">Total Accounts</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="entry" items="${DASHBOARD_DATA.accountStats}">
                                    <tr>
                                        <td>
                                            <span class="role-badge">
                                                <i class="fa-solid fa-user-shield" style="font-size: 12px; margin-right: 8px; color: #ccc;"></i>
                                                ${entry.key}
                                            </span>
                                        </td>
                                        <td style="text-align: right;">
                                            <span class="count-circle">${entry.value}</span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="content-box">
                        <h2><i class="fa-solid fa-chart-pie"></i> Account Activity Ratio</h2>
                        <div class="chart-container">
                            <canvas id="accountChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script>
    document.addEventListener('DOMContentLoaded', function() {
        const activeCount = ${DASHBOARD_DATA.activeAccounts};
        const inactiveCount = ${DASHBOARD_DATA.inactiveAccounts};
        const total = activeCount + inactiveCount;
        const activePercentage = total > 0 ? Math.round((activeCount / total) * 100) : 0;

        const ctx = document.getElementById('accountChart').getContext('2d');
        new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['Active', 'Locked'],
                datasets: [{
                    data: [activeCount, inactiveCount],
                    backgroundColor: ['#FF8C00', '#333333'],
                    borderWidth: 0,
                    hoverOffset: 15,
                    cutout: '80%' 
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { position: 'bottom', labels: { boxWidth: 12, padding: 20 } }
                }
            },
            plugins: [{
                id: 'centerText',
                beforeDraw: function(chart) {
                    const { width, height, ctx } = chart;
                    ctx.restore();
                    ctx.font = "bold 2.5em sans-serif";
                    ctx.textBaseline = "middle";
                    ctx.fillStyle = "#FF8C00"; 
                    const text = activePercentage + "%";
                    const textX = Math.round((width - ctx.measureText(text).width) / 2);
                    ctx.fillText(text, textX, height / 2 - 10);

                    ctx.font = "600 1em sans-serif";
                    ctx.fillStyle = "#777";
                    const subText = "Active";
                    const subTextX = Math.round((width - ctx.measureText(subText).width) / 2);
                    ctx.fillText(subText, subTextX, height / 2 + 25);
                    ctx.save();
                }
            }]
        });
    });
    </script>
</body>
</html>