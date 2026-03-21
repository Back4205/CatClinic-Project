package com.mycompany.catclinicproject.scheduler;

import com.mycompany.catclinicproject.dao.BookingDAO;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class AutoCancelScheduler implements ServletContextListener {
    private ScheduledExecutorService scheduler;
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("Auto cancel checking...");
                BookingDAO dao = new BookingDAO();
                dao.autoCancelExpiredBookings();
                dao.cancelNoShowBookings();
            }
        };

        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.MINUTES);
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}