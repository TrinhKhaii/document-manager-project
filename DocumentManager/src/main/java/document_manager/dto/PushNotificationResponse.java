package document_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
    Created by KhaiTT
    Time: 14:58 7/12/2022
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PushNotificationResponse {
    private int status;
    private String message;
}
