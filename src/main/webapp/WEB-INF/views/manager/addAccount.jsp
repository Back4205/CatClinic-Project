
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

            <c:if test="${not empty success_addaccount}">
                <div class="msg success_addaccount">${success_addaccount}</div>
            </c:if>

            <c:if test="${not empty error_addaccount}">
                <div class="msg error_addaccount">${error_addaccount}</div>
            </c:if>

            <form action="addAccount" method="post" id="userForm">

                <div class="row">
                    <div class="form-group">
                        <label>Username</label>
                        <input type="text" name="username" id="username" value="${param.username}" required>
                        <small class="error" id="usernameError"></small>
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="password" id="password" required>
                        <small class="error" id="passwordError"></small>
                    </div>
                </div>
                <div class="form-group">
                    <label>Full Name</label>
                    <input type="text" name="fullName" id="fullName" value="${param.fullName}">
                    <small class="error" id="fullNameError"></small>
                </div>

                <div class="row">
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" name="email" id="email" value="${param.email}">
                        <small class="error" id="emailError"></small>
                    </div>

                    <div class="form-group">
                        <label>Phone</label>
                        <input type="text" name="phone" id="phone" value="${param.phone}">
                        <small class="error" id="phoneError"></small>
                    </div>
                </div>

                <div class="row">
                    <div class="form-group">
                        <label>Role</label>
                        <select name="role">
                            <option ${param.role=='Receptionist'?'selected':''}>Receptionist</option>
                            <option ${param.role=='Staff'?'selected':''}>Staff</option>
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

                <button type="button" class="btn-cancel"
                        onclick="location.href = 'account'">
                    CANCEL
                </button>
            </form>
        </div>
        <script>
            const usernameInput = document.getElementById("username");
            const passwordInput = document.getElementById("password");
            const emailInput = document.getElementById("email");
            const phoneInput = document.getElementById("phone");

            const usernameError = document.getElementById("usernameError");
            const passwordError = document.getElementById("passwordError");
            const emailError = document.getElementById("emailError");
            const phoneError = document.getElementById("phoneError");
            usernameInput.addEventListener("input", () => {
                if (usernameInput.value.trim().length < 3) {
                    usernameError.innerText = "Username must be at least 3 characters";
                } else {
                    usernameError.innerText = "";
                }
            });
            passwordInput.addEventListener("input", () => {
                const val = passwordInput.value.trim();
                // regex: ít nh?t 1 ch? cái và 1 s?
                const regex = /^(?=.*[A-Za-z])(?=.*\d).{6,}$/;

                if (!regex.test(val)) {
                    passwordError.innerText = "Password must be at least 6 characters \n Contain both letters and numbers";
                } else {
                    passwordError.innerText = "";
                }
            });

            emailInput.addEventListener("input", () => {
                const val = emailInput.value.trim();
                if (val) {
                    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    emailError.innerText = regex.test(val) ? "" : "Invalid email format";
                } else {
                    emailError.innerText = "";
                }
            });

            phoneInput.addEventListener("input", () => {
                const val = phoneInput.value.trim();
                const regex = /^\d{10}$/;
                phoneError.innerText = val && !regex.test(val) ? "Phone must be 10 digits" : "";
            });

            document.getElementById("userForm").addEventListener("submit", (e) => {
                if (usernameError.innerText || passwordError.innerText || emailError.innerText || phoneError.innerText) {
                    e.preventDefault();
                    alert("Please fix the errors before submitting!");
                }
            });
        </script>
    </body>
</html>
