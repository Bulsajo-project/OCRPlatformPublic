package kr.co.chunjae.ocrplatform.dto.ocr;

import lombok.Data;

import java.util.List;

@Data
public class ProgressRequestDTO {
  private Long paperId;
  private int setOrd;
  private List<String> imgSrcList;
  private String subjCode;
  private List<String> imgTypeList;
}

