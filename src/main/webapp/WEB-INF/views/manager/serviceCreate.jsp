<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create Service</title>

    <!-- CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/service-create.css">
</head>

<body>

<div class="container">
    <h2>Create New Service</h2>
    <form action="${pageContext.request.contextPath}/CreateService" method="post">

        <label>Service Name</label>
        <input type="text" name="name" required>

        <label>Price</label>
        <input type="number" name="price" step="0.01" required>

        <label>Description</label>
        <textarea name="description" rows="4"></textarea>

        <label>Time (minutes)</label>
        <input type="number" name="time" required>

        <div class="checkbox-group">
            <input type="checkbox" name="isActive">
            <span>Active</span>
        </div>

        <div class="actions">
            <button type="submit">Create Service</button>

            <a class="cancel"
               href="${pageContext.request.contextPath}/ViewServiceList">
                Cancel
            </a>
        </div>

    </form>
</div>

</body>
</html>
