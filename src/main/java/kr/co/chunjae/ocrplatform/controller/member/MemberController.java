package kr.co.chunjae.ocrplatform.controller.member;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.chunjae.ocrplatform.dto.member.MemberDTO;
import kr.co.chunjae.ocrplatform.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

  private final MemberService memberService;

  // 로그인 페이지
  @GetMapping("/loginForm")
  public String loginForm(@RequestParam(required = false) String requestURI, Model model) {

    if(!StringUtils.isEmpty(requestURI)) { // 리다이렉션으로 requestURI을 요청한경우
      model.addAttribute("requestURI", requestURI);
    }
    return "member/loginForm";
  }

  // 로그인
  // 아이디 저장 - 쿠키
  @PostMapping("/login")
  public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session, Model model,
                      HttpServletRequest req, HttpServletResponse resp)
                      throws ServletException, IOException {

    MemberDTO loginResult = memberService.login(memberDTO);

    if (loginResult != null) {

      // login 성공
      session.setAttribute("loginId", loginResult.getLoginId()); // 회원의 아이디
      session.setAttribute("name", loginResult.getName()); // 회원 이름
      session.setAttribute("id", loginResult.getId()); // 회원 index
      session.setAttribute("role", loginResult.getRole()); // 권한
      session.setAttribute("loginFlag", loginResult.getLoginFlag()); // 최초 로그인 체크
      session.setMaxInactiveInterval(3600); // session 유효시간 설정 - 1시간

      memberService.loginLog(loginResult);
      return "redirect:/notice/list";

    } else {
      // login 실패
      model.addAttribute("errorMessage", "아이디/비밀번호를 다시 한 번 입력해주세요.");

      MemberDTO loginFail = memberService.loginFailed(memberDTO);

      if (loginFail != null && loginFail.getId() != null) {
        memberService.failLoginLog(loginFail);
      }
      return "member/loginForm";
    }
  }

  // 로그아웃
  @GetMapping("/logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "/member/loginForm";
  }

  // 비밀번호 수정 form
  @GetMapping("/passwordForm")
  public String passwordForm( HttpServletRequest request, Model model) throws Exception{

    return "member/passwordForm";
  }

  // 비밀번호 수정
  @PostMapping("/updatePw")
  @ResponseBody
  public String updatePassword(HttpSession session, @ModelAttribute MemberDTO memberDTO) throws Exception{

    memberService.updatePassword(memberDTO);
    return "OK";
  }

}