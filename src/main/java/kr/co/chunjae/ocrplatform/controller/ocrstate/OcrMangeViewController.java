package kr.co.chunjae.ocrplatform.controller.ocrstate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/ocrManage")
public class OcrMangeViewController {
    @GetMapping("/progressList")
    public String ocrProgress(){
        return "/ocrstate/ocrProgressList";
    }

    @GetMapping("/manageList")
    public String ocrMange(){
        return "/ocrstate/ocrMangeList";
    }

}
