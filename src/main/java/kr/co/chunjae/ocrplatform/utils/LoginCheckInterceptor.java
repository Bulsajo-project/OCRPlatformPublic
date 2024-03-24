package kr.co.chunjae.ocrplatform.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        /**
         * filter는 인증이 필요없는 resource의 경우,
         * String[] whiteList = {"/", "/members/add", "/login", "/logout", "/css/*"}를 사용하여 처리했지만,
         * interceptor는 @Configuration으로 설정 가능
         */
        HttpSession session = request.getSession();
        if (isEmpty(session) || isEmpty(session.getAttribute("id"))) {
            log.info("미인증 사용자 요청");
//            response.sendRedirect("/member/loginForm?requestURI=" + requestURI); 코드수정필요, 로그인 모듈 담당자와 상의
            response.sendRedirect("/member/loginForm");
            return false;
        }


        return true;
    }

}
