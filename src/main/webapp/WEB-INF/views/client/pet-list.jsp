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
    <title>Cat List </title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/cat/pet_list.css">

</head>

<body>

<header class="topbar">
    <div class="header-inner">

        <a href="${pageContext.request.contextPath}/loadinfo" class="logo">
            <i class="bi bi-hospital"></i> CatClinic
        </a>

        <div class="user-info">
            <span class="name">${acc.userName}</span>
            <div class="avatar">
                <img src="${pageContext.request.contextPath}/image/default.jpg" alt="Profile Picture">
            </div>
        </div>

    </div>
</header>



<div class="container">
    <div class="header">
        <h2>Cat Management</h2>
        <form action="${pageContext.request.contextPath}/cats/cat-add" method="get">
            <button class="btn-add">Add New Cat +</button>
        </form>
    </div>

    <form class="filter" method="get" action="${pageContext.request.contextPath}/cats">
        <select name="gender">
            <option value="">Gender</option>
            <option value="1" ${param.gender=='1' ? 'selected' : ''}>Male</option>
            <option value="0" ${param.gender=='0' ? 'selected' : ''}>Female</option>
        </select>

        <select name="age">
            <option value="">Age</option>
            <option value="0" ${param.age=='0' ? 'selected' : ''}>&lt; 1</option>
            <option value="1" ${param.age=='1' ? 'selected' : ''}>1</option>
            <option value="2" ${param.age=='2' ? 'selected' : ''}>2</option>
            <option value="3" ${param.age=='3' ? 'selected' : ''}>3+</option>
        </select>
        <input type="text" name="breed" placeholder="Search Breed..." value="${param.breed}">
        <input type="text" name="name" placeholder="Search Cat Name..." value="${param.name}">

        <button class="btn-filter">FILTER</button>
    </form>

    <table>
        <thead>
        <tr>
            <th style="width: 60px;">ID</th>
            <th style="width: 100px;">Photo</th>
            <th>Name</th>
            <th>Age</th>
            <th>Gender</th>
            <th>Breed</th>
            <th style="width: 300px; text-align: center;">Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${catList}" var="c">
            <tr>
                <td>${c.catID}</td>
                <td>
                    <img  src="${pageContext.request.contextPath}/${c.img}"
                         class="pet-img" width="60" height="60" alt="Pet Image">
                </td>
                <td><b>${c.name}</b></td>
                <td>${c.age} years</td>
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
        </tbody>
    </table>
    <div class="table-footer">
        <div class="footer-actions">
            <a href="${pageContext.request.contextPath}/profile" class="btn-profile">
                Back To Profile
            </a>
        </div>
    <div class="pagination">
        <a class="${indexPage == 1 ? 'disabled' : ''}"
           href="${pageContext.request.contextPath}/cats?indexPage=${indexPage - 1}&name=${param.name}&age=${param.age}&gender=${param.gender}&breed=${param.breed}">
            PREV
        </a>

        <c:forEach begin="1" end="${pageSize}" var="i">
            <a class="${i == indexPage ? 'active' : ''}"
               href="${pageContext.request.contextPath}/cats?indexPage=${i}&name=${param.name}&age=${param.age}&gender=${param.gender}&breed=${param.breed}">
                    ${i}
            </a>
        </c:forEach>

        <a class="${indexPage == pageSize ? 'disabled' : ''}"
           href="${pageContext.request.contextPath}/cats?indexPage=${indexPage + 1}&name=${param.name}&age=${param.age}&gender=${param.gender}&breed=${param.breed}">
            NEXT
        </a>
    </div>
    </div>

</div>
<footer>
    <p>&copy; 2026 CatClinic. All rights reserved.</p>
</footer>

</body>


</html>