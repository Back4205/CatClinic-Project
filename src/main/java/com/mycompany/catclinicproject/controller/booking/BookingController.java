package com.mycompany.catclinicproject.controller.booking;

import com.mycompany.catclinicproject.dao.*;
import com.mycompany.catclinicproject.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@WebServlet(name = "BookingController", urlPatterns = {"/Booking"})
public class BookingController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("acc") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?from=booking");
            return;
        }

        loadBookingData(request, user);
        request.getRequestDispatcher("/WEB-INF/views/client/Booking.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("acc") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy dữ liệu từ Form
        String catParam = request.getParameter("catID");
        String serviceParam = request.getParameter("serviceID");
        String assigneeParam = request.getParameter("assigneeInfo");
        String slotParam = request.getParameter("slotID");
        String startDateParam = request.getParameter("startDate");
        String note = request.getParameter("note");

        BookingDAO bookingDAO = new BookingDAO();
        TimeSlotDAO slotDAO = new TimeSlotDAO();
        Connection conn = null;

        try {
            int catID = Integer.parseInt(catParam);
            int serviceID = Integer.parseInt(serviceParam);
            int assigneeID = Integer.parseInt(assigneeParam);

            conn = bookingDAO.getConnection();
            conn.setAutoCommit(false);

            Booking booking = new Booking();
            booking.setCatID(catID);
            booking.setNote(note);
            booking.setStatus("PendingPayment");

            // KIỂM TRA LOẠI NGƯỜI PHỤ TRÁCH (Bác sĩ hay Staff)
            UserDAO userDAO = new UserDAO();
            User assignee = userDAO.getUserByID(assigneeID);

            if (assignee.getRoleID() == 2) { // BÁC SĨ: Cần Slot
                int slotID = Integer.parseInt(slotParam);
                if (!bookingDAO.lockSlot(slotID)) {
                    throw new Exception("Slot đã bị người khác đặt!");
                }
                TimeSlot slot = slotDAO.getSlotByID(slotID);
                booking.setVeterinarianID(assigneeID);
                booking.setSlotID(slotID);
                booking.setAppointmentDate(slot.getSlotDate());
                booking.setEndDate(slot.getSlotDate());
                booking.setAppointmentTime(slot.getStartTime());
            } else { // STAFF: Trông mèo - Chỉ cần ngày bắt đầu
                booking.setStaffID(assigneeID);
                booking.setAppointmentDate(java.sql.Date.valueOf(startDateParam));
                booking.setEndDate(java.sql.Date.valueOf(startDateParam)); // Mặc định 1 ngày
                booking.setAppointmentTime(java.sql.Time.valueOf("08:00:00"));
            }

            int bookingID = bookingDAO.createBooking(booking);

            // Tạo hóa đơn và xử lý thanh toán...
            conn.commit();
            response.sendRedirect("vnpay?bookingID=" + bookingID);

        } catch (Exception e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            request.setAttribute("error", e.getMessage());
            loadBookingData(request, user);
            request.getRequestDispatcher("/WEB-INF/views/client/Booking.jsp").forward(request, response);
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ex) {}
        }
    }

    private void loadBookingData(HttpServletRequest request, User user) {
        CatDAO catDAO = new CatDAO();
        TimeSlotDAO slotDAO = new TimeSlotDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        UserDAO userDAO = new UserDAO();

        // 1. Dữ liệu mèo và phân trang
        int ownerID = catDAO.getOwnerIdByUserId(user.getUserID());
        int indexPage = request.getParameter("indexPage") != null ? Integer.parseInt(request.getParameter("indexPage")) : 1;
        request.setAttribute("catList", catDAO.getCatsByOwnerPaging(ownerID, indexPage, 5));
        request.setAttribute("totalPage", (int) Math.ceil((double) catDAO.countCats(ownerID) / 5));
        request.setAttribute("indexPage", indexPage);

        // 2. Dịch vụ và Người phụ trách
        request.setAttribute("serviceList", serviceDAO.getAllServices());
        List<UserDTO> listPerson = userDAO.getVetAndCareStaff();
        request.setAttribute("listPerson", listPerson);

        // 3. Xử lý lịch trình 7 ngày
        String assigneeParam = request.getParameter("assigneeInfo");
        String startDateParam = request.getParameter("startDate");
        if (startDateParam == null || startDateParam.isEmpty()) {
            startDateParam = LocalDate.now().toString();
        }

        Map<String, List<TimeSlot>> groupedSlots = new LinkedHashMap<>();
        String isStaff = "false";

        if (assigneeParam != null && !assigneeParam.isEmpty()) {
            int assigneeID = Integer.parseInt(assigneeParam);

            // Kiểm tra role người phụ trách
            for (UserDTO p : listPerson) {
                if (p.getUserID() == assigneeID) {
                    if ("Staff".equalsIgnoreCase(p.getType()) || p.getRoleID() == 4) {
                        isStaff = "true";
                    }
                    break;
                }
            }

            // Chỉ load slot nếu là Bác sĩ
            if ("false".equals(isStaff)) {
                List<TimeSlot> flatList = slotDAO.getSlotsNext7DaysFromDate(assigneeID, startDateParam);
                for (TimeSlot s : flatList) {
                    String dateKey = s.getSlotDate().toString();
                    groupedSlots.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(s);
                }
            }
        }

        request.setAttribute("slotListGrouped", groupedSlots);
        request.setAttribute("isStaff", isStaff);
        request.setAttribute("currentDate", LocalDate.now().toString());
    }
}