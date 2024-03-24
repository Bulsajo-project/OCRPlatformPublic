package kr.co.chunjae.ocrplatform.dto.ocr;

import lombok.Data;

@Data
public class ProgressResponseDTO {
  private Long item_id;
  private String ocrTxt;
  private String cluster_text;
  private String elastic_text;
  private String subj_code;
  private String result;

}
