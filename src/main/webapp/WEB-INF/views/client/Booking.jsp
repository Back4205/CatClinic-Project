<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.time.LocalDate"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Book Appointment | CatClinic</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">

  <style>
    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f5f7fa; margin: 0; padding: 0; }
    .container { width: 1200px; margin: 30px auto; }
    h2 { color: #1f2937; font-weight: 800; text-transform: uppercase; border-left: 5px solid #ff6600; padding-left: 15px; margin-bottom: 30px; }

    .booking-wrapper { display: flex; gap: 30px; align-items: flex-start; }
    .left-panel { width: 45%; }
    .right-panel { width: 55%; }

    .card { background: #fff; padding: 25px; border-radius: 12px; margin-bottom: 25px; border: 1px solid #e5e7eb; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1); }
    .card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #f3f4f6; padding-bottom: 10px; }
    .card-header h4 { margin: 0; color: #374151; font-size: 16px; text-transform: uppercase; }

    /* SELECTION DROPDOWNS & DATE */
    .selection-grid { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 15px; margin-bottom: 25px; }
    .selection-grid label { display: block; font-size: 12px; font-weight: 700; color: #6b7280; text-transform: uppercase; margin-bottom: 8px; }
    .selection-grid select, .selection-grid input[type="date"] {
      width: 100%; padding: 10px; border-radius: 8px; border: 1.5px solid #e5e7eb;
      background-color: #fff; color: #374151; font-family: inherit; cursor: pointer; font-size: 13px;
    }

    /* TABLE STYLES (PET LIST) */
    table { width: 100%; border-collapse: collapse; }
    th { background: #f9fafb; padding: 10px; text-align: left; font-size: 11px; color: #6b7280; text-transform: uppercase; }
    td { padding: 10px; border-bottom: 1px solid #f3f4f6; vertical-align: middle; font-size: 13px; }
    .pet-img { width: 40px; height: 40px; border-radius: 6px; object-fit: cover; }
    .badge { padding: 3px 8px; border-radius: 12px; font-size: 10px; font-weight: 700; }
    .male { background: #e0f2fe; color: #0369a1; }
    .female { background: #fce7f3; color: #9d174d; }

    /* MINI PAGINATION */
    .pagination { margin-top: 15px; display: flex; justify-content: center; gap: 4px; }
    .pagination a { padding: 3px 8px; border: 1px solid #e5e7eb; text-decoration: none; color: #4b5563; border-radius: 4px; font-size: 10px; min-width: 20px; text-align: center; }
    .pagination a.active { background: #ff6600; color: white; border-color: #ff6600; font-weight: bold; }

    /* SCHEDULE DISPLAY (7 DAYS) */
    .schedule-scroll { max-height: 400px; overflow-y: auto; padding-right: 10px; }
    .day-group { margin-bottom: 15px; }
    .day-title { font-size: 12px; font-weight: bold; color: #ff6600; background: #fff7f0; padding: 5px 10px; border-radius: 5px; margin-bottom: 8px; display: inline-block; }

    .slot-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; }
    .slot { padding: 8px; border: 1.5px solid #e5e7eb; border-radius: 8px; cursor: pointer; display: flex; justify-content: center; align-items: center; font-size: 12px; transition: 0.2s; }
    .slot:hover { border-color: #ff6600; background: #fffaf5; }
    .slot input { accent-color: #ff6600; margin-right: 5px; }

    .btn-main { width: 100%; padding: 14px; background: #ff6600; color: white; border: none; border-radius: 10px; margin-top: 20px; cursor: pointer; font-weight: 800; text-transform: uppercase; }
  </style>
</head>
<body>

<div class="container">
  <h2><i class="bi bi-calendar-check"></i> Book Appointment</h2>

  <div class="booking-wrapper">
    <div class="left-panel">
      <div class="card">
        <div class="card-header"><h4><i class="bi bi-person-badge"></i> Owner Information</h4></div>
        <div style="font-size: 13px;">
          <div><strong>Name:</strong> ${acc.fullName}</div>
          <div><strong>Phone:</strong> ${acc.phone}</div>
          <div><strong>Email:</strong> ${acc.email}</div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <h4><i class="bi bi-github"></i> Your Cats</h4>
          <a href="${pageContext.request.contextPath}/cats/cat-add?from=booking" style="background:#22a06b; color:white; padding:6px 12px; border-radius:6px; font-size:11px; text-decoration:none;">
            + Add New
          </a>
        </div>

        <form action="Booking" method="post" id="bookingForm">
          <table>
            <thead>
            <tr><th>Select</th><th>Photo</th><th>Name</th><th>Age</th><th>Gender</th></tr>
            </thead>
            <tbody>
            <c:forEach items="${catList}" var="c" varStatus="status">
              <tr>
                <td><input type="radio" name="catID" value="${c.catID}" ${status.first ? 'checked' : ''}></td>
                <td><img src="${pageContext.request.contextPath}/${c.img}" class="pet-img"></td>
                <td><strong>${c.name}</strong></td>
                <td>${c.age == 0 ? '< 1 Y' : c.age += ' Y'}</td>
                <td><span class="badge ${c.gender == 1 ? 'male' : 'female'}">${c.gender == 1 ? 'M' : 'F'}</span></td>
              </tr>
            </c:forEach>
            </tbody>
          </table>

          <div class="pagination">
            <c:if test="${indexPage > 1}"><a href="Booking?indexPage=${indexPage - 1}"><i class="bi bi-chevron-left"></i></a></c:if>
            <c:forEach begin="1" end="${totalPage}" var="i"><a class="${i == indexPage ? 'active' : ''}" href="Booking?indexPage=${i}">${i}</a></c:forEach>
            <c:if test="${indexPage < totalPage}"><a href="Booking?indexPage=${indexPage + 1}"><i class="bi bi-chevron-right"></i></a></c:if>
          </div>
      </div>
    </div>

    <div class="right-panel">
      <div class="card">
        <div class="card-header"><h4><i class="bi bi-pencil-square"></i> Details</h4></div>

        <c:if test="${not empty error}"><div style="color:#dc2626; font-size:12px; margin-bottom:10px;">${error}</div></c:if>

        <div class="selection-grid">
          <div>
            <label>Service</label>
            <select name="serviceID" required>
              <option value="">-- Choose --</option>
              <c:forEach items="${serviceList}" var="ser">
                <option value="${ser.serviceID}" ${param.serviceID == ser.serviceID ? 'selected' : ''}>${ser.nameService}</option>
              </c:forEach>
            </select>
          </div>

          <div>
            <label>Doctor / Staff</label>
            <select name="assigneeInfo" required onchange="this.form.action='Booking'; this.form.method='get'; this.form.submit();">
              <option value="">-- Select --</option>
              <c:forEach items="${listPerson}" var="p">
                <option value="${p.userID}" ${param.assigneeInfo == p.userID ? 'selected' : ''}>
                    ${p.type == 'Veterinarian' ? 'Dr. ' : 'Staff: '} ${p.fullName}
                </option>
              </c:forEach>
            </select>
          </div>

          <div>
            <label>Start Date</label>
            <input type="date" name="startDate" value="${empty param.startDate ? currentDate : param.startDate}"
                   min="${currentDate}" required onchange="this.form.action='Booking'; this.form.method='get'; this.form.submit();">
          </div>
        </div>

        <div class="card-header" style="border:none; margin-top:5px;">
          <h4 style="font-size: 13px; color:#6b7280;"><i class="bi bi-clock-history"></i> Available Slots (7 Days)</h4>
        </div>

        <div class="schedule-scroll">
          <c:forEach items="${slotListGrouped}" var="entry">
            <div class="day-group">
              <div class="day-title">${entry.key}</div> <div class="slot-grid">
              <c:forEach items="${entry.value}" var="s"> <label class="slot">
                <input type="radio" name="slotID" value="${s.slotID}" required>
                  ${s.startTime}
              </label>
              </c:forEach>
            </div>
            </div>
          </c:forEach>

          <c:if test="${empty slotListGrouped}">
            <p style="text-align:center; font-size:12px; color:#9ca3af; padding: 20px;">
              No slots available. Please select a Doctor and Start Date.
            </p>
          </c:if>
        </div>

        <div style="margin-top: 15px;">
          <label style="font-weight: bold; display: block; font-size: 12px; margin-bottom: 5px;">Special Notes:</label>
          <textarea name="note" style="width: 100%; padding: 10px; border-radius: 8px; border: 1.5px solid #e5e7eb; min-height: 50px; font-size: 12px;" placeholder="Symptoms..."></textarea>
        </div>

        <button type="submit" class="btn-main">Confirm & Pay Now</button>
        </form>
      </div>
    </div>
  </div>
</div>

</body>
</html>