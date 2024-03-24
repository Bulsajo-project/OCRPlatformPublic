package kr.co.chunjae.ocrplatform.controller.common;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.chunjae.ocrplatform.utils.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalCatcher {

    private final MessageSource messageSource;

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleForbidden(ForbiddenException exception, Model model) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "/exception/forbiddenError";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        // 유효성 검사 실패에 대한 에러를 담을 맵 생성
        List<Map<String, String>> errors = new ArrayList<>();

        // 예외에서 필드 에러 추출
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.add(
                        Map.of(
                        "defaultMessage", error.getDefaultMessage(),
                        "field", error.getField()
                        )
                )
        );
        ErrorResponse errorResponse = new ErrorResponse(400, "validation failed", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<Object> handleBindException(
//            BindException e) {
//        final ErrorResponse errorResponse = ErrorResponse.of(e.getBindingResult());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//
//    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView catcher(Exception exception) {
        ModelAndView modelAndView = new ModelAndView("/exception/error");
        modelAndView.addObject("errorMessage", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({NullPointerException.class, FileNotFoundException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView catchers(Exception exception) {
        ModelAndView modelAndView = new ModelAndView("/exception/servererror");
        modelAndView.addObject("errorMessage", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNoHandlerFoundException(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/exception/notFound");
        String message = messageSource.getMessage("label.notFound.page", null, request.getLocale());
        modelAndView.addObject("errorMessage", message);
        return modelAndView;
    }
}
