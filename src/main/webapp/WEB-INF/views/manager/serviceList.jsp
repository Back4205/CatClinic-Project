<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Service List</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/service-list.css">
    </head>
    <body>

        <div class="container">

            <!-- HEADER -->
            <div class="header">

                <a href="${pageContext.request.contextPath}/ViewCategoryList"
                   class="btn-back">
                    Back to Category List
                </a>

                <h2 class="title">Service List</h2>

                <a href="${pageContext.request.contextPath}/CreateService?categoryID=${categoryID}"
                   class="btn-create">
                    + Create New Service
                </a>
            </div>


            <form action="ViewServiceList" method="GET">

                <input type="hidden" name="id" value="${categoryID}">

                <div class="filter-card">

                    <input type="text"
                           name="search"
                           placeholder="Search service name..."
                           value="${currentSearch}">

                    <button type="submit" name="status" value="ALL"
                            class="${currentStatus == 'ALL' ? 'active' : ''}">
                        All
                    </button>

                    <button type="submit" name="status" value="Active"
                            class="${currentStatus == 'Active' ? 'active' : ''}">
                        Active
                    </button>

                    <button type="submit" name="status" value="Inactive"
                            class="${currentStatus == 'Inactive' ? 'active' : ''}">
                        Inactive
                    </button>

                </div>

            </form>
            <!-- TABLE -->
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Price (VND)</th>
                            <th>Description</th>
                            <th>Time (Min)</th>
                            <th>Status</th>
                            <th>Image</th>
                            <th>Action</th>
                        </tr>
                    </thead>

                    <tbody id="serviceTable">
                        <c:forEach var="s" items="${serviceList}">
                            <tr>
                                <td>${s.serviceID}</td>
                                <td>${s.nameService}</td>
                                <td>${s.price}</td>
                                <td>${s.description}</td>
                                <td>${s.timeService}</td>

                                <td>
                                    <span class="${s.isActive ? 'status-active' : 'status-inactive'}">
                                        ${s.isActive ? 'Active' : 'Inactive'}
                                    </span>
                                </td>

                                <td>
                                    <img src="${s.imgUrl}" class="service-img">
                                </td>

                                <td class="action-buttons">

                                    <a href="${pageContext.request.contextPath}/EditService?serviceID=${s.serviceID}"
                                       class="btn-edit">
                                        Edit
                                    </a>

                                    <form action="${pageContext.request.contextPath}/HideService"
                                          method="post"
                                          onsubmit="return confirmAction(this)">
                                        <input type="hidden" name="serviceID" value="${s.serviceID}">
                                        <input type="hidden" name="categoryID" value="${s.categoryID}">
                                        <input type="hidden" name="action"
                                               value="${s.isActive ? 'hide' : 'show'}">

                                        <button type="submit"
                                                class="${s.isActive ? 'btn-hide' : 'btn-show'}">
                                            ${s.isActive ? 'Hide' : 'Show'}
                                        </button>
                                    </form>

                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <c:if test="${totalPage > 1}">
                    <div class="pagination">

                        <c:if test="${currentPage > 1}">
                            <a href="ViewServiceList?id=${categoryID}&page=${currentPage-1}&search=${currentSearch}&status=${currentStatus}">
                                Prev
                            </a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPage}" var="i">
                            <a href="ViewServiceList?id=${categoryID}&page=${i}&search=${currentSearch}&status=${currentStatus}"
                               class="${i == currentPage ? 'active' : ''}">
                                ${i}
                            </a>
                        </c:forEach>

                        <c:if test="${currentPage < totalPage}">
                            <a href="ViewServiceList?id=${categoryID}&page=${currentPage+1}&search=${currentSearch}&status=${currentStatus}">
                                Next
                            </a>
                        </c:if>

                    </div>
                </c:if>
            </div>

        </div>

        <script>
            function confirmAction(form) {
                const action = form.querySelector("input[name='action']").value;
                return confirm(
                        action === "hide"
                        ? "Are you sure you want to hide this service?"
                        : "Are you sure you want to show this service?"
                        );
            }

            searchInput.addEventListener("keyup", applyFilter);
            statusFilter.addEventListener("change", applyFilter);
        </script>

    </body>
</html>
