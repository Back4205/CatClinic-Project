package com.mycompany.catclinicproject.scheduler;

import com.mycompany.catclinicproject.dao.BookingDAO;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener // khi server chajy thì class này sẽ được gọi .
                                    // interface có sẵn trong java
public class AutoCancelScheduler implements ServletContextListener {
    // chay theo thoi gian va co thoi gian lap lại
    private ScheduledExecutorService scheduler;

    // chay sẻrver và chạy 1 lần duy nhất khi sẻve khởi động
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        scheduler = Executors.newSingleThreadScheduledExecutor();
        // chjay ngam
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("Auto cancel checking...");
                BookingDAO dao = new BookingDAO();
                dao.autoCancelExpiredBookings();
            }
        };

        // Sau 1 phút bắt đầu chạy
        // Sau đó cứ 10 phút chạy 1 lần
        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.MINUTES);
    }
    // dung sẻver
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}