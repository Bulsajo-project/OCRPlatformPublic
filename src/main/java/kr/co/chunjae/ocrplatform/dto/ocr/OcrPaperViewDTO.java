package kr.co.chunjae.ocrplatform.dto.ocr;

import lombok.Data;

import java.util.Date;

@Data
public class OcrPaperViewDTO {
    private Long id;
    private int pastYear;
    private String examType;
    private int pastMonth;
    private int grade;
    private String area;
    private String subject;
    private String examName;
    private String creator;
    private Date createDate;
}
