package document_manager.service.impl;
/*
    Created by KhaiTT
    Time: 09:35 7/11/2022
*/

import com.google.firebase.messaging.*;
import document_manager.dto.PushNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class FCMService {

    public String pushNotification(PushNotificationRequest request) {
        Message message = Message.builder()
                .setToken(request.getTo())
                .setNotification(Notification.builder().setBody(request.getTo()).setBody(request.getMessage())
                        .setTitle(request.getTitle()).setImage(request.getImage()).build())
                .build();

        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return response;
    }
}
