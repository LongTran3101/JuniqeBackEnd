package juniqe.com.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DomainCode {
    SUCCESS("200", "SUCCESS"),
    CREATED("201", "CREATED"),
    BAD_REQUEST("400", "BAD_REQUEST"),
    UNAUTHORIZED("401", "UNAUTHORIZED"),
    FORBIDDEN("403", "FORBIDDEN"),
    NOT_FOUND("404", "NOT_FOUND"),
    INTERNAL_SERVER_ERROR("500", "INTERNAL_SERVER_ERROR");

    private String code;
    private String message;
}
