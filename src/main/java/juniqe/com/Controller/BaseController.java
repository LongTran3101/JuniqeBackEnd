package juniqe.com.Controller;

import juniqe.com.model.DomainCode;
import juniqe.com.model.ResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;


abstract class BaseController implements Serializable {
    public <T> ResponseEntity<ResponseDTO<T>> success(T response) {
        return ResponseEntity.ok(ResponseDTO.success(response));
    }

    public <T> ResponseEntity<ResponseDTO<T>> success() {
        return ResponseEntity.ok(ResponseDTO.success());
    }

    public <T> ResponseEntity<ResponseDTO<T>> error(DomainCode status, String message) {
        return ResponseEntity.ok(ResponseDTO.error(status, message));
    }

    public <T> ResponseEntity<Page<T>> pageSuccess(Page<T> response) {
        return ResponseEntity.ok(response);
    }

}
