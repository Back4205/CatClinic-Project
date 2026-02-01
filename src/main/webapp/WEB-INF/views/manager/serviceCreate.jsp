<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Service</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #ffffff;
            color: #333;
            padding: 40px;
        }

        .container {
            max-width: 520px;
            margin: 0 auto;
            border: 1px solid #eee;
            border-radius: 10px;
            padding: 28px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.05);
        }

        h2 {
            margin-bottom: 24px;
            color: #111;
        }

        label {
            font-weight: 600;
            display: block;
            margin-bottom: 6px;
        }

        input[type="text"],
        input[type="number"],
        textarea {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            margin-bottom: 16px;
            font-size: 14px;
        }

        textarea {
            resize: vertical;
        }

        .checkbox-group {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 20px;
        }

        .actions {
            display: flex;
            gap: 12px;
        }

        button {
            background: #f97316;
            color: #fff;
            border: none;
            padding: 10px 18px;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 600;
        }

        button:hover {
            opacity: 0.9;
        }

        .cancel {
            display: inline-flex;
            align-items: center;
            padding: 10px 18px;
            border-radius: 6px;
            text-decoration: none;
            border: 1px solid #f97316;
            color: #f97316;
            font-weight: 600;
        }

        .cancel:hover {
            background: #f97316;
            color: #fff;
        }
    </style>
</head>

<body>

<div class="container">
    <h2>Create New Service</h2>

    <!-- ❗ GIỮ NGUYÊN action + method + name -->
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
            <a class="cancel" href="${pageContext.request.contextPath}/ViewServiceList">Cancel</a>
        </div>

    </form>
</div>

</body>
</html>
