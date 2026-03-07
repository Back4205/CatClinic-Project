

<%--<header class="topbar">--%>
<%--    <div class="logo"><i class="bi bi-hospital"></i> CatClinic</div>--%>
<%--    <div class="user-info">--%>
<%--        <span class="name">${acc.fullName}</span>--%>
<%--        <div class="avatar">--%>
<%--            <img src="${pageContext.request.contextPath}/image/default.jpg" alt="Profile Picture">--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</header>--%>


<header class="topbar">
    <div class="logo">
        <c:choose>
        <c:when test="${sessionScope.acc != null && sessionScope.acc.roleID == 5}">
            <a href="${pageContext.request.contextPath}/loadinfo" style="text-decoration: none; color: inherit; display: flex; align-items: center; gap: 10px;">
                <i class="bi bi-hospital"></i> CatClinic
            </a>
        </c:when>
            <c:otherwise>
                <a  style="text-decoration: none; color: inherit; display: flex; align-items: center; gap: 10px;">
                    <i class="bi bi-hospital"></i> CatClinic
                </a>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="user-info">
        <span class="name">Wellcome ${acc.fullName}</span>
        <div class="avatar">
            <img src="${pageContext.request.contextPath}/image/default.jpg" alt="Profile Picture">
        </div>
    </div>
</header>