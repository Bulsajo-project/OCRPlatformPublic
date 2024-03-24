package kr.co.chunjae.ocrplatform.service.notice;


import kr.co.chunjae.ocrplatform.dto.notice.NoticeDTO;
import kr.co.chunjae.ocrplatform.mapper.notice.NoticeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeMapper noticeMapper;

    public int getTotalCount() {
        return noticeMapper.getTotalCount();
    }

    public List<NoticeDTO> getNoticeList(int start, int pageSize) {
        return noticeMapper.getNoticeList(start, pageSize);
    }

    // 공지사항 상세조회
    public HashMap<String, Object> getNotice(Long id) {
        return noticeMapper.getNotice(id);
    }

    // 공지사항 작성
    @Transactional
    public void writeNotice(NoticeDTO noticeDTO) {
        // creator가 null인지 확인
        if (noticeDTO.getCreator() == null) {
            // creator가 null이면 게시글 작성을 하지 않음
            throw new IllegalArgumentException("게시글 작성자가 지정되지 않았습니다.");
        }

        // 공지사항 등록
        noticeMapper.writeNotice(noticeDTO);
    }

    // 공지사항 수정
    public void updateNotice(NoticeDTO noticeDTO) {
        noticeMapper.updateNotice(noticeDTO);
    }

    // 공지사항 삭제
    public void deleteNotice(Long id) {
        noticeMapper.deleteNotice(id);
    }

    // 공지사항 검색
    public int getSearchCount(String keyword, String condition) {
        return noticeMapper.getSearchCount(keyword, condition);
    }

    public List<Map<String, Object>> searchNotice(String keyword, String condition, int start, int pageSize) {
        return noticeMapper.searchNotice(keyword, condition, start, pageSize);
    }
}



