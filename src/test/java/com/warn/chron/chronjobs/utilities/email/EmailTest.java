package com.warn.chron.chronjobs.utilities.email;


import com.warn.chron.chronjobs.business.notification.NotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EmailTest {

    @Autowired
    NotificationService notificationService;

    @Test
    public void testSendEmail() {

        String to = "diopa@warn.com";
        String subject = "Test Email - Test Subject";
        String body = "Test Email - Test Body";

        assertTrue(notificationService.sendEmail(to, subject, body));

    }

    @Test
    public void testSendAuthenticatedEmail() {

        String to = "diopa@warn.com";
        String subject = "Test Email - Test Subject";
        String body = "Test Email - Test Body";


        assertTrue(notificationService.sendAuthenticatedEmail(to, subject, body));
    }
}
