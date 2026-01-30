<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Sign Up</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <style>
            :root {
                --primary-orange: #f97316;
                --hover-orange: #ea580c;
                --bg-light: #fff7ed;
            }

            body {
                background-color: var(--bg-light);
                background-image: radial-gradient(circle at 2px 2px, #fde6d2 1px, transparent 0);
                background-size: 40px 40px;
                font-family: 'Inter', system-ui, -apple-system, sans-serif;
                min-height: 100vh;
                padding: 2rem 0;
            }

            .register-card {
                max-width: 600px;
                margin: auto;
                padding: 2.5rem;
                border-radius: 1.5rem;
                background: white;
                box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
                border: 1px solid #fed7aa;
            }

            .clinic-icon {
                color: var(--primary-orange);
                font-size: 2.5rem;
                line-height: 1;
            }

            .required::after {
                content: " *";
                color: var(--primary-orange);
            }

            .form-label {
                font-weight: 600;
                color: #4b5563;
                font-size: 0.9rem;
            }

            .form-control {
                padding: 0.65rem 0.75rem;
                border-radius: 0.75rem;
                border: 1px solid #e5e7eb;
            }

            .form-control:focus {
                box-shadow: 0 0 0 4px rgba(249, 115, 22, 0.1);
                border-color: var(--primary-orange);
            }

            .btn-primary {
                background-color: var(--primary-orange);
                border: none;
                padding: 0.75rem;
                font-weight: 600;
                border-radius: 0.75rem;
                transition: all 0.2s;
            }

            .btn-primary:hover {
                background-color: var(--hover-orange);
                transform: translateY(-1px);
                box-shadow: 0 4px 12px rgba(249, 115, 22, 0.3);
            }

            .btn-google {
                background-color: #ffffff;
                color: #374151;
                border: 1px solid #e5e7eb;
                padding: 0.75rem;
                font-weight: 500;
                border-radius: 0.75rem;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 10px;
                transition: all 0.2s;
                text-decoration: none;
            }

            .btn-google:hover {
                background-color: #f9fafb;
                border-color: #d1d5db;
                color: #374151;
            }

            .divider {
                display: flex;
                align-items: center;
                text-align: center;
                margin: 1.5rem 0;
                color: #9ca3af;
                font-size: 0.85rem;
            }

            .divider::before, .divider::after {
                content: '';
                flex: 1;
                border-bottom: 1px solid #e5e7eb;
            }

            .divider:not(:empty)::before {
                margin-right: .75em;
            }
            .divider:not(:empty)::after {
                margin-left: .75em;
            }

            a {
                color: var(--primary-orange);
                font-weight: 600;
                text-decoration: none;
            }

            a:hover {
                color: var(--hover-orange);
                text-decoration: underline;
            }
        </style>
    </head>
    <body>

        <div class="container">
            <div class="register-card">
                <div class="text-center mb-4">
                    <div class="clinic-icon mb-2">
                        <i class="bi bi-paws-fill"></i>
                    </div>
                    <h3 class="fw-bold text-dark">Create Account</h3>
                    <p class="text-muted small">Join our clinic to care for your cats</p>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger py-2 border-0 shadow-sm mb-4" style="font-size: 0.9rem;">
                        <i class="bi bi-exclamation-circle-fill me-2"></i> ${error}
                    </div>
                </c:if>

                <form action="register" method="post">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label required">Full Name</label>
                            <input type="text" class="form-control" name="fullname" required pattern="^[a-zA-Z\s\p{L}]+$" 
                                   title="Names cannot contain numbers or special characters"
                                   value="${data.fullName}" placeholder="John Doe">
                        </div>

                        <div class="col-md-6 mb-3">
                            <label class="form-label required">Phone Number</label>
                            <input type="text" class="form-control" name="phone" required pattern="\d{10}" 
                                   title="Phone number must be exactly 10 digits"
                                   value="${data.phone}" placeholder="0901234567">
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label required">Email Address</label>
                        <input type="email" class="form-control" name="email" required 
                               value="${data.email}" placeholder="example@gmail.com">
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
                            <input type="password" class="form-control" name="password" required pattern=".{6,}" 
                                   title="Password must be at least 6 characters"
                                   placeholder="••••••••">
                        </div>

                        <div class="col-md-6 mb-3">
                            <label class="form-label required">Confirm Password</label>
                            <input type="password" class="form-control" name="confirmPassword" required 
                                   placeholder="••••••••">
                        </div>
                    </div>

                    <div class="d-grid mt-3">
                        <button type="submit" class="btn btn-primary shadow-sm">Create Account</button>
                    </div>

                    <div class="divider">OR</div>

                    <div class="d-grid">
                        <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile&redirect_uri=http://localhost:8080/CatClinicProject/login-google&response_type=code&client_id=766431550241-en0u2iq6q8dem2psor9jj7reu7cu400t.apps.googleusercontent.com&approval_prompt=force" class="btn btn-google">
                            <svg width="18" height="18" viewBox="0 0 24 24"><path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"/><path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/><path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#FBBC05"/><path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"/></svg>
                            Sign up with Google
                        </a>
                    </div>

                    <div class="text-center mt-4 pt-2 small">
                        <span class="text-muted">Already have an account?</span> <a href="login">Sign In</a>
                    </div>
                </form>
            </div>
        </div>

    </body>
</html>