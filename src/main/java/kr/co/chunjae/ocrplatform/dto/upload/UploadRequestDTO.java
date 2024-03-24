package kr.co.chunjae.ocrplatform.dto.upload;


import lombok.Data;

@Data
public class UploadRequestDTO {
  private Long id;
  private Long paperId;

  private int check;
}
