<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Create Account | Cat Clinic</title>

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

        <link href="${pageContext.request.contextPath}/css/register-style.css" rel="stylesheet">
    </head>
    <body>

        <div class="register-card">
            <div class="header">
                <div class="header-icon"><i class="bi bi-paws-fill"></i></div>
                <h3>Create Account</h3>
                <p>Join our clinic to care for your cats</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert-error">
                    <i class="bi bi-exclamation-circle-fill"></i> ${error}
                </div>
            </c:if>

            <form action="register" method="post">

                <div class="row-flex">
                    <div class="col-half form-group">
                        <label class="form-label required">Full Name</label>

                        <input type="text" class="form-control" name="fullname" required 
                               pattern="^[a-zA-Z\s\p{L}]+$" title="Names cannot contain numbers"
                               value="${data.fullName}" placeholder="John Doe">
                    </div>
                    <div class="col-half form-group">
                        <label class="form-label required">Phone Number</label>
                        <input type="text" class="form-control" name="phone" required 
                               pattern="\d{10}" title="Must be 10 digits"
                               value="${data.phone}" placeholder="0901234567">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label required">Email Address</label>
                    <input type="email" class="form-control" name="email" required
                           value="${data.email}" placeholder="example@gmail.com">
                </div>

                <div class="form-group">
                    <label class="form-label">Address</label>
                    <input type="text" class="form-control" name="address"
                           value="${data.address}" placeholder="Enter residential address">
                </div>

                <div class="form-group">
                    <label class="form-label required">Username</label>
                    <input type="text" class="form-control" name="username" required
                           value="${data.username}" placeholder="Choose a username">
                </div>

                <div class="row-flex">
                    <div class="col-half form-group">
                        <label class="form-label required">Password</label>
                        <input type="password" class="form-control" name="password" required 
                               pattern=".{6,}" title="Min 6 characters" placeholder="••••••••">
                    </div>
                    <div class="col-half form-group">
                        <label class="form-label required">Confirm Password</label>
                        <input type="password" class="form-control" name="confirmPassword" required
                               placeholder="••••••••">
                    </div>
                </div>

                <button type="submit" class="btn btn-primary">Create Account</button>

                <div class="divider">OR</div>

                <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile&redirect_uri=http://localhost:8080/CatClinicProject/login-google&response_type=code&client_id=766431550241-en0u2iq6q8dem2psor9jj7reu7cu400t.apps.googleusercontent.com&approval_prompt=force" class="btn btn-google">
                    <svg width="18" height="18" viewBox="0 0 24 24"><path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"/><path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/><path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#FBBC05"/><path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"/></svg>
                    Sign up with Google
                </a>

                <div class="text-center">
                    Already have an account? <a href="login">Sign In</a>
                </div>
            </form>
        </div>

    </body>
</html>