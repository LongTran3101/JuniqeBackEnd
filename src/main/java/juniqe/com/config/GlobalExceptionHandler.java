package juniqe.com.config;

import juniqe.com.model.DomainCode;
import juniqe.com.model.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<?>> handleValidationException(MethodArgumentNotValidException ex) {
        // Lấy danh sách lỗi từng field
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        // Gom message từng lỗi thành chuỗi: "field1: lỗi1; field2: lỗi2; ..."
        String errorMessages = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ResponseDTO<?> response = ResponseDTO.error(DomainCode.BAD_REQUEST, errorMessages);
        return ResponseEntity.badRequest().body(response);
    }

}
