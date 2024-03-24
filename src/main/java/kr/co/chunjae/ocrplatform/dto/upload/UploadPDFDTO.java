package kr.co.chunjae.ocrplatform.dto.upload;


import lombok.Data;

@Data
public class UploadPDFDTO {

  private Long paperId;
  private String path;
  private String originalFile;
  private String uploadFile;
  private Long creator;
  private Long updater;

  private Long id;

}
