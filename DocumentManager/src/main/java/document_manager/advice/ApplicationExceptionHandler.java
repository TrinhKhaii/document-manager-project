package document_manager.advice;

import document_manager.dto.ResponseObject;
import document_manager.exception.IncomingDocumentNotFoundException;
import document_manager.exception.RotationNotFoundException;
import document_manager.exception.UserNotFoundException;
import document_manager.exception.WrongFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

/*
    Created by KhaiTT
    Time: 14:24 7/6/2022
*/
@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public <T> ResponseObject<T> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return new ResponseObject<T>(false, "Lỗi định dạng các trường.", errorMap);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UserNotFoundException.class)
    public <T> ResponseObject<T> handleBusinessException(UserNotFoundException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", ex.getMessage());
        return new ResponseObject<T>(false, "Lỗi dữ liệu.", errorMap);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IncomingDocumentNotFoundException.class)
    public <T> ResponseObject<T> handleBusinessException(IncomingDocumentNotFoundException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", ex.getMessage());
        return new ResponseObject<T>(false, "Lỗi dữ liệu.", errorMap);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RotationNotFoundException.class)
    public <T> ResponseObject<T> handleBusinessException(RotationNotFoundException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", ex.getMessage());
        return new ResponseObject<T>(false, "Lỗi dữ liệu.", errorMap);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(WrongFormatException.class)
    public <T> ResponseObject<T> handleBusinessException(WrongFormatException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", ex.getMessage());
        return new ResponseObject<T>(false, "Lỗi định dạng.", errorMap);
    }
}
