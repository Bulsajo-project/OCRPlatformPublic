package kr.co.chunjae.ocrplatform.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {


  @GetMapping(value = "/")
  public String home(){
    return "/member/loginForm";
  }
}
