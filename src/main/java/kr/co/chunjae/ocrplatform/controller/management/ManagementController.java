package kr.co.chunjae.ocrplatform.controller.management;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.co.chunjae.ocrplatform.dto.member.MemberDTO;
import kr.co.chunjae.ocrplatform.dto.member.PageDTO;
import kr.co.chunjae.ocrplatform.service.management.ManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/management")
@Slf4j
public class ManagementController {

    private final ManagementService managementService;


    //  계정 관리(회원 리스트)
    @GetMapping("/account")
    public String getAccountList(@RequestParam(value="id", required=false) Long id,
                                 @RequestParam(value = "page",required = false,defaultValue = "1") int page,
                                 @RequestParam(value = "searchType", defaultValue = "0", required = false) int searchType,
                                 @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
                                 Model model) {
        // 검색어 앞뒤 공백 제거
        keyword = keyword.trim();

        // 검색+페이징
        PageDTO pageDTO = managementService.initialize(id, page, searchType, keyword);
        model.addAttribute("pageDTO",pageDTO);

        // 계정 개수
        model.addAttribute("numOfAccounts", pageDTO.getNumOfAccounts());

        ArrayList<MemberDTO> pagingAccountList  = managementService.getAccountList(pageDTO);
        model.addAttribute("accountList", pagingAccountList);
        return "management/main";
    }


    /**
     * 사용자 계정 추가
     * @return
     */
    @PostMapping("/addAccount")
    @ResponseBody
    public void saveMember(@Valid @RequestBody MemberDTO memberDTO, HttpServletRequest request){
        request.getSession().getAttribute("id");
        managementService.saveMember(memberDTO ,request);
    }

    // 비밀번호 초기화
    @PostMapping("/resetPw")
    @ResponseBody
    public void resetPassword(@RequestBody MemberDTO memberDTO, HttpServletRequest request) {
        request.getSession().getAttribute("id");
        managementService.resetPassword(memberDTO, request);
    }

    // 사용자 계정 비활성화
    @PostMapping("/updateAccount")
    @ResponseBody
    public void updateMember(@RequestBody MemberDTO memberDTO, HttpServletRequest request){
        request.getSession().getAttribute("id");
        managementService.updateMember(memberDTO, request);
    }

    // 아이디 중복확인
    @PostMapping("/checkLoginId")
    @ResponseBody
    public String checkLoginId(@RequestParam("loginId") String loginId) {
        int checkResult = managementService.checkLoginId(loginId);
        if(checkResult == 0) {
            return "noredundancy";
        } else {
            return "redundancy";
        }
    }


}
