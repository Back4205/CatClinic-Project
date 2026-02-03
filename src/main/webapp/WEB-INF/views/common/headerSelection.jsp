<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
    <c:when test="${sessionScope.acc == null}">
        <jsp:include page="/WEB-INF/views/common/header_1.jsp"/>
    </c:when>
    <c:otherwise>
        <jsp:include page="/WEB-INF/views/common/header_2.jsp"/>
    </c:otherwise>
</c:choose>