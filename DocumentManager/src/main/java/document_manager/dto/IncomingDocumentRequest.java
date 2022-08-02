package document_manager.dto;

import document_manager.annotation.SerialNumber;
import document_manager.annotation.UserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/*
    Created by KhaiTT
    Time: 08:12 7/7/2022
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncomingDocumentRequest {
    private long id;

    @NotBlank(message = "Trích yếu không được để trống.")
    @Length(min = 3, message = "Trích yếu có tối thiểu 3 ký tự.")
    @Length(max = 255, message = "Trích yếu có tối đa 255 ký tự.")
    private String excerpt;

    @NotBlank(message = "Số hiệu không được để trống.")
    @Length(min = 3, message = "Số hiệu có tối thiểu 3 ký tự.")
    @Length(max = 50, message = "Số hiệu có tối đa 50 ký tự.")
    @SerialNumber
    private String serialNumber;

    private Date signingDate;

    @NotNull(message = "Người ký không được để trống.")
    @UserId
    private Long signerId;

    private boolean isDelete;
}
