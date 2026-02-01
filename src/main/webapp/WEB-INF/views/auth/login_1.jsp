<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Cat Clinic Management</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .login-card { max-width: 400px; margin: 50px auto; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); background: white; }
        .btn-google { background-color: #dd4b39; color: white; }
        .btn-google:hover { background-color: #c23321; color: white; }
    </style>
</head>
<body>

    <div class="container">
        <div class="login-card">
            <h3 class="text-center mb-4">Sign In</h3>
            
            
            <c:if test="${not empty mess}">
                <div class="alert alert-danger" role="alert">
                    ${mess}
                </div>
            </c:if>

            <form action="login" method="post">
                
                <div class="mb-3">
                    <label for="username" class="form-label">Username or Email</label>
                    <input type="text" class="form-control" id="username" name="username" 
                           value="${username}" required placeholder="Enter username or email">
                </div>

                
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password" 
                           value="${password}" required placeholder="Enter password">
                </div>

                
                <div class="mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="remember" name="remember" ${remember}>
                    <label class="form-check-label" for="remember">Remember me</label>
                </div>

                
                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary">Sign In</button>
                </div>

                
                <div class="text-center mt-2">
                    <a href="forgot-password" class="text-decoration-none">Forgot password?</a>
                </div>

                <hr>

                
                <div class="d-grid gap-2">
                    
                    <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile&redirect_uri=http://localhost:8080/CatClinicProject/login-google&response_type=code&client_id=766431550241-en0u2iq6q8dem2psor9jj7reu7cu400t.apps.googleusercontent.com&approval_prompt=force" class="btn btn-google">
                        <i class="bi bi-google"></i> Sign in with Google
                    </a>
                </div>

                
                <div class="text-center mt-3">
                    Don't have an account? <a href="register" class="text-decoration-none">Sign Up</a>
                </div>
            </form>
        </div>
    </div>

</body>
</html>