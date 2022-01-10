package spring.itemproject;

import org.springframework.boot.autoconfigure.web.embedded.JettyWebServerFactoryCustomizer;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

// runTimeException이 Was에 전달되거나 response.sendError() 이 호출되면 위에 등록한 예외 페이지 경로가 호출된다.
// @Component   // 스프링 부트가 제공하는 기본 오류 메커니즘을 사용하도록 주석처리함
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        // 오류 페이지는 예외를 다룰때 해당 예외와 그 자식 타입의 오류를 함께 처리한다
        // 즉 RuntimeException은 물론 RuntimeException의 자식도 함께 처리한다
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}
