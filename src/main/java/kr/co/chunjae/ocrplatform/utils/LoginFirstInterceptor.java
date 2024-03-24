package kr.co.chunjae.ocrplatform.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
public class LoginFirstInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestURI = request.getRequestURI();
        log.info("초기 비밀번호 수정 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession();

        if((Integer)session.getAttribute("loginFlag") == 0){
            log.info("초기 비밀번호 미수정 사용자 요청");
            response.sendRedirect("/member/passwordForm");
            return false;
        }


        return true;
    }

}
