<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty listService}">
    <p class="empty-text">No services found.</p>
</c:if>

<c:if test="${not empty listService}">
    <table class="service-table">
        <thead>
        <tr>
            <th class="col-id">ID</th>
            <th class="col-name">Name</th>
            <th class="col-price">Price</th>
            <th class="col-time">Time</th>
            <th class="col-status">Status</th>
            <th class="col-action">Manager</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="s" items="${listService}">
            <tr>
                <td class="col-id">${s.serviceID}</td>
                <td class="col-name">${s.nameService}</td>
                <td class="col-price">${s.price}</td>
                <td class="col-time">${s.timeService}</td>

                <td class="col-status">
                    <span class="${s.isActive ? 'status-active' : 'status-inactive'}">
                        ${s.isActive ? 'Active' : 'Inactive'}
                    </span>
                </td>

                <td class="col-action">
                    <a class="link" href="${pageContext.request.contextPath}/ViewServiceDetail?id=${s.serviceID}">View</a>
                    <a class="link" href="${pageContext.request.contextPath}/EditService?id=${s.serviceID}">Edit</a>

                    <form action="${pageContext.request.contextPath}/HideService"
                          method="post" class="inline-form">
                        <input type="hidden" name="id" value="${s.serviceID}">
                        <input type="hidden" name="action"
                               value="${s.isActive ? 'hide' : 'show'}">
                        <button class="btn-outline" type="submit">
                            ${s.isActive ? 'Hide' : 'Show'}
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <!-- PAGINATION -->
    <div class="pagination">
        <c:forEach begin="1" end="${totalPage}" var="i">
            <button onclick="goPage(${i})"
                    class="${i == page ? 'active' : ''}">
                ${i}
            </button>
        </c:forEach>
    </div>
</c:if>
