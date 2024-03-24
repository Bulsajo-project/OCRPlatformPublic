package kr.co.chunjae.ocrplatform.dto.ocrstate;

import lombok.Data;

import java.util.Date;

@Data
public class OcrExcelSaveDTO {
    private String id;
    private Integer pastYear;
    private String examType;
    private String pastMonth;
    private String grade;
    private String area;
    private String subject;
    private String examName;
    private String name;
    private Date createDate;
    private Integer itemCnt;
    private Integer ocrOk;
}
