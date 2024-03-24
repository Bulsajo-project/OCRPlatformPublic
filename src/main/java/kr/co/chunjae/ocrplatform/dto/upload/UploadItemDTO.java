package kr.co.chunjae.ocrplatform.dto.upload;

import lombok.Data;

@Data
public class UploadItemDTO {

  private Long id;

  private Long paperId;
  private Long creator;
  private int itemOrder;


}
