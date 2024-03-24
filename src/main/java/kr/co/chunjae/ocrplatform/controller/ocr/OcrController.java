package kr.co.chunjae.ocrplatform.controller.ocr;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.co.chunjae.ocrplatform.dto.ocr.OcrPaperViewDTO;
import kr.co.chunjae.ocrplatform.dto.ocr.ProgressRequestDTO;
import kr.co.chunjae.ocrplatform.dto.ocr.ProgressResponseDTO;
import kr.co.chunjae.ocrplatform.dto.ocr.EditOcrTextRequsetDTO;
import kr.co.chunjae.ocrplatform.service.ocr.OcrService;
import kr.co.chunjae.ocrplatform.utils.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
@Slf4j
public class OcrController {

  private final OcrService ocrService;


  @GetMapping("/ocrPaperView")
  public String ocrPaperView(@RequestParam Long paperId, Model model, HttpServletRequest request){
    //권한 체크
    ocrService.checkMember(paperId, request);

    OcrPaperViewDTO ocrPaperViewDTO = ocrService.ocrPaperView(paperId);
    String pdfFullPath = ocrService.getPdfPath(paperId);
    int itemCount = ocrService.countItem(paperId);

    model.addAttribute("paper", ocrPaperViewDTO); // 뷰로 보낼 데이터 값
    model.addAttribute("pdf", pdfFullPath);
    model.addAttribute("itemCount", itemCount);
    return "/ocr/ocrProgress";
  }

  @PostMapping("/ocrItemView")
  @ResponseBody
  public Map<String, Object> ocrItemView(@RequestBody Map<String, String> params){
    Map<String, Object> response = ocrService.ocrItemView(params);

    return response;
  }

  @PostMapping ("/ocrProgress")
  @ResponseBody
  public ProgressResponseDTO progress(@ModelAttribute ProgressRequestDTO progressRequestDTO, HttpServletRequest request)
          throws IOException, MethodArgumentNotValidException {
    ProgressResponseDTO response = ocrService.progress(progressRequestDTO, request);
    return response;
  }

  @PostMapping ("/ocrTextEdit")
  @ResponseBody
  public Map<String, Object> editOcrText(@Valid @RequestBody EditOcrTextRequsetDTO editOcrTextRequsetDTO,
                                         HttpServletRequest request) {

    Map<String, Object> response = ocrService.editOcrText(editOcrTextRequsetDTO, request);
    return response;
  }










}