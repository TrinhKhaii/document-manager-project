package document_manager.dto;
/*
    Created by KhaiTT
    Time: 15:06 7/8/2022
*/

import document_manager.annotation.IncomingDocumentId;
import document_manager.annotation.UserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RotationMultiRequest {
    private long id;

    @NotNull(message = "Văn bản đến không được để trống.")
    @IncomingDocumentId
    private Long incomingDocumentId;

    @NotNull(message = "Người gửi không được để trống.")
    @UserId
    private Long senderId;

    @NotNull(message = "Người nhận không được để trống.")
    private List<Long> receiverId;

    private Date deliveryDate;

    private boolean isDelete;
}
