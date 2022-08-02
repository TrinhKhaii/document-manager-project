package document_manager.dto;
/*
    Created by KhaiTT
    Time: 13:27 7/13/2022
*/

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePageObject<T> {
    private boolean status;
    private String message;
    private List<T> data;
    private Long page;
    private Long totalElement;
    private Long numberOfElementPerPage;
}
