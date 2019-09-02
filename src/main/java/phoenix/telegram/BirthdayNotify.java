package phoenix.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import phoenix.entities.Birthday;
import phoenix.services.BirthdayService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class BirthdayNotify {

    @Value("${notification.period}")
    private Integer period;

    @Value("${notification.timeUnit}")
    private TimeUnit timeUnit;

    private ScheduledExecutorService service;
    private HappyBirthdayBot happyBirthdayBot;
    private BirthdayService birthdayService;
    private MessageConcat messageConcat;

//    public BirthdayNotify(HappyBirthdayBot happyBdayBot, BirthdayService bdayService, MessageConcat msg,
//                          @Value("${notification.period}") Integer period, @Value("${notification.timeUnit}") TimeUnit timeUnit) {
//        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
//        Runnable runnable = () -> {
//            List<Birthday> list = bdayService.selectByDate(LocalDate.now());
//            for (Birthday e : list) {
//                try {
//                    happyBdayBot.birthdayNotification(msg.birthdayAndTemplateConcat(e.toString()));
//                } catch (IllegalArgumentException c) {
//                    happyBdayBot.birthdayNotification("No templates found");
//                }
//            }
//        };
//        try {
//            service.scheduleAtFixedRate(runnable, 0, period,
//                    timeUnit);
//        } catch (NumberFormatException e) {
//            System.out.println("incorrect period format");
//        } catch (IllegalArgumentException e) {
//            System.out.println("incorrect TimeUnit format");
//        }
//    }

    public BirthdayNotify (HappyBirthdayBot happyBdayBot, BirthdayService bdayService, MessageConcat msg) {
        service = Executors.newScheduledThreadPool(1);
        happyBirthdayBot=happyBdayBot;
        birthdayService=bdayService;
        messageConcat=msg;
    }

    @PostConstruct
    public void init(){
        Runnable runnable = () -> {
            List<Birthday> list = birthdayService.selectByDate(LocalDate.now());
            for (Birthday birthdays : list) {
                try {
                    happyBirthdayBot.birthdayNotification(messageConcat.birthdayAndTemplateConcat(birthdays.toString()));
                } catch (IllegalArgumentException c) {
                    happyBirthdayBot.birthdayNotification("No templates found");
                }
            }
        };
        try {
            service.scheduleAtFixedRate(runnable, 0, period,timeUnit);
        } catch (NumberFormatException e) {
            System.out.println("incorrect period format");
        } catch (IllegalArgumentException e) {
            System.out.println("incorrect TimeUnit format");
        }
    }
}
