package document_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
    Created by KhaiTT
    Time: 14:01 7/7/2022
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RotationListOfDocumentResponse {
    private Long id;
    private String excerpt;
    private String serialNumber;
    private String signingDate;
    private String deliveryDate;
    private String sender;
    private String receiver;
    private String signer;
}
