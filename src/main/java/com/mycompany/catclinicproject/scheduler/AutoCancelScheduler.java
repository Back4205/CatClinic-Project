package com.mycompany.catclinicproject.scheduler;

import com.mycompany.catclinicproject.dao.BookingDAO;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener   // annotation server tự động chạy class này khi app start

public class AutoCancelScheduler implements ServletContextListener { // override 2 hàm
    private ScheduledExecutorService scheduler;
    //object chay neen theo lich
    @Override
    public void contextInitialized(ServletContextEvent sce) { // run server chajy

        scheduler = Executors.newSingleThreadScheduledExecutor(); // tao hang doi cac tak . moi tak start
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
    public void contextDestroyed(ServletContextEvent sce) {  // stop
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}