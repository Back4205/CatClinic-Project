package com.mycompany.catclinicproject.controller.cat;

import com.mycompany.catclinicproject.dao.CatDAO;
import com.mycompany.catclinicproject.model.Cat;
import com.mycompany.catclinicproject.model.Owner;
import com.mycompany.catclinicproject.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;


@WebServlet (name = "CatListController", urlPatterns = {"/cats"})
public class CatListController extends HttpServlet {
   

      protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("acc");

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            CatDAO catDAO = new CatDAO();

            int userID = user.getUserID();
            int ownerID = catDAO.getOwnerIdByUserId(userID);


            String name = request.getParameter("name");
            String gender = request.getParameter("gender");
            String breed = request.getParameter("breed");
            String indexPageTmp = request.getParameter("indexPage");

            if (indexPageTmp == null) indexPageTmp = "1";
            int indexPage = Integer.parseInt(indexPageTmp);

            int age = -1;
            String ageRaw = request.getParameter("age");
            if (ageRaw != null && !ageRaw.isEmpty()) {
                age = Integer.parseInt(ageRaw);
            }

            int total = catDAO.countCatsWithFilter(ownerID, name, gender, breed, age);
            int numberItemInPage = 5;
            int pageSize = total / numberItemInPage;
            if (total % numberItemInPage != 0) {
                pageSize++;
            }

            List<Cat> catList = catDAO.filterAndPagingCats(ownerID, name, gender, breed, age, numberItemInPage, indexPage);

            for (Cat cat : catList) {
                boolean booking = catDAO.hasBooking(cat.getCatID());
                cat.setHasBooking(booking);
            }

            request.setAttribute("ownerID", ownerID);
            request.setAttribute("account", user);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("catList", catList);
            request.setAttribute("indexPage", indexPage);

            request.getRequestDispatcher("/WEB-INF/views/client/pet-list.jsp").forward(request, response);
        }
    }


