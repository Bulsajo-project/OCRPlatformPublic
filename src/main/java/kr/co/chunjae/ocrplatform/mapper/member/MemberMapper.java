package kr.co.chunjae.ocrplatform.mapper.member;

import kr.co.chunjae.ocrplatform.dto.member.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface MemberMapper {

    // 로그인
    MemberDTO login(MemberDTO memberDTO);

    // 비밀번호 수정
    void updatePassword(MemberDTO memberDTO);
  
    void loginLoged(MemberDTO loginResult);
    MemberDTO loginFail(MemberDTO memberDTO);
    void failLoginLoged(MemberDTO loginFail);

    // loginFlag 변경
    void setloginFlag(MemberDTO loginResult);
}
