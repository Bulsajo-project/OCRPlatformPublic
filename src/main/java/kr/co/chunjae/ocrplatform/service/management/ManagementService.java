package kr.co.chunjae.ocrplatform.service.management;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.chunjae.ocrplatform.dto.member.MemberDTO;
import kr.co.chunjae.ocrplatform.dto.member.PageDTO;
import kr.co.chunjae.ocrplatform.mapper.management.ManagementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagementService {

    private final ManagementMapper managementMapper;

    // 사용자 계정 추가
    @Transactional
    public void saveMember(MemberDTO memberDTO, HttpServletRequest request) {
        memberDTO.setDepartment(memberDTO.getDepartment()); // 소속부서
        memberDTO.setName(memberDTO.getName());
        memberDTO.setLoginId(memberDTO.getLoginId());

        String encryptedPassword = Encrypt.getEncrypt(memberDTO.getLoginId());
        memberDTO.setPassword(encryptedPassword);

        memberDTO.setRole(memberDTO.getRole());

        Date startDate = memberDTO.getStartDate();
        if (startDate != null) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            startCalendar.set(Calendar.HOUR_OF_DAY, 0);
            startCalendar.set(Calendar.MINUTE, 0);
            startCalendar.set(Calendar.SECOND, 0);
            memberDTO.setStartDate(new Timestamp(startCalendar.getTimeInMillis()));
        }

        Date endDate = memberDTO.getEndDate();
        if (endDate != null) {
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            endCalendar.set(Calendar.HOUR_OF_DAY, 23);
            endCalendar.set(Calendar.MINUTE, 59);
            endCalendar.set(Calendar.SECOND, 59);
            memberDTO.setEndDate(new Timestamp(endCalendar.getTimeInMillis()));
        }

        // 로그인한 멤버 기준으로 creator
        memberDTO.setCreator((Long) request.getSession().getAttribute("id"));
        // 최초로그인
        memberDTO.setLoginFlag(memberDTO.getLoginFlag());
        managementMapper.saveMember(memberDTO);

    }

    // 비밀번호 초기화
    public void resetPassword(MemberDTO memberDTO, HttpServletRequest request) {
        Long updaterId = (Long) request.getSession().getAttribute("id");
        memberDTO.setUpdater(updaterId);
        String encryptedPassword = Encrypt.getEncrypt(memberDTO.getLoginId());
        memberDTO.setPassword(encryptedPassword);
        memberDTO.setLoginFlag(0);
        managementMapper.resetPassword(memberDTO);
    }

    // 사용자 계정 비활성화
    public void updateMember(MemberDTO memberDTO, HttpServletRequest request) {
        // 로그인한 멤버 기준으로 deleter
        Long deleterId = (Long) request.getSession().getAttribute("id");
        memberDTO.setDeleter(deleterId);

        managementMapper.updateMember(memberDTO);
    }

    public PageDTO initialize(Long id, int page, int searchType, String keyword) {
        // id, 현재 페이지, 검색 유형, 검색어 초기화
        PageDTO pageDTO = new PageDTO(id, page, searchType, keyword);

        // 전체 계정수
        int numOfAccounts = 0;

        numOfAccounts = managementMapper.accountCountById(pageDTO);
        pageDTO.setNumOfAccounts(numOfAccounts);

        // 전체 페이지 수
        int maxPage = (int)Math.ceil((double)numOfAccounts / pageDTO.postsPerPage);
        pageDTO.setMaxPage(maxPage);

        // 시작 페이지
        int startPage = ((int)(Math.ceil((double)page / pageDTO.pageLimit)) - 1) * pageDTO.pageLimit + 1;
        pageDTO.setStartPage(startPage);

        // 마지막 페이지
        int endPage = startPage + pageDTO.postsPerPage - 1;
        if(endPage > maxPage){
            endPage = maxPage;
        }
        pageDTO.setEndPage(endPage);

        return pageDTO;
    }

    // 계정 리스트
    public ArrayList<MemberDTO> getAccountList(PageDTO pageDTO) {
        pageDTO.setOffset((pageDTO.getPage()-1)*PageDTO.postsPerPage);

        List<MemberDTO> pagingList = managementMapper.getAccountList(pageDTO);
        ArrayList<MemberDTO> pagingAccountList = new ArrayList<>(pagingList);

        return pagingAccountList;

    }

    // 아이디 중복확인
    public int checkLoginId(String loginId) {
        return managementMapper.checkLoginId(loginId);
    }
}