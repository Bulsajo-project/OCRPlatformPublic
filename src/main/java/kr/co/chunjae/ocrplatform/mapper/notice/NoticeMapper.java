package kr.co.chunjae.ocrplatform.mapper.notice;

import kr.co.chunjae.ocrplatform.dto.notice.NoticeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper {

    // 공지사항 전체 게시글 수 조회
    int getTotalCount();

    // 공지사항 목록 조회
    List<NoticeDTO> getNoticeList(@Param("start") int start, @Param("pageSize") int pageSize); // 페이징 처리된 공지사항 목록을 조회하는 메서드

    // 공지사항 상세조회
    HashMap<String, Object> getNotice(Long id);

    // 공지사항 작성
    void writeNotice(NoticeDTO noticeDTO);

    // 공지사항 수정
    void updateNotice(NoticeDTO noticeDTO);

    // 공지사항 삭제
    void deleteNotice(Long id);

    // 공지사항 검색
    int getSearchCount(@Param("keyword") String keyword, @Param("condition") String condition);

    List<Map<String, Object>> searchNotice(@Param("keyword") String keyword, @Param("condition") String condition,
                                           @Param("start") int start, @Param("pageSize") int pageSize);
}


