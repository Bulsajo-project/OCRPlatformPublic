package kr.co.chunjae.ocrplatform.service.member;


import kr.co.chunjae.ocrplatform.dto.member.MemberDTO;
import kr.co.chunjae.ocrplatform.mapper.member.MemberMapper;
import kr.co.chunjae.ocrplatform.service.management.Encrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberMapper memberMapper;

  // 로그인
  public MemberDTO login(MemberDTO memberDTO) {
    String encryptedPassword = Encrypt.getEncrypt(memberDTO.getPassword());
    memberDTO.setPassword(encryptedPassword);
    return memberMapper.login(memberDTO);
  }

  // 비밀번호 수정
  public void updatePassword(MemberDTO memberDTO) {
    String encryptedPassword = Encrypt.getEncrypt(memberDTO.getPassword());
    memberDTO.setPassword(encryptedPassword);
    memberDTO.setLoginFlag(1);
    memberMapper.updatePassword(memberDTO);
  }
  
  public void loginLog(MemberDTO loginResult) {
    memberMapper.loginLoged(loginResult);
  }

  public MemberDTO loginFailed(MemberDTO memberDTO) {
    return memberMapper.loginFail(memberDTO);
  }

  public void failLoginLog(MemberDTO loginFail) {
    memberMapper.failLoginLoged(loginFail);
  }

}



