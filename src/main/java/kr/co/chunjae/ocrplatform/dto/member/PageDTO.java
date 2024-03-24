package kr.co.chunjae.ocrplatform.dto.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageDTO {
    private int page;      // 현재 페이지
    private int maxPage;   // 전체 필요한 페이지 개수
    private int startPage; // 현재 페이지 기준 시작 페이지 값
    private int endPage;   // 현재 페이지 기준 마지막 페이지 값

    private int offset; // sql limit-offset

    static public int postsPerPage = 10; // 1 페이지 당 글의 개수는 10
    static public int pageLimit = 5;     // 한 페이지에 보여지는 페이지는 5개.

    private Long id;    // 계정 id
    private int searchType; // 검색 유형
    private String keyword; // 검색어

    private int numOfAccounts; // 계정 수

    public PageDTO(Long id, int page, int searchType, String keyword) {
        this.id = id;
        this.page = page;
        this.searchType = searchType;
        this.keyword = keyword;
    }
}