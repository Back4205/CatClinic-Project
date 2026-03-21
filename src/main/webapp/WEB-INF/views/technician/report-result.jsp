<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Report Result</title>
    <link rel="stylesheet" href="css/report-result.css">
</head>
<body>

<div class="container">
    <h2>Laboratory Report Result</h2>

    <form action="ReportResultController" method="post" enctype="multipart/form-data">

        <input type="hidden" name="testOrderID" value="${testOrder.testOrderID}" />
        <input type="hidden" name="oldimage" value="${testOrder.result}" />

        <!-- Hiển thị ảnh cũ -->
        <c:if test="${not empty testOrder.result}">
            <div class="current-image">
                <p>Current Image:</p>
                <img src="${testOrder.result}" />
            </div>
        </c:if>

        <!-- Upload file -->
        <div class="form-group">
            <label>Upload New Result Image</label>

            <div class="upload-box">
                <input type="file" name="resultFile" id="resultFile" accept="image/*" onchange="previewImage(event)">
                <div class="upload-content">
                    <span class="upload-icon">📁</span>
                    <p>Click to choose image</p>
                </div>
            </div>

            <!-- Preview ảnh -->
            <div class="preview-container">
                <img id="imagePreview" src="#" alt="Preview Image"/>
            </div>
        </div>

        <!-- Ghi chú -->
        <div class="form-group">
            <label>Laboratory Findings</label>
            <textarea name="resultName"
                      placeholder="Enter detailed laboratory findings...">${testOrder.resultName}</textarea>
        </div>

        <div class="button-group">
            <button type="submit" name="action" value="draft" class="btn draft-btn">
                Save Draft
            </button>

            <button type="submit" name="action" value="submit" class="btn submit-btn">
                Submit Result
            </button>
        </div>

    </form>
</div>
<script>
    function previewImage(event) {
        const input = event.target;
        const preview = document.getElementById('imagePreview');

        if (input.files && input.files[0]) {
            const reader = new FileReader();

            reader.onload = function(e) {
                preview.src = e.target.result;
                preview.style.display = "block";
            }

            reader.readAsDataURL(input.files[0]);
        }
    }
</script>
</body>
</html>