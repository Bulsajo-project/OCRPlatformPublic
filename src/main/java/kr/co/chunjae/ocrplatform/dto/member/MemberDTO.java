package kr.co.chunjae.ocrplatform.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class MemberDTO {
  private Long num; // 넘버링
  private Long id; // 회원 id
  private int department; // 소속부서

  @NotNull
  @NotBlank(message = "이름은 공백일 수 없습니다.")
  @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z]{2,8}$", message = "이름은 특수문자, 숫자를 제외한 2~8자리여야 합니다.")
  private String name; // 이름

  @NotNull
  @NotBlank(message = "아이디는 공백일 수 없습니다.")
  @Pattern(regexp = "^[a-zA-Z0-9]{3,10}$", message = "아이디는 3~10자리, 알파벳 대소문자/숫자만 입력 가능합니다.")
  private String loginId; // 아이디

  private String password; // 비밀번호
  private Timestamp startDate; //시작일
  private Timestamp endDate; // 종료일
  private int status; // 계정상태 -> 0: 계정 활성화, 1: 비활성화(로그인 불가)
  private int role; // 권한 -> 1: 총괄관리자, 2: 수집담당자, 3: 문제운영자, 4: 문제검수자
  private Timestamp createDate; // 등록일
  private Long creator; // 등록자(현재 로그인되어있는 사람 기준으로 사용자 생성)
  private Timestamp updateDate; // 수정일
  private Long updater; // 수정자
  private Timestamp deleteDate; // 계정 삭제일
  private Long deleter; // 삭제자

  private int loginFlag; // 최초 로그인 판별 - 0: 최초로그인, 1: 로그인 경험 O


}
