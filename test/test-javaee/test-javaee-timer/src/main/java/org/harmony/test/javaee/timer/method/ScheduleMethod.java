package org.harmony.test.javaee.timer.method;

import java.util.Date;

import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ScheduleMethod {

    private static final Logger log = LoggerFactory.getLogger(ScheduleMethod.class);
    static int count = 0;
    Date start;

    @Schedule(hour = "*", minute = "*", second = "*/7")
    public void handle() {
        Date now = new Date();
        if (count == 0) {
            start = now;
            count++;
        }
        log.info("schedule method handle execute every 7 second, " + (now.getTime() - start.getTime()));
        start = now;
    }

    @Schedules({ //
            @Schedule(hour = "*", minute = "*", second = "*/5"), //
            @Schedule(hour = "*", minute = "*", second = "*/10") //
    })
    public void multiScheduleHandle() {
        log.info("one method with multi schedule execute 5/10 second");
    }

}
