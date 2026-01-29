package com.mycompany.catclinicproject.controller.cat;

import com.mycompany.catclinicproject.dao.CatDAO;
import com.mycompany.catclinicproject.model.Cat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet (name = "CatListController", urlPatterns = {"/cats"})
public class CatListController extends HttpServlet {
 protected  void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
     String name = request.getParameter("name");
     String gender = request.getParameter("gender");
     String breed = request.getParameter("breed");
     String  indexPageTmp =  request.getParameter("indexPage");

     if (indexPageTmp== null) {
         indexPageTmp = "1";
     }
     int indexPage = Integer.parseInt(indexPageTmp);



     int age = -1; // mặc định không filter
     String ageRaw = request.getParameter("age");
     if (ageRaw != null && !ageRaw.isEmpty()) {
         age = Integer.parseInt(ageRaw);
     }
     int ownerID = 1; // gia su ownerID = 1
     CatDAO catDAO = new CatDAO();
//     List<Cat> catList = catDAO.fillterCats(ownerID,name, gender, breed, age);
//     for (Cat cat : catList) {
//         boolean booking = catDAO.hasBooking(cat.getCatID());
//         cat.setHasBooking(booking);
//     }

     // phan trang
     int total = catDAO.countCatsWithFilter(ownerID, name, gender, breed, age);
     int pageSize = total / 5 ;
     if (total % 5 != 0) {
            pageSize += 1;
     }
     List<Cat> catList = catDAO.filterAndPagingCats(ownerID, name, gender,breed, age, 5, indexPage);
     for (Cat cat : catList) {
         boolean booking = catDAO.hasBooking(cat.getCatID());
         cat.setHasBooking(booking);
     }

        request.setAttribute("pageSize", pageSize);
        request.setAttribute("catList", catList);
        request.setAttribute("indexPage", indexPage);
        request.getRequestDispatcher("/WEB-INF/views/client/pet-list.jsp").forward(request, response);

 }
}
