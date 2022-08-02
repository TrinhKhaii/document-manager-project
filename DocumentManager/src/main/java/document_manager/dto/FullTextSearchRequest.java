package document_manager.dto;
/*
    Created by KhaiTT
    Time: 08:20 7/29/2022
*/

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullTextSearchRequest {
    @Length(max = 100, message = "Trường tìm kiếm co tối đa 100 ký tự.")
    private String searchParam = "";

    @Min(value = 0, message = "Số trang phải là số dương.")
    private Long pageNumber = 0L;

    @Min(value = 1, message = "Số phần tử trong 1 trang có tối thiểu 1 phần tử.")
    private Long elementNumber = 3L;
}
