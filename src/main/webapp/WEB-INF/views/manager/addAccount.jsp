<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add New Account</title>
    <link href="css/addAccountStyle.css" rel="stylesheet" type="text/css"/>
</head>
<body>

<a href="account" class="btn-back"> Back to User List</a>


<div class="form-wrapper">
    <h2 class="title">ADD NEW ACCOUNT</h2>

    <c:if test="${not empty success}">
        <div class="msg success">${success}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="msg error">${error}</div>
    </c:if>

    <form action="addAccount" method="post">

        <div class="row">
            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" value="${param.username}" required>
            </div>

            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" required>
            </div>
        </div>

        <div class="form-group">
            <label>Full Name</label>
            <input type="text" name="fullName" value="${param.fullName}">
        </div>

        <div class="row">
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" value="${param.email}">
            </div>

            <div class="form-group">
                <label>Phone</label>
                <input type="text" name="phone" value="${param.phone}">
            </div>
        </div>

        <div class="row">
            <div class="form-group">
                <label>Role</label>
                <select name="role">
                    <option ${param.role=='Receptionist'?'selected':''}>Receptionist</option>
                    <option ${param.role=='Staff'?'selected':''}>Staff</option>
                    <option ${param.role=='Admin'?'selected':''}>Admin</option>
                    <option ${param.role=='Veterinarian'?'selected':''}>Veterinarian</option>
                    <option ${param.role=='Customer'?'selected':''}>Customer</option>
                </select>
            </div>

            <div class="form-group">
                <label>Gender</label>
                <select name="gender">
                    <option ${param.gender=='Female'?'selected':''}>Female</option>
                    <option ${param.gender=='Male'?'selected':''}>Male</option>
                </select>
            </div>
        </div>

        <button type="submit" class="btn-submit">
            CONFIRM ACCOUNT CREATION
        </button>

        <button type="button" class="btn-cancel" onclick="history.back()">
            CANCEL
        </button>

    </form>
</div>

</body>
</html>
