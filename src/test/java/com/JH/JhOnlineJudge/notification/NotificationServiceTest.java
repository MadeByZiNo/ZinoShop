package com.JH.JhOnlineJudge.notification;


import com.JH.JhOnlineJudge.notification.domain.NotificationFrom;
import com.JH.JhOnlineJudge.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

   /* @Test
    void getNotificationStatus() {

        Long trueId = 1L;
        Long falseId = 2L;

        NotificationStatusDto trueStatus = notificationService.getNotificationStatus(trueId);
        NotificationStatusDto falseStatus = notificationService.getNotificationStatus(falseId);

        System.out.println(trueStatus.getCount() + "   " + trueStatus.isExists());
        System.out.println(falseStatus.getCount() + "   " + trueStatus.isExists());

        Assertions.assertThat(trueStatus.getCount()).isEqualTo(5);
        Assertions.assertThat(trueStatus.isExists()).isTrue();

        Assertions.assertThat(falseStatus.getCount()).isEqualTo(null);
        Assertions.assertThat(falseStatus.isExists()).isFalse();
    }*/

    @Test
    public void NotificationMessageTest() {
        Long id = 1L;
        notificationService.sendNotificationMessage(id,"test4", NotificationFrom.배송);
        notificationService.sendNotificationMessage(id,"test5",NotificationFrom.배송);
        notificationService.sendNotificationMessage(id,"test6",NotificationFrom.문의);

      }


}