<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Cat List</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f6f8fb;
            padding: 30px;
            margin-top: 50px;
        }

        .container {
            background: #ffffff;
            border-radius: 12px;
            padding: 25px;
        }

        /* ===== HEADER ===== */
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
        }

        .btn-add {
            background: #22a06b;
            color: white;
            border: none;
            padding: 10px 16px;
            border-radius: 8px;
            cursor: pointer;
        }

        /* ===== FILTER ===== */
        .filter {
            display: grid;
            grid-template-columns: 1fr 1fr 1.5fr 1.5fr auto;
            gap: 15px;
            margin-bottom: 25px;
        }

        .filter input,
        .filter select {
            padding: 10px;
            border-radius: 8px;
            border: 1px solid #ddd;
        }

        .btn-filter {
            background: #22a06b;
            color: white;
            border: none;
            border-radius: 8px;
            padding: 10px 16px;
            cursor: pointer;
        }

        /* ===== TABLE FIX CÂN CỘT ===== */
        table {
            width: 100%;
            border-collapse: collapse;
            table-layout: fixed; /* ⭐ QUAN TRỌNG */
        }

        th,
        td {
            text-align: left;
            vertical-align: middle;
            padding: 14px 16px; /* ⭐ GIỐNG NHAU → KHÔNG LỆCH */
        }

        th {
            font-size: 12px;
            color: #888;
            border-bottom: 1px solid #eee;
        }

        td {
            border-top: 1px solid #eee;
        }

        /* ===== FIX ĐỘ RỘNG CỘT (KHÔNG ĐỔI LAYOUT) ===== */
        th:nth-child(1), td:nth-child(1) { width: 60px; }   /* ID */
        th:nth-child(2), td:nth-child(2) { width: 200px; }  /* NAME */
        th:nth-child(3), td:nth-child(3) { width: 80px; }   /* AGE */
        th:nth-child(4), td:nth-child(4) { width: 140px; }  /* GENDER */
        th:nth-child(5), td:nth-child(5) { width: auto; }   /* BREED */
        th:nth-child(6), td:nth-child(6) { width: 220px; }  /* ACTION */

        /* ===== BADGE (GIỮ NGUYÊN) ===== */
        .badge {
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
        }

        .male {
            background: #e6f4ff;
            color: #1c7ed6;
        }

        .female {
            background: #ffe4ec;
            color: #d63384;
        }

        /* ===== ACTION ===== */
        .action-group {
            display: flex;
            gap: 8px;
            align-items: center;
            flex-wrap: nowrap;
        }

        .btn {
            padding: 6px 12px;
            border-radius: 6px;
            border: none;
            font-size: 12px;
            cursor: pointer;
            white-space: nowrap;
        }

        .btn-edit {
            background: #22a06b;
            color: white;
        }

        .btn-delete {
            background: #f1f3f5;
        }

        .btn-history {
            background: none;
            color: #22a06b;
            font-size: 11px;
            border: none;
            cursor: pointer;
        }

        /* ===== PAGINATION (GIỮ NGUYÊN CÁCH CŨ) ===== */
        .pagination {
            display: flex;
            justify-content: flex-end;
            gap: 6px;
            padding-top: 15px;
            border-top: 1px solid #eee;
        }

        .pagination a {
            padding: 6px 12px;
            border-radius: 6px;
            border: 1px solid #ddd;
            text-decoration: none;
            color: #333;
            font-size: 12px;
        }

        .pagination a.active {
            background: #22a06b;
            color: white;
        }

        .pagination a.disabled {
            pointer-events: none;
            opacity: 0.4;
        }
    </style>
</head>

<body>
<div class="container">

    <!-- HEADER -->
    <div class="header">
        <h2>Cat List</h2>
        <form action="${pageContext.request.contextPath}/cats/cat-add" method="get">
            <button class="btn-add">Add New Cat +</button>
        </form>
    </div>

    <!-- FILTER -->
    <form class="filter" method="get" action="${pageContext.request.contextPath}/cats">
        <select name="gender">
            <option value="">Gender</option>
            <option value="1" ${param.gender=='1' ? 'selected' : ''}>Male</option>
            <option value="0" ${param.gender=='0' ? 'selected' : ''}>Female</option>
        </select>
        <select name="age">
            <option value="">Age</option>
            <option value="1" ${param.age=='1' ? 'selected' : ''}>1</option>
            <option value="2" ${param.age=='2' ? 'selected' : ''}>2</option>
            <option value="3" ${param.age=='3' ? 'selected' : ''}>3+</option>
        </select>
        <input type="text" name="breed" placeholder="Breed" value="${param.breed}">
        <input type="text" name="name" placeholder="Cat name" value="${param.name}">

        <button class="btn-filter">FILTER</button>
    </form>

    <!-- TABLE -->
    <table>
        <tr>
            <th>ID</th>
            <th>NAME</th>
            <th>AGE</th>
            <th>GENDER</th>
            <th>BREED</th>
            <th>ACTIONS</th>
        </tr>

        <c:forEach items="${catList}" var="c">
            <tr>
                <td>${c.catID}</td>
                <td><b>${c.name}</b></td>
                <td>${c.age}</td>
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


                        <c:if test="${c.catID != 1 && !c.hasBooking}">
                            <form action="${pageContext.request.contextPath}/cats/cat-delete"
                                  method="post"
                                  onsubmit="return confirm('Delete this cat?')">
                                <input type="hidden" name="catId" value="${c.catID}">
                                <button class="btn btn-delete">DELETE</button>
                            </form>
                        </c:if>

                        <c:if test="${c.hasBooking}">
                            <form action="${pageContext.request.contextPath}/cats/medical-history" method="get">
                                <input type="hidden" name="catId" value="${c.catID}">
                                <button class="btn-history">MEDICAL HISTORY</button>
                            </form>
                        </c:if>

                    </div>
                </td>
            </tr>
        </c:forEach>
    </table>

    <!-- PAGINATION -->
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
</body>
</html>