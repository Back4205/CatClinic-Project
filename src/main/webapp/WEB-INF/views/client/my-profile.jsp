<%--<%@page contentType="text/html" pageEncoding="UTF-8"%>--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile Settings | CatClinic</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/my-profile.css">
    
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>
<%@include file="header.jsp" %>


    <div class="container">
        <c:set var="activePage" value="profile" />
        <%@include file="sidebar.jsp" %>

        <main class="content">
            
            <c:if test="${not empty message}">
                <div class="alert ${messageType}" style="padding: 15px; margin-bottom: 20px; border-radius: 8px; font-weight: bold; 
                     ${messageType == 'success' ? 'background: #dcfce7; color: #166534;' : 'background: #fee2e2; color: #991b1b;'}">
                    ${message}
                </div>
            </c:if>

            <div class="profile-box">
                <div class="profile-header">
                    <h2>Profile Settings</h2>
                    <a href="edit" class="edit-btn"><i class="bi bi-pencil"></i> Edit Profile</a>
                </div>
                <div class="info-grid"> 
                    <div><label>Full Name</label><p>${user.userName}</p></div>
                    <div><label>Email</label><p>${user.email}</p></div>
                    <div><label>Phone</label><p>${user.phone}</p></div>
                    <div><label>Address</label><p>${user.address}</p></div>
                </div>
            </div>

            <div class="password-box">
                
                <input type="checkbox" id="toggle-pass"
       <c:if test="${not empty message}">checked</c:if>>


                <div class="password-header">
                    <h3><i class="bi bi-shield-lock"></i> Password Security</h3>
                    
                    <label for="toggle-pass" class="btn-toggle">Change Password</label>
                </div>

                <div id="passwordForm">
                    <form action="${pageContext.request.contextPath}/accessprofile" method="post">
    <input type="hidden" name="action" value="changePassword">

    <input type="password"
           name="oldPass"
           placeholder="Current Password"
           >

    <input type="password"
           name="newPass"
           placeholder="New Password">

    <input type="password"
           name="confirmPass"
           placeholder="Confirm Password"
>           

    <div class="actions">
        <button type="submit" class="update-btn">Update Credentials</button>
        <label for="toggle-pass"
               class="cancel-btn"
               style="text-align:center; padding-top:14px; display:inline-block;">
            Cancel
        </label>
    </div>
</form>

                </div>
                
            </div>
        </main>
    </div>
    </div> <%-- CHÈN DÒNG NÀY VÀO ?ÂY --%>
    <footer style="background: #ffffff; border-top: 1px solid #e5e7eb; padding: 25px 0; text-align: center; color: #64748b; font-size: 14px; margin-top: auto;">
        <div class="footer-content">
            &copy; 2026 CatClinic. All rights reserved.
        </div>
    </footer>

</body>
</html>
</body>
</html>