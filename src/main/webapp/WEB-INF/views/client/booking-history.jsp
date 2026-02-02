<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Booking History | CatClinic</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/my-profile.css">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/booking-history.css">
    
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>

    <header class="topbar">
        <div class="logo"><i class="bi bi-hospital"></i> CatClinic</div>
        <div class="user-info">
            <span class="name">${user.userName}</span>
            <div class="avatar"><i class="bi bi-person-circle"></i></div>
        </div>
    </header>

    <div class="container">
        
        <aside class="sidebar">
            <div class="profile-card">
                <div class="avatar large"><i class="bi bi-person-circle"></i></div>
                <h3>${user.userName}</h3>
                <p>CATCLINIC PORTAL</p>
            </div>
            <nav class="menu">
                <a href="${pageContext.request.contextPath}/cats"><i class="bi bi-grid-fill"></i> Cat List</a>
                
                <a href="booking-history" class="active"><i class="bi bi-calendar-event"></i> Booking History</a>
                
                <a href="accessprofile"><i class="bi bi-person-gear"></i> Profile & Security</a>
                <a href="#"><i class="bi bi-house"></i> Home</a>
                <a href="#" style="color: red; margin-top: 20px;"><i class="bi bi-box-arrow-right"></i> Logout</a>
            </nav>
        </aside>

        <main class="content">
            
            <div class="page-title">
                <h2>BOOKING HISTORY</h2>
                <p>MANAGEMENT LOG FOR MEDICAL AND GROOMING SERVICES.</p>
            </div>

            <div class="stats-container">
                <div class="stat-card">
                    <div class="stat-icon icon-total"><i class="bi bi-clock-history"></i></div>
                    <div class="stat-info">
                        <h3>${total}</h3>
                        <p>Total Visits</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon icon-scheduled"><i class="bi bi-calendar-check"></i></div>
                    <div class="stat-info">
                        <h3>${scheduled}</h3>
                        <p>Scheduled</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon icon-completed"><i class="bi bi-check-circle-fill"></i></div>
                    <div class="stat-info">
                        <h3>${completed}</h3>
                        <p>Completed</p>
                    </div>
                </div>
            </div>

            <form action="booking-history" method="GET">
                <div class="filter-bar">
                    
                    <input type="text" name="search" class="search-input" 
                           placeholder="Search cat name or service..." 
                           value="${currentSearch}">
                    
                    <div class="filter-buttons">
                        <button type="submit" name="status" value="ALL" 
                                class="${empty currentStatus || currentStatus == 'ALL' ? 'active' : ''}">
                            ALL STATUS
                        </button>
                        
                        <button type="submit" name="status" value="Completed"
                                class="${currentStatus == 'Completed' ? 'active' : ''}">
                            COMPLETED
                        </button>
                        
                        <button type="submit" name="status" value="Confirmed"
                                class="${currentStatus == 'Confirmed' ? 'active' : ''}">
                            UPCOMING
                        </button>
                        
                        <button type="submit" name="status" value="Cancelled"
                                class="${currentStatus == 'Cancelled' ? 'active' : ''}">
                            CANCELLED
                        </button>
                    </div>
                </div>
            </form>

            <div class="booking-list">
                
                <c:forEach var="b" items="${bookingList}">
                    <div class="booking-item">
                        <div class="cat-info">
                            <strong>${b.catName}</strong>
                            <span>${b.catBreed}</span>
                        </div>
                        
                        <div class="datetime">
                            <span><i class="bi bi-calendar3"></i> ${b.appointmentDate}</span>
                            <span style="font-size: 12px; color: #888; margin-left: 25px;">
                                ${b.appointmentTime}
                            </span>
                        </div>

                        <div class="service-type">
                            <i class="bi ${b.serviceType == 'Spa' ? 'bi-scissors' : 'bi-capsule'}"></i>
                            ${b.serviceName}
                        </div>

                        <div class="price">
                            $${b.price}
                        </div>

                        <div>
                            <span class="status-badge 
                                ${b.status == 'Completed' || b.status == 'Done' ? 'status-completed' : ''}
                                ${b.status == 'Confirmed' || b.status == 'Upcoming' || b.status == 'Pending' ? 'status-upcoming' : ''}
                                ${b.status == 'Cancelled' ? 'status-cancelled' : ''}
                                ${b.status == 'In Progress' ? 'status-inprogress' : ''}">
                                ${b.status}
                            </span>
                        </div>

                        <div style="text-align: right;">
                            <a href="#" class="btn-view">Details</a>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty bookingList}">
                    <div class="empty-state">
                        <i class="bi bi-inbox"></i>
                        <p>No booking history found.</p>
                    </div>
                </c:if>

            </div>

        </main>
    </div>

</body>
</html>