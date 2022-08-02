package document_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
    Created by KhaiTT
    Time: 14:54 7/7/2022
*/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentAmountReceivedResponse {
    private String receiverName;
    private Long documentAmount;
}
