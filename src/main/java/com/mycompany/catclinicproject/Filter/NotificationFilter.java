package com.mycompany.catclinicproject.filter;

import com.mycompany.catclinicproject.dao.NotificationDAO;
import com.mycompany.catclinicproject.model.NotificationDTO;
import com.mycompany.catclinicproject.model.User;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter("/*")
public class NotificationFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);
        if (session != null) {
            User acc = (User) session.getAttribute("acc");
            if (acc != null) {
                NotificationDAO dao = new NotificationDAO();
                int vetID = dao.getVetIDByUserID(acc.getUserID());
                session.setAttribute("vetID", vetID);
                int count = dao.countUnreadByVet(vetID);
                req.setAttribute("unreadNoti", count);
                List<NotificationDTO> list = dao.getNotificationByVet(vetID);
                session.setAttribute("listNoti", list);
            }
        }
        chain.doFilter(request, response);
    }
}