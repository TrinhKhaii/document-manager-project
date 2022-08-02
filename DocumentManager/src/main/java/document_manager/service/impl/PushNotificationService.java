package document_manager.service.impl;

import document_manager.dto.PushNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {
    private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);
    @Autowired
    private FCMService fcmService;

    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.pushNotification(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
