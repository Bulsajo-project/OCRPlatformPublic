package kr.co.chunjae.ocrplatform.controller.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class ErrorResponse {

    private int status;

    private String description;

    private List<Map<String, String>> errors;

    public ErrorResponse(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public ErrorResponse(int status, String description, List<Map<String, String>> errors) {
        this.status = status;
        this.description = description;
        this.errors = errors;
    }
}