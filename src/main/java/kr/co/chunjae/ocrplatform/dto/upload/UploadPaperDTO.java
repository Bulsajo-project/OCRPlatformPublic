package kr.co.chunjae.ocrplatform.dto.upload;


import lombok.Data;

@Data
public class UploadPaperDTO {

  private Long id;
  private Long subjectId;
  private int pastYear;
  private int pastMonth;
  private int grade;
  private int examType;
  private String examName;
  private Long creator;
  private Long worker;
  private int status;
  private int itemCnt;
  private Long updater;


}