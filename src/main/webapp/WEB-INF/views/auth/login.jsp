<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Sign In</title>

        <link href="${pageContext.request.contextPath}/css/login-style.css" rel="stylesheet">
    </head>
    <body>

        <div class="auth-card login">
            <div class="header">
                <h3>Welcome Back</h3>
                <p>Please enter your details to sign in</p>
            </div>


            <c:if test="${not empty mess}">
                <div class="alert-error">${mess}</div>
            </c:if>

            <form action="login" method="post">
                <div class="form-group">
                    <label class="form-label">Username</label>
                    <input type="text" class="form-control" name="username" value="${username}" required>
                </div>

                <div class="form-group">
                    <label class="form-label">Password</label>
                    <input type="password" class="form-control" name="password" value="${password}" required>
                </div>

                <div class="options">
                    <label style="cursor: pointer;">
                        <input type="checkbox" name="remember" ${remember}> Remember me
                    </label>
                    <a href="forgot-password">Forgot password?</a>
                </div>

                <button type="submit" class="btn btn-primary">Sign In</button>

                <div class="divider">OR</div>

                <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile&redirect_uri=http://localhost:8080/CatClinicProject/login-google&response_type=code&client_id=766431550241-en0u2iq6q8dem2psor9jj7reu7cu400t.apps.googleusercontent.com&approval_prompt=force" class="btn btn-google">
                    <svg width="18" height="18" viewBox="0 0 24 24"><path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"/><path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/><path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#FBBC05"/><path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"/></svg>
                    Sign in with Google
                </a>

                <div class="text-center">
                    Don't have an account? <a href="register">Sign up</a>
                </div>
            </form>
        </div>

    </body>
</html>