package com.iamnbty.training.backend.schedule;

import com.iamnbty.training.backend.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserSchedule {

    private final UserService userService;

    public UserSchedule(UserService userService) {
        this.userService = userService;
    }

    // Schedule Note
    // 1 => second
    // 2 => minute
    // 3 => hour
    // 4 => day
    // 5 => month
    // 6 => year

    /**
     * Every minute (UTC Time)
     */
    @Scheduled(cron = "0 * * * * *")
    public void testEveryMinute() {
        log.info("Hello, What's up?");
    }

    /**
     * Everyday at 00:00 (UTC Time)
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void testEveryMidNight() {

    }

    /**
     * Everyday at 10:50 AM (Thai Time)
     */
    @Scheduled(cron = "0 50 10 * * *", zone = "Asia/Bangkok")
    public void testEverydayNineAM() {
        log.info("Hey Hoo!!!");
    }

}
