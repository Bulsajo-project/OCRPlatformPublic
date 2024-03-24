package kr.co.chunjae.ocrplatform.service.upload;

import kr.co.chunjae.ocrplatform.dto.upload.*;
import kr.co.chunjae.ocrplatform.mapper.upload.UploadPaperMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class UploadPaperService {

  private final UploadPaperMapper uploadPaperMapper;

  @Value("${custom.path.upload}")
  private String uploadPath;

  // 파일 데이터를 가져오는 메서드
  public byte[] getFileData(UploadPDFDTO uploadPDFDTO, String fileName) throws IOException {
    // 파일 경로에서 파일 데이터를 읽어옴
    String getPath = uploadPDFDTO.getPath();
    String fullPath = getPath.startsWith(uploadPath) ? getPath : Paths.get(uploadPath, getPath).toString();

    File file = new File(fullPath, fileName);

    return Files.readAllBytes(file.toPath());
  }
  private String getDynamicFolderPath() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/HH_mm_ss");
    String timestamp = dateFormat.format(new Date());
    return Paths.get(uploadPath, "pdf", timestamp).toString().replace("\\", "/");
  }
  public boolean savePaper(UploadPaperDTO uploadPaperDTO, UploadPDFDTO uploadPDFDTO, MultipartFile multipartFile) throws IOException {
    // 폼 데이터를 데이터베이스에 저장
    uploadPaperMapper.savePaper(uploadPaperDTO);

    // 디렉토리 내부에 파일 경로 생성
    String resultPath = getDynamicFolderPath();
    String fileName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

    try {
      String filePath = saveFile(multipartFile.getBytes(), resultPath, fileName);
      String path = filePath.replaceFirst(".*?/upload", "").replaceFirst(fileName, "");

      // PDF 파일 정보 저장
      uploadPDFDTO.setPaperId(uploadPaperDTO.getId());
      uploadPDFDTO.setPath(path);
      uploadPDFDTO.setUploadFile(fileName);
      uploadPDFDTO.setOriginalFile(multipartFile.getOriginalFilename());

      uploadPaperMapper.savePDF(uploadPDFDTO);

      return true;
    } catch (IOException e) {
      log.error("파일 저장 중 오류 발생: {}", e.getMessage());
      return false;
    }
  }


  public boolean saveItem(UploadPaperDTO uploadPaperDTO, UploadItemDTO uploadItemDTO){

    int count = uploadPaperDTO.getItemCnt();
    uploadItemDTO.setPaperId(uploadPaperDTO.getId());
    for (int i = 0; i < count; i++) {
      uploadItemDTO.setItemOrder(i+1);
      uploadPaperMapper.saveItem(uploadItemDTO);
    }

    return false;
  }
  public String saveFile(byte[] fileData, String directory, String fileName) {
    try {
      String filePath = directory + File.separator + fileName;
      File file = new File(filePath);
      org.apache.commons.io.FileUtils.writeByteArrayToFile(file, fileData);
      return filePath;
    } catch (IOException e) {
      log.error("파일 저장 중 오류 발생: {}", e.getMessage());
      throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
    }
  }
  public UploadSubjectDTO infoSubject(UploadPaperDTO uploadPaperDTO){

    return uploadPaperMapper.infoSubject(uploadPaperDTO);
  }


  public UploadPaperDTO infoPDF(UploadPaperDTO uploadPaperDTO) {
    return uploadPaperMapper.infoPDF(uploadPaperDTO);
  }

  public UploadPDFDTO infoPDFFile(UploadPDFDTO uploadPDFDTO) {
    return uploadPaperMapper.infoPDFFile(uploadPDFDTO);
  }

  public UploadPaperDTO infoStatus(UploadPaperDTO uploadPaperDTO){
    return uploadPaperMapper.infoStatus(uploadPaperDTO);
  }
  public boolean updatePaper(UploadPaperDTO uploadPaperDTO, UploadPDFDTO uploadPDFDTO,
                             MultipartFile multipartFile
  ) throws IOException {

    // 폼 데이터를 데이터베이스에 저장
    uploadPaperMapper.updatePaper(uploadPaperDTO);

    if (multipartFile.getOriginalFilename().length()<1) {

//      //  파일 경로 생성
//      String resultPath = getDynamicFolderPath();
//      String fileName = UUID.randomUUID().toString() + "_" +uploadPDFDTO.getOriginalFile();
//
//      // 파일 저장 메서드
//      String filePath = saveFile(multipartFile.getBytes(), resultPath, fileName);
//      // 경로 수정
//      String path = filePath.replace("\\", "/").replaceFirst(".*?/upload", "").replaceFirst(fileName,"");
//      // PDF 파일 정보 저장
//      uploadPDFDTO.setPaperId(uploadPaperDTO.getId());
//      uploadPDFDTO.setPath(path);
//      uploadPDFDTO.setUploadFile(fileName);
//      uploadPDFDTO.setOriginalFile(uploadPDFDTO.getOriginalFile());

      return true;
    }else {
      try {

        //  파일 경로 생성
        String resultPath = getDynamicFolderPath();
        String fileName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

        // 파일 저장 메서드
        String filePath = saveFile(multipartFile.getBytes(), resultPath, fileName);
        // 경로 수정
        String path = filePath.replaceFirst(".*?/upload", "").replaceFirst(fileName,"");
        // PDF 파일 정보 저장
        uploadPDFDTO.setPaperId(uploadPaperDTO.getId());
        uploadPDFDTO.setPath(path);
        uploadPDFDTO.setUploadFile(fileName);
        uploadPDFDTO.setOriginalFile(multipartFile.getOriginalFilename());


        uploadPaperMapper.updatePDFFile(uploadPDFDTO);

        return true;
      } catch (IOException e) {
        log.error("파일 저장 중 오류 발생: {}", e.getMessage());
        return false;
      }
    }

  }

  public int deletePaper(UploadRequestDTO requestDTO) throws IOException {

    return  uploadPaperMapper.deletePaper(requestDTO);

  }
  public int deletePDF(UploadRequestDTO requestDTO)throws IOException {

    return uploadPaperMapper.deletePDF(requestDTO);


  }

  public int deleteItem(UploadRequestDTO requestDTO)throws IOException {

    return uploadPaperMapper.deleteItem(requestDTO);


  }

}
