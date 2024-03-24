package kr.co.chunjae.ocrplatform.controller.paper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/paper")
public class PaperViewController {

    @GetMapping("/paperBoard")
    public String paperBoardPage(){
        return "/paperManage/listPage";
    }
}
