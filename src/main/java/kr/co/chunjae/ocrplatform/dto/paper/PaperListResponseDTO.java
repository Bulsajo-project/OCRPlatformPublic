package kr.co.chunjae.ocrplatform.dto.paper;

import lombok.Data;

import java.util.Date;

@Data
public class PaperListResponseDTO {
    private Integer id;
    private Integer pastYear;
    private String examType;
    private Integer pastMonth;
    private Integer grade;
    private String area;
    private String subject;
    private String examName;
    private String creator;
    private Date createDate;
}
