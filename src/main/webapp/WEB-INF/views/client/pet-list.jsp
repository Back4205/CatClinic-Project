<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 1/23/2026
  Time: 6:19 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cat List | CatClinic</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/sidebar.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cat/pet_list.css">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>

<body>

<%@include file="header.jsp" %>

<div class="container">

    <%-- ACTIVE MENU --%>
    <c:set var="activePage" value="cats" />
    <%@include file="sidebar.jsp" %>

    <main class="content">

        <div class="page-header">
            <div class="page-title">
                <h2>CAT MANAGEMENT</h2>
                <p>MANAGE ALL REGISTERED CATS IN SYSTEM.</p>
            </div>

            <div class="top-action">
                <form action="${pageContext.request.contextPath}/cats/cat-add" method="get" style="margin: 0;">
                    <button class="btn-add">
                        <i class="bi bi-plus-circle"></i> ADD NEW CAT
                    </button>
                </form>
            </div>
        </div>

        <form class="filter-bar" method="get" action="${pageContext.request.contextPath}/cats">
            <select name="gender">
                <option value="">All Genders</option>
                <option value="1" ${param.gender=='1' ? 'selected' : ''}>Male</option>
                <option value="0" ${param.gender=='0' ? 'selected' : ''}>Female</option>
            </select>

            <select name="age">
                <option value="">All Ages</option>
                <option value="0" ${param.age=='0' ? 'selected' : ''}>&lt; 1 Year</option>
                <option value="1" ${param.age=='1' ? 'selected' : ''}>1 Year</option>
                <option value="2" ${param.age=='2' ? 'selected' : ''}>2 Years</option>
                <option value="3" ${param.age=='3' ? 'selected' : ''}>3+ Years</option>
            </select>

            <input type="text" name="breed" placeholder="Search Breed..." value="${param.breed}">
            <input type="text" name="name" placeholder="Search cat name..." value="${param.name}">

            <button class="btn-filter">
                FILTER
            </button>
        </form>

        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th style="width: 50px;">ID</th>
                    <th style="width: 80px;">Photo</th>
                    <th>Name</th>
                    <th>Age</th>
                    <th>Gender</th>
                    <th>Breed</th>
                    <th style="text-align:center;">Actions</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach items="${catList}" var="c">
                    <tr>
                        <td><strong>${c.catID}</strong></td>

                        <td>
                            <img src="${pageContext.request.contextPath}/${c.img}"
                                 class="pet-img"
                                 alt="Pet">
                        </td>

                        <td><strong style="font-size: 15px;">${c.name}</strong></td>

                        <td>
                            <c:choose>
                                <c:when test="${c.age == 0}">
                                    < 1 Year
                                </c:when>
                                <c:otherwise>
                                    ${c.age} Years
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>
                            <span class="badge ${c.gender == 1 ? 'male' : 'female'}">
                                    ${c.gender == 1 ? 'MALE' : 'FEMALE'}
                            </span>
                        </td>

                        <td>${c.breed}</td>

                        <td>
                            <div class="action-group">
                                <form action="${pageContext.request.contextPath}/cats/cat-update" method="get">
                                    <input type="hidden" name="catId" value="${c.catID}">
                                    <button class="btn btn-edit">EDIT</button>
                                </form>

                                <form action="${pageContext.request.contextPath}/cats/cat-delete"
                                      method="post"
                                      onsubmit="return confirm('Are you sure you want to delete this cat?')">
                                    <input type="hidden" name="catId" value="${c.catID}">
                                    <button class="btn btn-delete">DELETE</button>
                                </form>

                                <form action="${pageContext.request.contextPath}/cats/medical-history" method="get">
                                    <input type="hidden" name="catId" value="${c.catID}">
                                    <button class="btn-history">MEDICAL HISTORY</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty catList}">
                    <tr>
                        <td colspan="7" class="empty-state">
                            <i class="bi bi-inbox" style="font-size: 24px; display: block; margin-bottom: 10px;"></i>
                            No cats found.
                        </td>
                    </tr>
                </c:if>

                </tbody>
            </table>
        </div>

        <div class="pagination">
            <c:choose>
                <c:when test="${indexPage == 1 || empty indexPage}">
                    <a class="disabled">PREV</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/cats?indexPage=${indexPage - 1}&name=${param.name}&age=${param.age}&gender=${param.gender}&breed=${param.breed}">
                        PREV
                    </a>
                </c:otherwise>
            </c:choose>

            <c:forEach begin="1" end="${pageSize}" var="i">
                <a class="${i == indexPage ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/cats?indexPage=${i}&name=${param.name}&age=${param.age}&gender=${param.gender}&breed=${param.breed}">
                        ${i}
                </a>
            </c:forEach>

            <c:choose>
                <c:when test="${indexPage == pageSize || empty pageSize || pageSize == 0}">
                    <a class="disabled">NEXT</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/cats?indexPage=${indexPage + 1}&name=${param.name}&age=${param.age}&gender=${param.gender}&breed=${param.breed}">
                        NEXT
                    </a>
                </c:otherwise>
            </c:choose>
        </div>

    </main>
</div>

<footer style="background: #ffffff; border-top: 1px solid #e5e7eb; padding: 25px 0; text-align: center; color: #64748b; font-size: 14px; margin-top: auto;">
    <div class="footer-content">
        &copy; 2026 CatClinic. All rights reserved.
    </div>
</footer>

</body>
</html>