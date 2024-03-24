package kr.co.chunjae.ocrplatform.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.chunjae.ocrplatform.utils.HTMLCharacterEscapes;
import kr.co.chunjae.ocrplatform.utils.LoginCheckInterceptor;
import kr.co.chunjae.ocrplatform.utils.LoginFirstInterceptor;
import kr.co.chunjae.ocrplatform.utils.XssEscapeServletFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
@AllArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    /*
    필터 설정
     */
    @Bean
    public FilterRegistrationBean<XssEscapeServletFilter> filterRegistrationBean() {
        FilterRegistrationBean<XssEscapeServletFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new XssEscapeServletFilter());
        filterRegistration.setOrder(1);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        ObjectMapper copy = objectMapper.copy();
        copy.getFactory().setCharacterEscapes(new HTMLCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(copy);
    }


    /*
    인터셉터 설정
     */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/member/loginForm", "/member/login", "/member/logout",
                        "/static/**", "/*.ico", "/error");
        registry.addInterceptor(new LoginFirstInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/member/loginForm", "/member/login", "/member/logout", "/member/passwordForm", "/member/updatePw",
                        "/static/**", "/*.ico", "/error");
    }


}
