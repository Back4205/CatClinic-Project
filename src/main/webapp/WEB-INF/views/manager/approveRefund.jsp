<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Approve Refund</title>
    <link rel="stylesheet" href="css/refund.css">
</head>
<body>

<div class="container">
    <h2>Upload Refund Proof</h2>

    <form action="ApproveRefund" method="post" enctype="multipart/form-data">
        
        <input type="hidden" name="bookingID" value="${bookingID}">

        <label class="file-label">Select refund proof image:</label>

        <!-- Custom File Upload -->
        <div class="file-upload">
            <label for="fileInput" class="custom-file-btn">
                Choose Image
            </label>
            <span id="fileName">No file chosen</span>
            <input type="file" id="fileInput" name="refundImage" accept="image/*" required>
        </div>

        <!-- Image Preview -->
        <div class="preview-container">
            <img id="previewImage" src="#" alt="Preview Image">
        </div>

        <button type="submit" class="btn">Submit Refund</button>
    </form>
</div>

<script>
    const fileInput = document.getElementById("fileInput");
    const fileName = document.getElementById("fileName");
    const previewImage = document.getElementById("previewImage");

    fileInput.addEventListener("change", function () {
        const file = this.files[0];

        if (file) {
            fileName.textContent = file.name;

            const reader = new FileReader();
            reader.onload = function (e) {
                previewImage.src = e.target.result;
                previewImage.style.display = "block";
            };
            reader.readAsDataURL(file);
        }
    });
</script>

</body>
</html>