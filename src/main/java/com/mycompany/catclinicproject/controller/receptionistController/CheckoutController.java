package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.BookingHistory2DTO;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import java.io.IOException;
import java.util.List;

import com.mycompany.catclinicproject.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(name = "CheckoutController", urlPatterns = {"/reception/checkout-queue", "/reception/checkout"})
public class CheckoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null || user.getRoleID() != 3) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        BookingDAO dao = new BookingDAO();
        String path = request.getServletPath();
        if (path.equals("/reception/checkout")) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                String condition = request.getParameter("condition");
                boolean success = dao.updateCheckoutStatus(id, condition);
                if (success) {

                    response.sendRedirect(request.getContextPath() + "/reception/invoice_detail?bookingID=" + id);
                } else {
                    response.sendRedirect("checkout-queue?status=error");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("checkout-queue?status=error");
            }
            return;
        }
        String search = request.getParameter("search");
        String dateFilter = request.getParameter("dateFilter");

        String indexPage = request.getParameter("index");
        if (indexPage == null || indexPage.trim().isEmpty()) {
            indexPage = "1";
        }
        int index = Integer.parseInt(indexPage);

        if (dateFilter == null || dateFilter.trim().isEmpty()) {
            dateFilter = java.time.LocalDate.now().toString();
        }

        int pageSize = 2;

        int total = dao.countCheckoutQueue(search, dateFilter);
        int endPage = total / pageSize;
        if (total % pageSize != 0) {
            endPage++;
        }
        List<BookingHistory2DTO> list = dao.getCheckoutPaging(search, dateFilter, (index - 1) * pageSize, pageSize);

        request.setAttribute("checkoutList", list);
        request.setAttribute("currentDate", dateFilter);
        request.setAttribute("currentSearch", search);
        request.setAttribute("endP", endPage); // Tổng số trang
        request.setAttribute("tag", index);    // Trang đang đứng để tô màu nút
        request.setAttribute("activePage", "CheckOut");

        request.getRequestDispatcher("/WEB-INF/views/reception/counter-checkout.jsp")
                .forward(request, response);
    }
}