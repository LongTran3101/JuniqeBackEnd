package juniqe.com.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class  ResponseDTO<T> implements Serializable {
    private DomainCode code;
    private String message;
    private T content;

    public ResponseDTO(DomainCode code, String message, T content) {
        this.code = code;
        this.message = message;
        this.content = content;
    }

    public ResponseDTO(DomainCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseDTO(DomainCode code) {
        this.code = code;
    }

    public ResponseDTO() {
        this.code = DomainCode.SUCCESS;
    }

    public ResponseDTO(T content) {
        this.code = DomainCode.SUCCESS;
        this.content = content;
    }

    public static <T> ResponseDTO<T> error(DomainCode code, String message) {
        ResponseDTO<T> res = new ResponseDTO<>();
        res.setCode(code);
        res.setMessage(message);
        return res;
    }

    public static <T> ResponseDTO<T> error(DomainCode code, String message, T content) {
        ResponseDTO<T> res = new ResponseDTO<>();
        res.setCode(code);
        res.setMessage(message);
        res.setContent(content);
        return res;
    }

    public static <T> ResponseDTO<T> success(T data) {
        ResponseDTO<T> res = new ResponseDTO<>(DomainCode.SUCCESS);
        res.setContent(data);
        return res;
    }

    public static <T> ResponseDTO<T> success() {
        return new ResponseDTO<>(DomainCode.SUCCESS);
    }

    public static <T> ResponseDTO<T> success(T data, String message) {
        ResponseDTO<T> res = new ResponseDTO<>(DomainCode.SUCCESS);
        res.setContent(data);
        res.setMessage(message);
        return res;
    }
}
