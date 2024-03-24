package kr.co.chunjae.ocrplatform.dto.ocr;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditOcrTextRequsetDTO {

    private Long paperId;
    private int setOrd;

    @Size(max = 21844, message ="OCR 인식 결과가 너무 깁니다.") // (65535-2)/3 모든 한글이 3바이트라고 가정
    @NotBlank(message = "OCR 인식 결과를 입력해주세요.")
    private String ocrText;
}
