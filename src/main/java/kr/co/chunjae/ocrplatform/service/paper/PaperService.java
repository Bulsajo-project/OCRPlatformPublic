package kr.co.chunjae.ocrplatform.service.paper;

import kr.co.chunjae.ocrplatform.dto.paper.PaperExcelSaveDTO;
import kr.co.chunjae.ocrplatform.dto.paper.PaperListResponseDTO;

import kr.co.chunjae.ocrplatform.dto.common.SearchDTO;
import kr.co.chunjae.ocrplatform.dto.common.TotalCounts;
import kr.co.chunjae.ocrplatform.mapper.paper.PaperMapper;
import kr.co.chunjae.ocrplatform.utils.AreaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaperService {

    private final PaperMapper paperMapper;


    public List<PaperListResponseDTO> viewPaperList(SearchDTO paperSearchDTO) {
        int offset = (paperSearchDTO.getPageNum() - 1) * paperSearchDTO.getPageSize();
        Map<String, Object> params = new HashMap<>();

        params.put("offset", offset);
        params.put("pageSize", paperSearchDTO.getPageSize());
        params.put("year", paperSearchDTO.getYear());
        params.put("month", paperSearchDTO.getMonth());
        String areaValue = AreaUtil.switchArea(paperSearchDTO.getArea());
        params.put("area", areaValue);
        params.put("subject", paperSearchDTO.getSubject());
        params.put("grade", paperSearchDTO.getGrade());
        params.put("searchOption", paperSearchDTO.getSearchOption());
        params.put("searchText", paperSearchDTO.getSearchText());

        if (paperSearchDTO.getExamTypes() != null && !paperSearchDTO.getExamTypes().isEmpty()) {
            List<String> examTypesList = Arrays.asList(paperSearchDTO.getExamTypes().split(","));
            params.put("examTypes", examTypesList);

        }

        return paperMapper.paperListPage(params);
    }




    public TotalCounts totalCount(SearchDTO paperSearchDTO) {
        Map<String, Object> data = new HashMap<>();
        data.put("year", paperSearchDTO.getYear());
        data.put("month", paperSearchDTO.getMonth());
        String areaValue = AreaUtil.switchArea(paperSearchDTO.getArea());
        data.put("area", areaValue);
        data.put("subject", paperSearchDTO.getSubject());
        data.put("grade", paperSearchDTO.getGrade());
        data.put("searchOption", paperSearchDTO.getSearchOption());
        data.put("searchText", paperSearchDTO.getSearchText());
        if (paperSearchDTO.getExamTypes() != null && !paperSearchDTO.getExamTypes().isEmpty()) {
            List<String> examTypesList = Arrays.asList(paperSearchDTO.getExamTypes().split(","));
            data.put("examTypes", examTypesList);

        }
        return paperMapper.totalCnt(data);
    }


    public List<PaperExcelSaveDTO> getDataList(SearchDTO paperSearchDTO) {
        Map<String, Object> excelData = new HashMap<>();
        excelData.put("year", paperSearchDTO.getYear());
        excelData.put("month", paperSearchDTO.getMonth());
        String areaValue = AreaUtil.switchArea(paperSearchDTO.getArea());
        excelData.put("area", areaValue);
        excelData.put("subject", paperSearchDTO.getSubject());
        excelData.put("grade", paperSearchDTO.getGrade());
        excelData.put("searchOption", paperSearchDTO.getSearchOption());
        excelData.put("searchText", paperSearchDTO.getSearchText());
        if (paperSearchDTO.getExamTypes() != null && !paperSearchDTO.getExamTypes().isEmpty()) {
            List<String> examTypesList = Arrays.asList(paperSearchDTO.getExamTypes().split(","));
            excelData.put("examTypes", examTypesList);

        }
        return paperMapper.excelData(excelData);
    }
}
