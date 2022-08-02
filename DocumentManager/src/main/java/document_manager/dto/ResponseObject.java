package document_manager.dto;
/*
    Created by KhaiTT
    Time: 15:48 7/7/2022
*/

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ResponseObject<T> {
    private boolean status;
    private String message;
    private Map<String, String> error;

    private List<T> data;

    public ResponseObject() {}

    public ResponseObject(boolean status, String message) {
        this.status = status;
        this.message = message;
    };

    public ResponseObject(boolean status, String message, Map<String, String> error) {
        this.status = status;
        this.message = message;
        this.error = error;
    };

    public ResponseObject(boolean status, String message, List<T> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    };
}
