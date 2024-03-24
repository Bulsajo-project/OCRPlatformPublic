package kr.co.chunjae.ocrplatform.dto.ocr;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OcrItemImageDTO {
  private Long id;
  private Long itemId;
  private String path;
  private String originalFile;
  private String uploadFile;
  private Long creator;
  private Timestamp createDate;
  private Long updater;
  private Timestamp updateDate;
  private String imageType;

}
