package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.ReceptionDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CounterCancellationController", urlPatterns = {"/reception/counter-cancellation"})
public class CounterCancellationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ReceptionDAO dao = new ReceptionDAO();
        String action = request.getParameter("action");
        String idStr = request.getParameter("id");
        String search = request.getParameter("search");
        if ("cancel".equals(action) && idStr != null) {
            dao.updateStatus(Integer.parseInt(idStr), "Cancelled");
            String redirectUrl = request.getContextPath() + "/reception/counter-cancellation";
            if (search != null && !search.isEmpty()) redirectUrl += "?search=" + search;
            response.sendRedirect(redirectUrl);
            return;
        }

        request.setAttribute("cancelList", dao.searchBookingsForCancellation(search));
        request.setAttribute("searchKeyword", search);
        request.getRequestDispatcher("/WEB-INF/views/reception/counter-cancellation.jsp").forward(request, response);
    }
}