<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>VetCare Pro - Assigned Cases</title>

        <link rel="stylesheet" href="css/DashboardVeteStyle.css">
        <link href="css/sidebar.css" rel="stylesheet">
        <link href="css/headerVeteStyle.css" rel="stylesheet">
        <link href="css/AssignCaseStyle.css" rel="stylesheet" type="text/css"/>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    </head>

    <body>

        <div class="layout">

            <!-- SIDEBAR -->
            <jsp:include page="sidebar.jsp"/>

            <!-- MAIN -->
            <main class="main">

                <!-- HEADER -->
                <jsp:include page="header.jsp"/>

                <div class="dashboard-content">

                    <!-- ===== TITLE ===== -->
                    <div class="page-title">
                        <h2>Assigned Cases</h2>
                    </div>

                    <!-- ===== FILTER ===== -->
                    <div class="case-header">
                        <div class="search-container">
                            <i class="fa fa-search"></i>
                            <input type="text" placeholder="Search pet, owner, or ID...">
                        </div>
                        <div class="header-actions">
                            <select class="status-dropdown">
                                <option>All Status</option>
                            </select>
                            <button class="btn-plus"><i class="fa fa-plus"></i></button>
                        </div>
                    </div>

                    <div class="table-container">
                        <table class="vet-table">
                            <thead>
                                <tr>
                                    <th>CASE ID</th>
                                    <th>PET & SPECIES</th>
                                    <th>OWNER INFO</th>
                                    <th>PRELIMINARY DIAGNOSIS</th>
                                    <th>DATE ASSIGNED</th>
                                    <th>STATUS</th>
                                    <th style="text-align: right;">ACTIONS</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td class="case-id">CASE-001</td>
                                    <td>
                                        <div class="pet-cell">
                                            <div class="avatar">B</div>
                                            <div class="info">
                                                <span class="name">Buddy</span>
                                                <span class="sub">Dog • Golden Retriever</span>
                                            </div>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="info">
                                            <span class="name">Nguyen Van B</span>
                                            <span class="sub">0901234567</span>
                                        </div>
                                    </td>
                                    <td>Skin Allergy</td>
                                    <td>2024-05-20</td>
                                    <td><span class="badge assigned">ASSIGNED</span></td>
                                    <td class="actions-cell">
                                        <button class="btn-emr"><i class="fa-regular fa-file-lines"></i> EMR</button>
                                        <i class="fa-solid fa-flask icon-action"></i>
                                        <i class="fa-solid fa-table-cells-large icon-action"></i>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                </div>

                <!-- ===== PAGINATION ===== -->
                <div class="pagination">
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a href="AssignCaseController?page=${i}&status=${statusFilter}"
                           class="${i == currentPage ? 'active-page' : ''}">
                            ${i}
                        </a>
                    </c:forEach>
                </div>

            </main>
        </div>

    </body>
</html>