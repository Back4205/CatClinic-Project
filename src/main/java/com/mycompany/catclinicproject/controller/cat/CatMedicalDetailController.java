package com.mycompany.catclinicproject.controller.cat;

import com.mycompany.catclinicproject.dao.MedicalRecordDAO;
import com.mycompany.catclinicproject.model.Drug;
import com.mycompany.catclinicproject.model.MedicalRecordDetailView;
import com.mycompany.catclinicproject.model.PrescriptionDrugView;
import com.mycompany.catclinicproject.model.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet (name = "CatMedicalDetailController", urlPatterns = {"/cats/medical-detail"})
public class CatMedicalDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        int bookingId = Integer.parseInt(request.getParameter("idBooking"));


        MedicalRecordDAO dao = new MedicalRecordDAO();


        MedicalRecordDetailView medicalDetail = dao.getMedicalRecordDetailViewByBookingID(bookingId);


        List<Service> serviceList = dao.getServiceByBookingID(bookingId);


        List<PrescriptionDrugView> drugList = dao.getPrescriptionDrugByBookingID(bookingId);
        request.setAttribute("id",bookingId);// test


        request.setAttribute("medicalDetail", medicalDetail);
        request.setAttribute("serviceList", serviceList);
        request.setAttribute("drugList", drugList);


        request.getRequestDispatcher("/WEB-INF/views/client/cat-medical-detail.jsp").forward(request, response);
    }
}