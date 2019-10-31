package com.warn.chron.chronjobs.business.notification;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.application.common.RestServiceConfig;
import com.warn.chron.chronjobs.job.pdm.ecns.entities.EcnSummary;
import com.warn.chron.chronjobs.persistence.entity.Notification;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    private ApplicationEnvironmentConfig environmentConfig;

    @Autowired
    private NotificationServiceTemplate notificationServiceTemplate;

    public Boolean sendEmail(String to, String subject, String body) {
        return notificationServiceTemplate.sendEmail(to, subject, body);
    }

    public Boolean sendAuthenticatedEmail(String to, String subject, String body) {
        return notificationServiceTemplate.sendAuthenticatedEmail(to, subject, body);
    }

    public String generateNotificationBody(String template, Map<String, Object> input) {

        String generatedCode = null;

        try {

            Configuration cfg = new Configuration();
            cfg.setClassForTemplateLoading(NotificationService.class, "/templates/");
            freemarker.template.Template freemarkerTemplate = cfg.getTemplate(template);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(out);
            freemarkerTemplate.process(input, writer);

            generatedCode = out.toString("UTF-8");

        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }

        return generatedCode;

    }

    @Component
    public class NotificationServiceTemplate {

        private RestTemplate restTemplate;

        public NotificationServiceTemplate() {
            this.restTemplate = new RestTemplate();
        }

        public Boolean sendEmail(String to, String subject, String body) {

            Notification notification = Notification.builder()
                    .from(Notification.FROM)
                    .to((environmentConfig.getNotificationRestService().getRecipientTestEmail().isEmpty() ? to : environmentConfig.getNotificationRestService().getRecipientTestEmail()))
                    .cc(environmentConfig.getNotificationRestService().getRecipientCCTestEmailList())
                    .subject(subject)
                    .body(body)
                    .type(Notification.Type.HTML)
                    .build();

            return sendEmail(notification);

        }

        public Boolean sendEmail(Notification notification) {

            notification.setSubject(environmentConfig.getNotificationRestService().getSubject() + " " + notification.getSubject());

            try {
                log.info("an email was sent {}", notification.toString());
                String uri = getUri().toUriString();
                ResponseEntity<Boolean> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, getHttpEntity(notification), Boolean.class);
                return responseEntity.getBody();
            } catch (Exception e) {
                log.error("An error has occurred while trying to send the notification", e);
            }

            return Boolean.FALSE;
        }

        public Boolean sendAuthenticatedEmail (String to, String subject, String body) {

            Notification notification = Notification.builder()
                    .from(Notification.AUTHENTICATED_FROM)
                    .to((environmentConfig.getNotificationRestService().getRecipientTestEmail().isEmpty() ? to : environmentConfig.getNotificationRestService().getRecipientTestEmail()))
                    .cc(environmentConfig.getNotificationAuthenticatedRestService().getRecipientCCTestEmailList())
                    .subject(subject)
                    .body(body)
                    .type(Notification.Type.HTML)
                    .build();

            return sendAuthenticatedEmail(notification);
        }

        public Boolean sendAuthenticatedEmail (Notification notification) {
            notification.setSubject(environmentConfig.getNotificationAuthenticatedRestService().getSubject() + " " + notification.getSubject());

            try {
                log.info("an email was sent {}", notification.toString());
                String uri = getAuthenticatedUri().toUriString();
                ResponseEntity<Boolean> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, getHttpEntity(notification), Boolean.class);
                return responseEntity.getBody();
            } catch (Exception e) {
                log.error("An error has occurred while trying to send the notification", e);
            }

            return Boolean.FALSE;
        }

        private UriComponentsBuilder getUri() {

            return UriComponentsBuilder.newInstance()
                    .scheme(environmentConfig.getNotificationRestService().getProtocol())
                    .host(environmentConfig.getNotificationRestService().getHostName())
                    .port(environmentConfig.getNotificationRestService().getPort())
                    .path(environmentConfig.getNotificationRestService().getUrlPrefix());
        }

        private UriComponentsBuilder getAuthenticatedUri() {

            return UriComponentsBuilder.newInstance()
                    .scheme(environmentConfig.getNotificationAuthenticatedRestService().getProtocol())
                    .host(environmentConfig.getNotificationAuthenticatedRestService().getHostName())
                    .port(environmentConfig.getNotificationAuthenticatedRestService().getPort())
                    .path(environmentConfig.getNotificationAuthenticatedRestService().getUrlPrefix());
        }

        private <T> HttpEntity<T> getHttpEntity(T body) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("key", environmentConfig.getNotificationRestService().getKey());
            return new HttpEntity<T>(body, headers);
        }

    }

}
