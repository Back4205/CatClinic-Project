<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>VetCare Pro - Dashboard</title>
        <link rel="stylesheet" href="css/DashboardVeteStyle.css">
        <link href="css/sidebar.css" rel="stylesheet" type="text/css"/>
        <link href="css/headerVeteStyle.css" rel="stylesheet" type="text/css"/>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    </head>
    <body>

        <div class="layout">
            <jsp:include page="sidebar.jsp"/>
            <!-- MAIN -->

            <main class="main">

                <!-- HEADER -->

                <jsp:include page="header.jsp"/>

                <div class="dashboard-content">

                    <!-- ===== STATUS CARDS ===== -->
                    <div class="status-cards">
                        <div class="status-card confirmed">
                            <h3>${confirmedCount}</h3>
                            <p>Confirmed</p>
                        </div>

                        <div class="status-card treatment">
                            <h3>${inTreatmentCount}</h3>
                            <p>In Treatment</p>
                        </div>

                        <div class="status-card completed">
                            <h3>${completedCount}</h3>
                            <p>Completed</p>
                        </div>
                    </div>


                    <!-- ===== FILTER ===== -->
                    <div class="filter-bar">
                        <form method="get" action="DashboardController">
                            <label>From:</label>
                            <input type="date" name="dateFrom" value="${dateFrom}">

                            <label>To:</label>
                            <input type="date" name="dateTo" value="${dateTo}">

                            <button type="submit">Filter</button>
                        </form>
                    </div>


                    <!-- ===== MAIN GRID ===== -->
                    <div class="main-grid">

                        <!-- LEFT -->
                        <div class="case-table-card">
                            <h3>Assigned Cases</h3>

                            <table>
                                <thead>
                                    <tr>
                                        <th>Case ID</th>
                                        <th>Pet Name</th>
                                        <th>Owner</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>

                                <tbody>
                                    <c:choose>
                                        <c:when test="${not empty assignList}">
                                            <c:forEach var="c" items="${assignList}">
                                                <tr>
                                                    <td>${c.bookingID}</td>
                                                    <td>${c.catName}</td>
                                                    <td>${c.fullName}</td>
                                                    <td>${c.status}</td>
                                                    <td><a href=""><i class="fa-solid fa-barcode"></i></a>
                                                        <a href=""><i class="fa-solid fa-book-open"></i></a>
                                                    
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="4" style="text-align:center">
                                                    No cases found
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>

                        <!-- RIGHT -->
                        <div class="case-detail-card">
                            <h3>Case Detail Panel</h3>
                            <p>Select a case from the table to view details</p>
                        </div>

                    </div>
                </div>

                <!-- PAGINATION -->
                <div class="pagination">
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a href="dashboard?page=${i}&dateFrom=${dateFrom}&dateTo=${dateTo}"
                           class="${i == currentPage ? 'active-page' : ''}">
                            ${i}
                        </a>
                    </c:forEach>
                </div>
        </div>

        <!-- DETAIL PANEL -->

    </div>

</main>
</div>

</body>
</html>