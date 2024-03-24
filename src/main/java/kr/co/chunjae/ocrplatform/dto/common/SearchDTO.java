package kr.co.chunjae.ocrplatform.dto.common;

import lombok.Data;

@Data
public class SearchDTO {
    private String year;
    private String month;
    private String area;
    private String subject;
    private String grade;
    private String examTypes;
    private String searchOption;
    private String searchText;

    private int pageNum;
    private int pageSize;

    private String workStatus;
}
