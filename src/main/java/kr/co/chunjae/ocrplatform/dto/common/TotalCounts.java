package kr.co.chunjae.ocrplatform.dto.common;

import lombok.Data;

@Data
public class TotalCounts {
    private Integer totalCount;

    public TotalCounts() {
        this.totalCount = 0;
    }
}