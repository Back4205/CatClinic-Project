package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.InvoiceDAO;
import com.mycompany.catclinicproject.dao.PaymentDAO;
import com.mycompany.catclinicproject.model.InvoiceDetail;
import com.mycompany.catclinicproject.model.InvoiceItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "InvoiceDetailController", urlPatterns = {"/reception/invoice_detail"})
public class InvoiceDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int bookingID = Integer.parseInt(request.getParameter("bookingID"));

        InvoiceDAO invoiceDAO = new InvoiceDAO();
        PaymentDAO paymentDAO = new PaymentDAO();

        InvoiceDetail invoice = invoiceDAO.getInvoiceInfo(bookingID);
        List<InvoiceItem> listServiceUse = invoiceDAO.getServicesByBookingID(bookingID);
        double hasPaid = paymentDAO.getPaidAmount(bookingID);

        double deposit = paymentDAO.getDepositByInvoice(bookingID);
        double totalServiceAmount = invoiceDAO.getTotalServiceAmount(bookingID);

        int invoiceID = Integer.parseInt(invoice.getInvoiceCode());

        invoiceDAO.updateTotalAmount(invoiceID, totalServiceAmount);
        request.setAttribute("deposit", deposit);
        request.setAttribute("inforInvoiceDetail", invoice);
        request.setAttribute("listServiceUse", listServiceUse);
        request.setAttribute("hasPaid", hasPaid);
        request.setAttribute("totalServiceAmount", totalServiceAmount);

        request.getRequestDispatcher("/WEB-INF/views/reception/Invoice_detail.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String paymentMethod = request.getParameter("paymentMethod");
        int bookingID = Integer.parseInt(request.getParameter("bookingID"));

        PaymentDAO paymentDAO = new PaymentDAO();
        InvoiceDAO invoiceDAO = new InvoiceDAO();

        try {


            InvoiceDetail invoice = invoiceDAO.getInvoiceInfo(bookingID);

            if (invoice == null) {
                response.sendRedirect("billing-bookings");
                return;
            }

            int invoiceID = Integer.parseInt(invoice.getInvoiceCode());
            String status = invoice.getInvoiceStatus();

            if ("Paid".equalsIgnoreCase(status)) {

                request.setAttribute("error", "Invoice already paid.");
                request.getRequestDispatcher("/WEB-INF/views/reception/Invoice_detail.jsp")
                        .forward(request, response);
                return;
            }

            double total = invoiceDAO.getTotalServiceAmount(bookingID);
            double paid = paymentDAO.getPaidAmount(bookingID);
            double remain = total - paid;

            if ("Cash".equals(paymentMethod)) {

                paymentDAO.insertPayment(invoiceID, remain, "Cash", null);
                invoiceDAO.updatePaymentStatus(invoiceID, "Paid");

                response.sendRedirect(request.getContextPath()
                        + "/reception/billing-bookings");
            }


            else if ("Bank Transfer".equals(paymentMethod)) {
                paymentDAO.insertPayment(invoiceID, remain, "BankTransfer", null);
                invoiceDAO.updatePaymentStatus(invoiceID, "Paid");

                response.sendRedirect(request.getContextPath()
                        + "/reception/invoice_detail?bookingID=" + bookingID);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}