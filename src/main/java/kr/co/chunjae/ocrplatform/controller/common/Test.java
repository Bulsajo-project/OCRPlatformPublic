package kr.co.chunjae.ocrplatform.controller.common;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

public class Test {
    @GetMapping("/test")
    public String loginForm() {
        return "index";
    }
}
