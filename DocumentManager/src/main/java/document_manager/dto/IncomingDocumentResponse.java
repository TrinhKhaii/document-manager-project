package document_manager.dto;
/*
    Created by KhaiTT
    Time: 08:03 7/20/2022
*/
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncomingDocumentResponse {
    private Long id;
    private String excerpt;
    private String serialNumber;
    private LocalDateTime signingDate;
    private Long signerId;
    private String signerName;
}
