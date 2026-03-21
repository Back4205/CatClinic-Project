<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Record Care Diary | CatClinic</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/carestaff-style.css">
</head>
<body>
<!-- Sidebar -->
<aside class="sidebar">
    <div class="logo"><i class="fa-solid fa-cat"></i> Purrfect Care</div>
    <a href="${pageContext.request.contextPath}/staff/daily-care-tasks"><i class="fa-solid fa-list-check"></i> Care Tasks</a>
    <a href="${pageContext.request.contextPath}/staff/record-care-diary" class="active"><i class="fa-solid fa-book-medical"></i> Care Diary</a>
    <a href="${pageContext.request.contextPath}/logout" class="logout"><i class="fa-solid fa-arrow-right-from-bracket"></i> Logout</a>
</aside>

<!-- Main Content -->
<main class="main">
    <div class="header-top">
        <div>
            <h1>Record Care Diary</h1>
            <p>Log daily observations and vitals for inpatient cats</p>
        </div>
    </div>

    <form action="${pageContext.request.contextPath}/staff/record-care-diary" method="POST">
        <div class="form-card">
            <div class="form-row">
                <div class="form-group">
                    <label class="form-label"><i class="fa-solid fa-cat"></i> SELECT PATIENT</label>
                    <select name="careJID" class="form-control" required>
                        <option value="" disabled selected>-- Select a Cat --</option>
                        <c:forEach items="${pendingTasks}" var="task">
                            <option value="${task.careJID}">${task.catName} (${task.breed})</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label class="form-label"><i class="fa-solid fa-utensils"></i> APPETITE</label>
                    <div class="pill-group">
                        <label class="pill"><input type="radio" name="appetite" value="None"> None</label>
                        <label class="pill"><input type="radio" name="appetite" value="Poor"> Poor</label>
                        <label class="pill active"><input type="radio" name="appetite" value="Normal" checked> Normal</label>
                        <label class="pill"><input type="radio" name="appetite" value="Great"> Great</label>
                    </div>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">Temperature (°C)</label>
                    <input type="text" name="temp" class="form-control" placeholder="e.g. 38.5">
                </div>
                <div class="form-group">
                    <label class="form-label">Weight (kg)</label>
                    <input type="text" name="weight" class="form-control" placeholder="e.g. 4.2">
                </div>
                <div class="form-group">
                    <label class="form-label">Heart Rate (bpm)</label>
                    <input type="text" name="heartRate" class="form-control" placeholder="e.g. 140">
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">DETAILED OBSERVATIONS</label>
                <textarea name="note" class="form-control" placeholder="Describe behavior, stool, urination, or any concerns..."></textarea>
            </div>

            <button type="submit" class="btn-save"><i class="fa-regular fa-floppy-disk"></i> Save Diary Entry</button>
        </div>
    </form>

    <div class="col-title" style="margin-top: 40px;"><i class="fa-solid fa-clock-rotate-left"></i> RECENT DIARY ENTRIES</div>
    <div class="card">
        <!-- Dữ liệu mẫu (Bạn có thể dùng c:forEach để load từ Database lên sau) -->
        <div class="log-item">
            <div>
                <div><span class="log-cat">Milo</span> <span class="log-time">08:15 AM</span></div>
                <div class="log-content">Milo took her medicine well. Appetite is normal for the morning.</div>
                <div><span class="badge badge-feed">APPETITE: NORMAL</span></div>
            </div>
            <div class="log-author">By Care Staff</div>
        </div>
    </div>
</main>

<script>
    // JS nhỏ để đổi màu nút Appetite khi click
    document.querySelectorAll('.pill input').forEach(radio => {
        radio.addEventListener('change', function() {
            document.querySelectorAll('.pill').forEach(p => p.classList.remove('active'));
            this.parentElement.classList.add('active');
        });
    });
</script>
</body>
</html>