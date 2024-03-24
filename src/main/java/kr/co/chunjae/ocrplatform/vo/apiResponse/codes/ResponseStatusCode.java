package kr.co.chunjae.ocrplatform.vo.apiResponse.codes;
import lombok.Getter;



@Getter
public enum ResponseStatusCode {
    SUCCESS(0, "response.success"),
    ERROR_FAIL(-1, "response.error.fail"),
    ERROR_EXISTS(-10, "response.error.exists"),
    ERROR_LOGIN(-100, "response.error.login"),
    SUCCESS_MOVE(10, "response.success.move"),
    ERROR_COMPLETE(-20, "response.error.complete"),
    ERROR_ROLE(-30, "response.error.role"),
    ERROR_ACCESS(-40, "response.error.access"),
    ERROR_NODATA(-50, "response.error.nodata"),
    ADMIN(1000, "response.admin"),
    CHECKS(100, "response.checks"),
    NOWORKER(-70, "response.noworker")
    ;

    private final int errno;
    private final String messageCode;


    ResponseStatusCode(final int errno, final String messageCode) {
        this.errno = errno;
        this.messageCode = messageCode;
    }

    public String getMessageCode(){
        return this.messageCode;
    }
}
