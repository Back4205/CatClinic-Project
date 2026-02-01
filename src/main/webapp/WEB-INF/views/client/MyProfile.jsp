<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile Settings | CatClinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/myProfileStyle.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    
    <style>
        /* 1. Ẩn cái checkbox đi (nó là công tắc ngầm) */
        #toggle-pass {
            display: none;
        }

        /* 2. Mặc định ẩn Form */
        #passwordForm {
            display: none;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid rgba(255,255,255,0.1);
        }

        /* 3. Style cho nút bấm (Label đóng vai trò là nút) */
        .btn-toggle {
            background-color: #10b981;
            color: white;
            padding: 10px 20px;
            border-radius: 8px;
            font-weight: bold;
            cursor: pointer;
            display: inline-block;
            user-select: none;
        }
        .btn-toggle:hover { background-color: #059669; }

        /* 4. PHÉP MÀU: Khi checkbox được check -> Hiện form, Ẩn nút mở */
        #toggle-pass:checked ~ #passwordForm {
            display: block; /* Hiện form */
        }
        
        #toggle-pass:checked ~ .password-header .btn-toggle {
            display: none; /* Ẩn nút Change Password đi */
        }
    </style>
</head>
<body>

    <header class="topbar">
        <div class="logo"><i class="bi bi-hospital"></i> CatClinic</div>
        <div class="user-info">
            <span class="name">${user.userName}</span>
            <div class="avatar"><i class="bi bi-person-circle"></i></div>
        </div>
    </header>

    <div class="container">
        <aside class="sidebar">
            <div class="profile-card">
                <div class="avatar large"><i class="bi bi-person-circle"></i></div>
                <h3>${user.userName}</h3>
                <p>CATCLINIC PORTAL</p>
            </div>
            <nav class="menu">
                <a href="#"><i class="bi bi-grid-fill"></i> Cat List</a>
                <a href="booking-history"><i class="bi bi-calendar-event"></i> Visit History</a>
                <a href="accessprofile" class="active"><i class="bi bi-person-gear"></i> Profile & Security</a>
                <a href="#"><i class="bi bi-house"></i> Home</a>
                <a href="#" style="color: red; margin-top: 20px;"><i class="bi bi-box-arrow-right"></i> Logout</a>
            </nav>
        </aside>

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
                       <c:if test="${not empty message}">checked</c:if> >

                <div class="password-header">
                    <h3><i class="bi bi-shield-lock"></i> Password Security</h3>
                    
                    <label for="toggle-pass" class="btn-toggle">Change Password</label>
                </div>

                <div id="passwordForm">
                    <form action="accessprofile" method="POST">
                        <input type="hidden" name="action" value="changePassword">
                        
                        <input type="password" name="oldPass" placeholder="Current Password" required>
                        <input type="password" name="newPass" placeholder="New Password" required>
                        <input type="password" name="confirmPass" placeholder="Confirm Password" required>

                        <div class="actions">
                            <button type="submit" class="update-btn">Update Credentials</button>
                            
                            <label for="toggle-pass" class="cancel-btn" style="text-align:center; padding-top:14px; display:inline-block;">Cancel</label>
                        </div>
                    </form>
                </div>
                
            </div>
        </main>
    </div>

</body>
</html>