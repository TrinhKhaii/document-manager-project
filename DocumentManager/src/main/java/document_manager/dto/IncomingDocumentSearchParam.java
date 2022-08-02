package document_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/*
    Created by KhaiTT
    Time: 13:34 7/13/2022
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncomingDocumentSearchParam {
    @Length(max = 255, message = "Trích yếu có tối đa 255 ký tự.")
    private String excerpt = "";

    @Length(max = 50, message = "Số hiệu có tối đa 50 ký tự.")
    private String serialNumber = "";

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "Sai định dạng, ngày tháng năm phải có định dạng yyyy-MM-dd HH:mm:ss")
    private String fromDate = "2000-01-01 00:00:00";

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "Sai định dạng, ngày tháng năm phải có định dạng yyyy-MM-dd HH:mm:ss")
    private String toDate = "3000-01-01 23:59:59";

    @Length(max = 50, message = "Tên người ký tối đa 50 ký tự.")
    private String signerName = "";

    @Min(value = 0, message = "Số trang phải là số dương.")
    private Long pageNumber = 0L;

    @Min(value = 1, message = "Số phần tử trong 1 trang có tối thiểu 1 phần tử.")
    private Long elementNumber = 3L;
}
