package document_manager.dto;
/*
    Created by KhaiTT
    Time: 14:27 7/6/2022
*/

import document_manager.annotation.Username;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersRequest {
    private long id;
    @NotBlank(message = "Tên không được để trống.")
    @Length(max = 50, message = "Tên không được vượt quá 50 ký tự.")
    @Username
    private String userName;

    private boolean isDelete;
    
}
