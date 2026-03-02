package com.mycompany.catclinicproject.controller.booking;

import com.mycompany.catclinicproject.config.VNPayConfig;
import com.mycompany.catclinicproject.dao.InvoiceDAO;
import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.dao.PaymentDAO;
import com.mycompany.catclinicproject.model.Invoice;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "VNPayReturnController", urlPatterns = {"/vnpay-return"})
public class VNPayReturnController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            // VERIFY
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = fields.remove("vnp_SecureHash");
            String signValue = VNPayConfig.hashAllFields(fields);

            if (!signValue.equals(vnp_SecureHash)) {
                request.setAttribute("msg", "invalid_signature");
                request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
                return;
            }

            //LẤY DATA
            String responseCode = request.getParameter("vnp_ResponseCode");
            String txnRef = request.getParameter("vnp_TxnRef");
            String amountStr = request.getParameter("vnp_Amount");
            String transactionNo = request.getParameter("vnp_TransactionNo");

            int invoiceID = Integer.parseInt(txnRef.split("_")[0]);

            InvoiceDAO invoiceDAO = new InvoiceDAO();
            BookingDAO bookingDAO = new BookingDAO();
            PaymentDAO paymentDAO = new PaymentDAO();

            Invoice invoice = invoiceDAO.getInvoiceByID(invoiceID);
            if (invoice == null) {
                request.setAttribute("msg", "invoice_not_found");
                request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
                return;
            }

            int bookingID = invoice.getBookingID();

            // CANCEL
            if ("24".equals(responseCode)) {
                request.setAttribute("msg", "failed");
                request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
                return;
            }

            // 4. FAIL
            if (!"00".equals(responseCode)) {
                request.setAttribute("msg", "failed");
                request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
                return;
            }

            // CHỐNG DOUBLE
            if (paymentDAO.isTransactionExists(transactionNo)) {
                request.setAttribute("msg", "success");
                request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
                return;
            }

            // INSERT PAYMENT

            long amountPaid = Long.parseLong(amountStr) / 100;

            //  tổng tiền invoice
            long totalInvoiceAmount = (long) invoice.getTotalAmount();

            //  tổng tiền đã thanh toán trước đó
            long alreadyPaid = paymentDAO.getTotalPaidByInvoice(invoiceID);

            //  nếu đã trả đủ rồi thì không cho trả nữa
            if (alreadyPaid >= totalInvoiceAmount) {
                request.setAttribute("msg", "already_paid");
                request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
                return;
            }

            //  nếu trả vượt quá số tiền còn lại
            if (alreadyPaid + amountPaid > totalInvoiceAmount) {
                request.setAttribute("msg", "over_paid");
                request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
                return;
            }

             // insert payment
            paymentDAO.insertPayment(invoiceID, amountPaid, "VNPay", transactionNo);

            long newTotalPaid = alreadyPaid + amountPaid;

            if (newTotalPaid < totalInvoiceAmount) {

                // Thanh toán một phần (20%)
                invoiceDAO.updatePaymentStatus(invoiceID, "PartiallyPaid");

                // Có tiền đặt cọc thì confirm booking
                bookingDAO.confirmBooking(bookingID);

            } else {

                // Thanh toán đủ 100%
                invoiceDAO.updatePaymentStatus(invoiceID, "Paid");

            }



            request.setAttribute("transactionCode", transactionNo);
            request.setAttribute("msg", "success");
            request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "system_error");
            request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
        }
    }
}