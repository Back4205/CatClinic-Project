/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.RefundManagement;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.CancelBookingDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author ADMIN
 */
@WebServlet(name="ViewCancelBookingList", urlPatterns={"/ViewCancelBookingList"})
public class ViewCancelBookingList extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BookingDAO dao = new BookingDAO();
        List<CancelBookingDTO> list = dao.getAllCancelBookings();
        double totalMonth = dao.getTotalRefundedThisMonth();
        String keyword = request.getParameter("search");
        String filterStatus = request.getParameter("status"); // Giá trị từ dropdown: ALL, PendingCancelRefund, ...
        String monthName = java.time.LocalDate.now().getMonth().name();
        // ===== 1. SEARCH + FILTER =====
        List<CancelBookingDTO> filteredList = new java.util.ArrayList<>();

        for (CancelBookingDTO c : list) {
            boolean matchKeyword = true;
            boolean matchStatus = true;

            // SEARCH theo tên dịch vụ (NameService)
            if (keyword != null && !keyword.trim().isEmpty()) {
                String k = keyword.toLowerCase().trim();
                String name = c.getNameService() != null ? c.getNameService().toLowerCase() : "";
                if (!name.contains(k)) {
                    matchKeyword = false;
                }
            }

            // FILTER theo 3 trạng thái chuỗi (String)
            if (filterStatus != null && !filterStatus.equals("ALL") && !filterStatus.isEmpty()) {
                // So sánh trực tiếp status của DTO với giá trị filter từ request
                if (c.getStatus() == null || !c.getStatus().equals(filterStatus)) {
                    matchStatus = false;
                }
            }

            if (matchKeyword && matchStatus) {
                filteredList.add(c);
            }
        }

        // ===== 2. PAGINATION (PHÂN TRANG) =====
        int pageSize = 4;
        int currentPage = 1;
        String pageParam = request.getParameter("page");

        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        int totalRecord = filteredList.size();
        int totalPage = (int) Math.ceil((double) totalRecord / pageSize);

        if (currentPage > totalPage && totalPage > 0) currentPage = totalPage;
        if (currentPage < 1) currentPage = 1;

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, totalRecord);

        // Sửa lại kiểu dữ liệu từ Category thành CancelBookingDTO
        List<CancelBookingDTO> pagedList = new java.util.ArrayList<>();
        if (totalRecord > 0 && start < totalRecord) {
            pagedList = filteredList.subList(start, end);
        }

        // ===== 3. GỬI DỮ LIỆU SANG JSP =====
        request.setAttribute("totalRefundMonth", totalMonth);
        request.setAttribute("cancelList", pagedList); // Gửi danh sách đã phân trang
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("search", keyword);
        request.setAttribute("status", filterStatus);
        request.setAttribute("currentMonthName", monthName);
        request.getRequestDispatcher("/WEB-INF/views/manager/cancelBookings.jsp")
               .forward(request, response);
    }

}
