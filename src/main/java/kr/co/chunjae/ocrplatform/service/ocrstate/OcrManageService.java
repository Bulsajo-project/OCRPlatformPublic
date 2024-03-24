package kr.co.chunjae.ocrplatform.service.ocrstate;

import kr.co.chunjae.ocrplatform.dto.common.SearchDTO;
import kr.co.chunjae.ocrplatform.dto.common.TotalCounts;
import kr.co.chunjae.ocrplatform.dto.ocrstate.OcrExcelSaveDTO;
import kr.co.chunjae.ocrplatform.dto.ocrstate.OcrListResponseDTO;
import kr.co.chunjae.ocrplatform.dto.ocrstate.WorkerResponseDTO;
import kr.co.chunjae.ocrplatform.dto.ocrstate.WorkerDTO;
import kr.co.chunjae.ocrplatform.mapper.ocrstate.OcrManageMapper;
import kr.co.chunjae.ocrplatform.utils.AreaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OcrManageService {
    private final OcrManageMapper ocrManageMapper;

    public List<OcrListResponseDTO> viewList(SearchDTO ocrSearchDTO) {
        int offset = (ocrSearchDTO.getPageNum() - 1) * ocrSearchDTO.getPageSize();
        Map<String, Object> params = new HashMap<>();

        params.put("offset", offset);
        params.put("pageSize", ocrSearchDTO.getPageSize());
        params.put("year", ocrSearchDTO.getYear());
        params.put("month", ocrSearchDTO.getMonth());

        String areaValue = AreaUtil.switchArea(ocrSearchDTO.getArea());
        params.put("area", areaValue);

        params.put("subject", ocrSearchDTO.getSubject());
        params.put("grade", ocrSearchDTO.getGrade());
        params.put("searchOption", ocrSearchDTO.getSearchOption());
        params.put("searchText", ocrSearchDTO.getSearchText());

        if (ocrSearchDTO.getExamTypes() != null && !ocrSearchDTO.getExamTypes().isEmpty()) {
            List<String> examTypesList = Arrays.asList(ocrSearchDTO.getExamTypes().split(","));
            params.put("examTypes", examTypesList);

        }
        params.put("workStatus", ocrSearchDTO.getWorkStatus());

        return ocrManageMapper.ocrListPage(params);
    }


    public TotalCounts totalCountOcr(SearchDTO ocrSearchDTO) {
        Map<String, Object> data = new HashMap<>();
        data.put("year", ocrSearchDTO.getYear());
        data.put("month", ocrSearchDTO.getMonth());
        String areaValue = AreaUtil.switchArea(ocrSearchDTO.getArea());
        data.put("area", areaValue);
        data.put("subject", ocrSearchDTO.getSubject());
        data.put("grade", ocrSearchDTO.getGrade());
        data.put("searchOption", ocrSearchDTO.getSearchOption());
        data.put("searchText", ocrSearchDTO.getSearchText());
        if (ocrSearchDTO.getExamTypes() != null && !ocrSearchDTO.getExamTypes().isEmpty()) {
            List<String> examTypesList = Arrays.asList(ocrSearchDTO.getExamTypes().split(","));
            data.put("examTypes", examTypesList);

        }

        data.put("workStatus", ocrSearchDTO.getWorkStatus());

        return ocrManageMapper.totalCntOcr(data);
    }


    public List<OcrExcelSaveDTO> getDataList(SearchDTO ocrSearchDTO) {
        Map<String, Object> excelData = new HashMap<>();
        excelData.put("year", ocrSearchDTO.getYear());
        excelData.put("month", ocrSearchDTO.getMonth());
        String areaValue = AreaUtil.switchArea(ocrSearchDTO.getArea());
        excelData.put("area", areaValue);
        excelData.put("subject", ocrSearchDTO.getSubject());
        excelData.put("grade", ocrSearchDTO.getGrade());
        excelData.put("searchOption", ocrSearchDTO.getSearchOption());
        excelData.put("searchText", ocrSearchDTO.getSearchText());
        if (ocrSearchDTO.getExamTypes() != null && !ocrSearchDTO.getExamTypes().isEmpty()) {
            List<String> examTypesList = Arrays.asList(ocrSearchDTO.getExamTypes().split(","));
            excelData.put("examTypes", examTypesList);

        }

        excelData.put("workStatus", ocrSearchDTO.getWorkStatus());

        return ocrManageMapper.ocrExcelData(excelData);
    }

    public Integer updateWorker(WorkerDTO paperId, Integer loginUser) {
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("id", paperId.getId());
        updateParams.put("worker", loginUser);

        return ocrManageMapper.updateWorkers(updateParams);
    }

    public List<OcrListResponseDTO> viewOcrMnageList(SearchDTO ocrSearchDTO) {
        int offset = (ocrSearchDTO.getPageNum() - 1) * ocrSearchDTO.getPageSize();
        Map<String, Object> manageParams = new HashMap<>();

        manageParams.put("offset", offset);
        manageParams.put("pageSize", ocrSearchDTO.getPageSize());
        manageParams.put("year", ocrSearchDTO.getYear());
        manageParams.put("month", ocrSearchDTO.getMonth());
        String areaValue = AreaUtil.switchArea(ocrSearchDTO.getArea());
        manageParams.put("area", areaValue);
        manageParams.put("subject", ocrSearchDTO.getSubject());
        manageParams.put("grade", ocrSearchDTO.getGrade());
        manageParams.put("searchOption", ocrSearchDTO.getSearchOption());
        manageParams.put("searchText", ocrSearchDTO.getSearchText());

        if (ocrSearchDTO.getExamTypes() != null && !ocrSearchDTO.getExamTypes().isEmpty()) {
            List<String> examTypesList = Arrays.asList(ocrSearchDTO.getExamTypes().split(","));
            manageParams.put("examTypes", examTypesList);

        }

        return ocrManageMapper.ocrManageListPage(manageParams);
    }

    public List<OcrExcelSaveDTO> getDataManageList(SearchDTO ocrSearchDTO) {
        Map<String, Object> excelManageData = new HashMap<>();
        excelManageData.put("year", ocrSearchDTO.getYear());
        excelManageData.put("month", ocrSearchDTO.getMonth());
        String areaValue = AreaUtil.switchArea(ocrSearchDTO.getArea());
        excelManageData.put("area", areaValue);
        excelManageData.put("subject", ocrSearchDTO.getSubject());
        excelManageData.put("grade", ocrSearchDTO.getGrade());
        excelManageData.put("searchOption", ocrSearchDTO.getSearchOption());
        excelManageData.put("searchText", ocrSearchDTO.getSearchText());
        if (ocrSearchDTO.getExamTypes() != null && !ocrSearchDTO.getExamTypes().isEmpty()) {
            List<String> examTypesList = Arrays.asList(ocrSearchDTO.getExamTypes().split(","));
            excelManageData.put("examTypes", examTypesList);

        }

        return ocrManageMapper.ocrExcelMangeData(excelManageData);
    }

    public TotalCounts totalCountOcrMange(SearchDTO ocrSearchDTO) {
        Map<String, Object> countData = new HashMap<>();
        countData.put("year", ocrSearchDTO.getYear());
        countData.put("month", ocrSearchDTO.getMonth());
        String areaValue = AreaUtil.switchArea(ocrSearchDTO.getArea());
        countData.put("area", areaValue);
        countData.put("subject", ocrSearchDTO.getSubject());
        countData.put("grade", ocrSearchDTO.getGrade());
        countData.put("searchOption", ocrSearchDTO.getSearchOption());
        countData.put("searchText", ocrSearchDTO.getSearchText());
        if (ocrSearchDTO.getExamTypes() != null && !ocrSearchDTO.getExamTypes().isEmpty()) {
            List<String> examTypesList = Arrays.asList(ocrSearchDTO.getExamTypes().split(","));
            countData.put("examTypes", examTypesList);

        }


        return ocrManageMapper.totalCntOcrManage(countData);
    }

    public WorkerResponseDTO getWorker(WorkerDTO paperId) {
        return ocrManageMapper.workerId(paperId.getId());

    }

    public Integer deleteWorker(WorkerDTO paperId) {
        return ocrManageMapper.deleteWorker(paperId);
    }
}
