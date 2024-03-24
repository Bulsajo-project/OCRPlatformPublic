package kr.co.chunjae.ocrplatform.controller.notice;

import jakarta.servlet.http.HttpSession;
import kr.co.chunjae.ocrplatform.dto.notice.NoticeDTO;
import kr.co.chunjae.ocrplatform.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.CtBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

  private final NoticeService noticeService;
  private static final Logger log = LoggerFactory.getLogger(NoticeController.class);

  // 공지사항 리스트
  @GetMapping("/list")
  public String getNoticeList(Model model, HttpSession session, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "1") int page) {
    int totalCount = noticeService.getTotalCount(); // 전체 게시글 수
    int totalPages = (int) Math.ceil((double) totalCount / pageSize);
    int start = (page - 1) * pageSize;

    log.info("***** Start: " + start + "   PageSize: " + pageSize);

    List<NoticeDTO> noticeList = noticeService.getNoticeList(start, pageSize);

    log.info("***** NoticeList: " + noticeList);

    // 세션에서 사용자의 권한 정보를 가져옴
    Object userRole = session.getAttribute("role");

    model.addAttribute("noticeList", noticeList);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPostCount", totalCount);
    model.addAttribute("userRole", userRole);

    log.info("공지사항 목록 조회");
    return "notice/list";
  }

  // 공지사항 상세조회
  @GetMapping("/detail/{id}")
  public String getNotice(@PathVariable("id") Long id, Model model, HttpSession session) {
    HashMap<String, Object> noticeDTO = noticeService.getNotice(id);
    // 세션에서 사용자 권한 가져오기
    Object userRole = session.getAttribute("role");
    model.addAttribute("noticeDetail", noticeDTO);
    // 모델에 사용자 권한 추가
    model.addAttribute("userRole", userRole);


    return "notice/detail";
  }

  // 공지사항 수정 폼
  @GetMapping("/update/{id}")
  public String updateNoticeForm(@PathVariable("id") Long id, Model model, HttpSession session) {
    // session에서 userRole 가져오기
    Object userRole = session.getAttribute("role");

    // 모델에 userRole 추가
    model.addAttribute("userRole", userRole);
    model.addAttribute("noticeDetail", noticeService.getNotice(id));
    return "notice/update";
  }

  //공지사항 수정
  @PostMapping("/update/{id}")
  public String updateNotice(@PathVariable("id") Long id, NoticeDTO noticeDTO, HttpSession session) {

    try {
      noticeDTO.setId(id);
      noticeService.updateNotice(noticeDTO);
      return "redirect:/notice/list";
    } catch (Exception e) {
      log.error("공지사항 업데이트 실패", e);
      return "error-page";
    }
  }

  // 공지사항 작성 폼
  @GetMapping("/write")
  public String writeNoticeForm() {
    return "notice/write";
  }

  // 공지사항 작성
  @PostMapping("/write")
  public String writeNotice(NoticeDTO noticeDTO, HttpSession httpSession) {
    try {
      // 작성자 정보 설정
      noticeDTO.setCreator((Long) httpSession.getAttribute("id"));

      // 공지사항 등록
      noticeService.writeNotice(noticeDTO);

      // 등록 성공 시 list로 이동
      return "redirect:/notice/list";
    } catch (Exception e) {
      log.error("공지사항 작성 실패", e);
      return "error-page";
    }
  }

  // 공지사항 삭제
  @PostMapping("/delete/{id}")
  public String deleteNotice(@PathVariable("id") Long id, Model model, HttpSession session) {
    Object userRole = session.getAttribute("role");
    model.addAttribute("userRole", userRole);
    noticeService.deleteNotice(id);
    return "redirect:/notice/list";
  }

  // 공지사항 검색
  @GetMapping("/search")
  public String searchNotice(@RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam(value = "condition", defaultValue = "all") String condition,
                             @RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                             Model model) {
    int start = (page - 1) * pageSize;
    int totalCount = noticeService.getSearchCount(keyword, condition);
    List<Map<String, Object>> notices = noticeService.searchNotice(keyword, condition, start, pageSize);

    model.addAttribute("noticeList", notices);
    model.addAttribute("total", totalCount);
    model.addAttribute("keyword", keyword);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", (int) Math.ceil((double) totalCount / pageSize));

    return "notice/list";
  }
}