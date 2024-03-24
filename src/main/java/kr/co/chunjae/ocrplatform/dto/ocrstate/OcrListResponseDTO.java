package kr.co.chunjae.ocrplatform.dto.ocrstate;

import lombok.Data;

import java.util.Date;

@Data
public class OcrListResponseDTO {
    private Integer id;
    private Integer pastYear;
    private String examType;
    private Integer pastMonth;
    private Integer grade;
    private String area;
    private String subject;
    private String examName;
    private String name;
    private Date createDate;
    private Integer itemCnt;
    private Integer ocrOk;
}