package gxj.study.demo.exceptionhandler;

import lombok.Data;

@Data
public class ErrorResponse {

    private String message;
    private String errorTypeName;
  
    public ErrorResponse(Exception e) {
        this(e.getClass().getName(), e.getMessage());
    }

    public ErrorResponse(String errorTypeName, String message) {
        this.errorTypeName = errorTypeName;
        this.message = message;
    }
}