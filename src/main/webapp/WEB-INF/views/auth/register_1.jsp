<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up - Cat Clinic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .register-card { max-width: 600px; margin: 30px auto; padding: 30px; border-radius: 10px; background: white; box-shadow: 0 0 15px rgba(0,0,0,0.1); }
        .required::after { content: " *"; color: red; }
    </style>
</head>
<body>

    <div class="container">
        <div class="register-card">
            <h3 class="text-center mb-4">Create Account</h3>
            
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="register" method="post">
                <div class="row">
                    
                    <div class="col-md-6 mb-3">
                        <label class="form-label required">Full Name</label>
                        <input type="text" class="form-control" name="fullname" required 
                               value="${data.fullName}" placeholder="Enter full name">
                    </div>
                    
                    
                    <div class="col-md-6 mb-3">
                        <label class="form-label required">Phone Number</label>
                        <input type="text" class="form-control" name="phone" required 
                               value="${data.phone}" placeholder="Enter phone">
                    </div>
                </div>

                
                <div class="mb-3">
                    <label class="form-label required">Email Address</label>
                    <input type="email" class="form-control" name="email" required 
                           value="${data.email}" placeholder="Enter email address">
                </div>

                
                <div class="mb-3">
                    <label class="form-label">Address</label>
                    <input type="text" class="form-control" name="address" 
                           value="${data.address}" placeholder="Enter residential address">
                </div>

                <div class="mb-3">
                    <label class="form-label required">Username</label>
                    <input type="text" class="form-control" name="username" required 
                           value="${data.username}" placeholder="Choose a username">
                </div>

                <div class="row">
                    
                    <div class="col-md-6 mb-3">
                        <label class="form-label required">Password</label>
                        <input type="password" class="form-control" name="password" required 
                               placeholder="Enter password">
                    </div>

                    
                    <div class="col-md-6 mb-3">
                        <label class="form-label required">Confirm Password</label>
                        <input type="password" class="form-control" name="confirmPassword" required 
                               placeholder="Re-enter password">
                    </div>
                </div>

                <div class="d-grid gap-2 mt-3">
                    <button type="submit" class="btn btn-primary btn-lg">Create Account</button>
                </div>

                <hr>
                <div class="d-grid gap-2">
                     
                    <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile&redirect_uri=http://localhost:8080/CatClinicProject/login-google&response_type=code&client_id=766431550241-en0u2iq6q8dem2psor9jj7reu7cu400t.apps.googleusercontent.com&approval_prompt=force" class="btn btn-danger">
                        <i class="bi bi-google"></i> Sign up with Google
                    </a>
                </div>

                <div class="text-center mt-3">
                    Already have an account? <a href="login" class="text-decoration-none">Sign In</a>
                </div>
            </form>
        </div>
    </div>

</body>
</html>