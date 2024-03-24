package kr.co.chunjae.ocrplatform.vo.apiResponse.response;

import kr.co.chunjae.ocrplatform.service.common.MessageService;
import kr.co.chunjae.ocrplatform.vo.apiResponse.codes.ResponseStatusCode;
import lombok.Getter;

@Getter
public class ResponseData<T> {
    // API 응답 결과 Response
    private T data;

    // API 응답 코드 Response
    private int code;

    // API 응답 메시지
    private String message;

    public ResponseData(final ResponseStatusCode responseStatusCode, T result, MessageService messageService) {
        this.data = result;
        this.code = responseStatusCode.getErrno();
        this.message = messageService.getMessage(responseStatusCode.getMessageCode());
    }
}
