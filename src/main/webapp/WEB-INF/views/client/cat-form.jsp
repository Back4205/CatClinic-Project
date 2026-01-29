<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Cat Form</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f6f8fb;
            margin: 0;
            padding: 0;
        }

        .container {
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .form-box {
            background: #fff;
            width: 420px;
            padding: 28px 32px;
            border-radius: 12px;
            box-shadow: 0 8px 24px rgba(0,0,0,0.08);
        }

        h2 {
            margin-bottom: 24px;
            font-size: 18px;
        }

        label {
            font-size: 13px;
            font-weight: bold;
            color: #444;
            margin-bottom: 6px;
            display: block;
        }

        input, select {
            width: 100%;
            padding: 9px 10px;
            border-radius: 8px;
            border: 1px solid #ddd;
            font-size: 13px;
        }

        input:focus, select:focus {
            outline: none;
            border-color: #22a06b;
        }

        .field {
            margin-bottom: 16px;
        }

        .row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 16px;
        }

        .hint {
            font-size: 11px;
            color: #888;
            margin-top: 4px;
        }

        .buttons {
            margin-top: 22px;
            display: flex;
            justify-content: center;
            gap: 12px;
        }

        .btn {
            padding: 8px 18px;
            border-radius: 8px;
            border: none;
            font-size: 13px;
            cursor: pointer;
        }

        .btn-add,
        .btn-update {
            background: #22a06b;
            color: white;
        }

        .btn-cancel {
            background: #f1f3f5;
        }

        .message {
            text-align: center;
            font-size: 12px;
            color: red;
            margin-top: 8px;
        }
    </style>
</head>

<body>

<div class="container">
    <div class="form-box">

        <h2>
            <c:choose>
                <c:when test="${cat == null}">Pet Information</c:when>
                <c:otherwise>Update Pet</c:otherwise>
            </c:choose>
        </h2>

        <form method="post"
              action="${cat == null ? 'cat-add' : 'cat-update'}">

            <!-- CAT ID -->
            <c:if test="${cat != null}">
                <input type="hidden" name="catId" value="${cat.catID}" />
            </c:if>

            <!-- OWNER ID -->
            <div class="field">
                <label>Owner ID</label>
                <c:if test="${cat == null}">
                    <input type="number" name="ownerID" required />
                </c:if>

                <c:if test="${cat != null}">
                    <input type="number" value="${cat.ownerID}" readonly />
                    <input type="hidden" name="ownerID" value="${cat.ownerID}" />
                </c:if>
            </div>

            <!-- PET NAME -->
            <div class="field">
                <label>Pet Name</label>
                <input type="text" name="name" value="${cat.name}" required />
            </div>

            <!-- SPECIES -->
            <div class="field">
                <label>Species</label>
                <input type="text" name="breed"
                       value="${cat.breed}"
                ${cat != null ? "readonly" : ""}/>
            </div>

            <!-- AGE + GENDER (NGANG NHAU) -->
            <div class="row">

                <!-- AGE -->
                <div class="field">
                    <label>Age</label>
                    <input type="number" name="age"
                           value="${cat.age}"
                           min="0" max="20" required />

                    <h2>
                        <c:choose>
                            <c:when test="${cat == null}"><div class="hint">
                                If the cat is under 1 year old, please set Age to 0.
                            </div></c:when>
                            <c:otherwise><div class="hint">
                                A cat's age can only increase by a maximum of one year at a time.
                            </div></c:otherwise>
                        </c:choose>
                    </h2>

                </div>

                <!-- GENDER -->
                <div class="field">
                    <label>Gender</label>

                    <c:if test="${cat == null}">
                        <select name="gender">
                            <option value="0">Male</option>
                            <option value="1">Female</option>
                        </select>
                    </c:if>

                    <c:if test="${cat != null}">
                        <select disabled>
                            <option value="0" ${cat.gender == 0 ? "selected" : ""}>Male</option>
                            <option value="1" ${cat.gender == 1 ? "selected" : ""}>Female</option>
                        </select>
                        <input type="hidden" name="gender" value="${cat.gender}" />
                    </c:if>
                </div>

            </div>

            <div class="message">${message}</div>

            <!-- BUTTONS -->
            <div class="buttons">
                <button type="submit"
                        class="btn ${cat == null ? 'btn-add' : 'btn-update'}">
                    <c:choose>
                        <c:when test="${cat == null}">Add</c:when>
                        <c:otherwise>Update</c:otherwise>
                    </c:choose>
                </button>

                <button type="button"
                        class="btn btn-cancel"
                        onclick="window.location.href='${pageContext.request.contextPath}/cats'">
                    Cancel
                </button>
            </div>

        </form>

    </div>
</div>

</body>
</html>