package gxj.study.demo.exceptionhandler;

import com.mysql.cj.exceptions.ConnectionIsClosedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpServletRequest;

/** assignableTypes 可以指定监听的类*/
@ControllerAdvice(assignableTypes = {ExceptionController.class})
@ResponseBody
public class GlobalExceptionHandler {

    /**
    * 异常从小到大捕获。
     * ExceptionHandlerMethodResolver.java中getMappedMethod方法会找到
     * 所有匹配的方法信息，然后按从小到大排序，取匹配度最小的那个方法。
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("errorCode","errorMessage");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleAppException(RuntimeException ex, HttpServletRequest request) {
        ErrorResponse representation = new ErrorResponse("ErrorCode", ex.getMessage());
        return new ResponseEntity<>(representation, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ConnectionIsClosedException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ConnectionIsClosedException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("errorCode","errorMessage");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}