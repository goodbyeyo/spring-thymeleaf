package spring.itemproject.exception.exhandler.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.AbstractController;
import spring.itemproject.exception.ex.UserException;
import spring.itemproject.exception.exhandler.ErrorResult;

// @ControllerAdvice 는 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler , @InitBinder 기능을 부여해주는 역할
// @ControllerAdvice 에 대상을 지정하지 않으면 모든 컨트롤러에 적용된다. (글로벌 적용)
// @RestControllerAdvice 는 @ControllerAdvice 와 같고, @ResponseBody 가 추가되어 있다.
// @Controller , @RestController 의 차이와 같다
// https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-anncontroller-advice
@Slf4j
@RestControllerAdvice   // @ControllerAdvice 와 같고, @ResponseBody 가 추가되어 있다.
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler   // (UserException.class) 생략해도 된다
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {  // 컨트롤러 호출하는것과 비슷
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    // RuneTimeException은 앞에서 해결불가능하기때문에 가장 넓은 Exception에서 처리
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }

    // 화면 에러를 처리할때 사용 (잘 사용하지 않음)
//    @ExceptionHandler(ViewException.class)
//    public ModelAndView ex(ViewException e) {
//        log.info("exception e", e);
//        return new ModelAndView("error");
//    }
}

// Target all Controllers annotated with @RestController
// @ControllerAdvice(annotations = RestController.class)
// public class ExampleAdvice1 { }
// Target all Controllers within specific packages
// @ControllerAdvice("org.example.controllers")
// public class ExampleAdvice2 { }
// Target all Controllers assignable to specific classes
// @ControllerAdvice(assignableTypes = {ControllerInterface.class, AbstractController.class})
// public class ExampleAdvice3 { }
