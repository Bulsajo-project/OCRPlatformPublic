package kr.co.chunjae.ocrplatform.mapper.management;

import kr.co.chunjae.ocrplatform.dto.member.MemberDTO;
import kr.co.chunjae.ocrplatform.dto.member.PageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ManagementMapper {

    // 사용자 계정 추가
    void saveMember(MemberDTO memberDTO);

    // 비밀번호 초기화
    void resetPassword(MemberDTO memberDTO);

    // 사용자 계정 비활성화
    void updateMember(MemberDTO memberDTO);


    int accountCountById(PageDTO pageDTO);

    List<MemberDTO> getAccountList(PageDTO pageDTO);

    // 아이디 중복확인
    int checkLoginId(String loginId);
}
