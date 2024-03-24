package kr.co.chunjae.ocrplatform.controller.upload;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.chunjae.ocrplatform.dto.upload.*;
import kr.co.chunjae.ocrplatform.service.upload.UploadPaperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/uploadPaper")
@Slf4j
public class UploadPaperController {

  private final UploadPaperService uploadPaperService;

  @GetMapping(value = "")
  public String uploadForm(@ModelAttribute UploadPaperDTO uploadPaperDTO) {

    return "/upload/uploadPaper";

  }

  @PostMapping(value = "/savePaper")
  public String savePaper(@Validated @ModelAttribute UploadPaperDTO uploadPaperDTO, UploadPDFDTO uploadPDFDTO, UploadItemDTO uploadItemDTO,
                          Model model, @RequestParam(value = "PDFFile") MultipartFile multipartFile, HttpServletRequest request) throws IOException {


    Long id = (Long) request.getSession().getAttribute("id");

    uploadPaperDTO.setCreator(id);
    uploadPDFDTO.setCreator(id);
    uploadItemDTO.setCreator(id);
    uploadPaperService.savePaper(uploadPaperDTO, uploadPDFDTO, multipartFile);
    uploadPaperService.saveItem(uploadPaperDTO,uploadItemDTO);

    model.addAttribute("uploadDTO", uploadPaperDTO);

    return "/upload/uploadPaper";
  }
  @GetMapping(value = "/update")
  public String updateForm(@ModelAttribute UploadPaperDTO uploadPaperDTO, UploadPDFDTO uploadPDFDTO, Model model) throws IOException  {

    UploadPaperDTO infoPDF =   uploadPaperService.infoPDF(uploadPaperDTO);
    UploadPDFDTO infoPDFFile = uploadPaperService.infoPDFFile(uploadPDFDTO);
    UploadSubjectDTO infoSubject = uploadPaperService.infoSubject(infoPDF);

    UploadPaperDTO check = uploadPaperService.infoStatus(uploadPaperDTO);

    model.addAttribute("check",check.getStatus());
    model.addAttribute("infoPDF",infoPDF);
    model.addAttribute("infoPDFFile",infoPDFFile);
    model.addAttribute("infoSubject",infoSubject);

    return "/upload/updatePaper";

  }

  @PostMapping(value = "/update")
  public String updatePaper(@Validated @ModelAttribute UploadPaperDTO uploadPaperDTO, UploadPDFDTO uploadPDFDTO,
                            UploadRequestDTO requestDTO , UploadItemDTO uploadItemDTO,
                            Model model, @RequestParam(value = "PDFFile") MultipartFile multipartFile, HttpServletRequest request) throws IOException {


    Long id = (Long) request.getSession().getAttribute("id");

    uploadPaperDTO.setCreator(id);
    uploadPDFDTO.setCreator(id);
    uploadPaperDTO.setUpdater(id);
    uploadPDFDTO.setUpdater(id);


    uploadPaperService.updatePaper(uploadPaperDTO, uploadPDFDTO, multipartFile);
    model.addAttribute("uploadDTO", uploadPaperDTO);
    model.addAttribute("originalFile",uploadPDFDTO.getOriginalFile());


    uploadPaperService.infoStatus(uploadPaperDTO);
    UploadPaperDTO check = uploadPaperService.infoStatus(uploadPaperDTO);

    model.addAttribute("ckeck",check);

    if (check.getStatus() == 0){
      requestDTO.setPaperId(uploadPaperDTO.getId());
      uploadItemDTO.setPaperId(uploadPaperDTO.getId());

      uploadItemDTO.setCreator(id);

      uploadPaperService.deleteItem(requestDTO);
      uploadPaperService.saveItem(uploadPaperDTO,uploadItemDTO);

    }

    return "/upload/uploadPaper";

  }

  @GetMapping(value = "/getFile")
  public ResponseEntity<byte[]> getFile(@RequestParam("uploadFile") String uploadFile,
                                        UploadPDFDTO uploadPDFDTO) throws IOException {

    // 파일명 URL 디코딩
    String decodedFileName = URLDecoder.decode(uploadFile, StandardCharsets.UTF_8);

    // 서비스를 통해 해당 경로에서 파일 데이터를 가져오기
    byte[] fileData = uploadPaperService.getFileData(uploadPDFDTO, decodedFileName);

    // 가져온 파일 데이터를 클라이언트에게 전송
    HttpHeaders headers = new HttpHeaders();

    // 파일명을 UTF-8로 인코딩하여 헤더에 추가
    String encodedFileName = URLEncoder.encode(decodedFileName, StandardCharsets.UTF_8);
    headers.setContentDispositionFormData("attachment", encodedFileName);

    // 캐시 제어 헤더 설정
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

    // ResponseEntity 생성
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(fileData, headers, HttpStatus.OK);

    log.info("Decoded file name: {}", decodedFileName);
    log.info("Encoded file name: {}", encodedFileName);
    log.info("Content-Disposition header: {}", headers.getContentDisposition());
    log.info("Cache-Control header: {}", headers.getCacheControl());
    log.info("File data length: {}", fileData.length);

    return responseEntity;
  }
  @PostMapping(value = "/deletePaper")
  public ResponseEntity<String> deletePaper(@Validated @RequestBody
                                            UploadRequestDTO requestDTO
                                          ) throws IOException {

    if (requestDTO.getCheck() == 0) {

      uploadPaperService.deleteItem(requestDTO);
      uploadPaperService.deletePDF(requestDTO);
      uploadPaperService.deletePaper(requestDTO);

      return ResponseEntity.ok().body("ok");
    }
    return ResponseEntity.ok().body("no");
  }

}