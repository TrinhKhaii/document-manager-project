package document_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;

/*
    Created by KhaiTT
    Time: 13:22 7/13/2022
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchParam {
    @Length(max = 50, message = "Tên người ký tối đa 50 ký tự.")
    private String name = "";

    @Min(value = 0, message = "Số trang phải là số dương.")
    private Long pageNumber = 0L;

    @Min(value = 1, message = "Số phần tử trong 1 trang có tối thiểu 1 phần tử.")
    private Long elementNumber = 3L;
}
