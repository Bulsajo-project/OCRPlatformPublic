package kr.co.chunjae.ocrplatform.dto.paper;

import lombok.Data;

import java.util.Date;

@Data
public class PaperExcelSaveDTO {
    private String id;
    private Integer pastYear;
    private String examType;
    private String pastMonth;
    private String grade;
    private String area;
    private String subject;
    private String examName;
    private String creator;
    private Date createDate;
}
