<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard - Cat Clinic</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/DashboardAdminStyle.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar-admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header-admin.css">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-dashboard.css">
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<body>
<div class="admin-layout">
    <jsp:include page="sidebar.jsp"/>

    <div class="admin-main">
        <jsp:include page="header.jsp"/>

        <div class="admin-dashboard">
            <h2 class="admin-title">Admin Management Dashboard</h2>

            <div class="admin-cards">
                <div class="admin-card">
                    <i class="fa-solid fa-bell-concierge"></i>
                    <p>Active Services</p>
                    <h3>${data.totalServices}</h3>
                </div>
                <div class="admin-card" style="border-left: 4px solid #ef4444;">
                    <i class="fa-solid fa-hand-holding-dollar" style="color: #ef4444;"></i>
                    <p>Refund Requests</p>
                    <h3 style="color: #ef4444;">${data.pendingCancelCount}</h3>
                </div>
            </div>

            <div class="admin-grid">
                
                <div class="admin-welcome-stats">
                    <h3><i class="fa-solid fa-users-gear"></i> Account Classification</h3>
                    
                    <form action="AdminDashboard" method="GET">
                        <div class="role-list-container">
                            <c:forEach items="${data.accountStats}" var="entry">
                                <label class="role-item-label">
                                    <span class="role-info-wrapper">
                                        <input type="checkbox" name="roles" value="${entry.key}" 
                                            ${fn:contains(paramValues.roles, entry.key) ? 'checked' : ''}>
                                        <span class="role-name">${entry.key}</span>
                                    </span>
                                    <span class="role-badge">${entry.value}</span>
                                </label>
                            </c:forEach>
                        </div>
                        <button type="submit" class="btn-update-stats">Update Statistics</button>
                    </form>
                    
                    <div class="total-display-box">
                        <p class="total-label">Total Selected Accounts</p>
                        <h2 class="total-number">${totalSelected}</h2>
                    </div>
                </div>

                <div class="admin-welcome-stats chart-container">
                    <h3><i class="fa-solid fa-chart-pie"></i> Service Usage Rate</h3>
                    
                    <div class="pie-chart" style="background: conic-gradient(${pieGradient});"></div>

                    <div class="chart-legend">
                        <c:forEach items="${data.serviceUsages}" var="item" varStatus="status">
                            <c:if test="${status.index < 4}">
                                <div class="legend-item">
                                    <span class="dot" style="background: 
                                        ${status.index==0 ? '#4338ca' : 
                                          status.index==1 ? '#10b981' : 
                                          status.index==2 ? '#f59e0b' : '#ef4444'};">
                                    </span> 
                                    ${item.name} (${item.count})
                                </div>
                            </c:if>
                        </c:forEach>
                        <c:if test="${fn:length(data.serviceUsages) > 4}">
                            <div class="legend-item">
                                <span class="dot" style="background: #94a3b8;"></span> Others...
                            </div>
                        </c:if>
                    </div>
                </div>

            </div> </div>
    </div>
</div>
</body>
</html>