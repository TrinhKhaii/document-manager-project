package document_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

/*
    Created by KhaiTT
    Time: 08:11 7/20/2022
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RotationResponse {
    private Long id;
    private Long incomingDocumentId;
    private String incomingDocumentExcerpt;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
    private LocalDateTime deliveryDate;
}
