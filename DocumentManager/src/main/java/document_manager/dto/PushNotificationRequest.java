package document_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PushNotificationRequest {
    private String to;
    private String title;
    private String message;
    private String topic;
    private String token;
    private String image;
}